package net.crunkhouse.shaitzov;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.crunkhouse.shaitzov.cards.PlayingCard;
import net.crunkhouse.shaitzov.ui.PlayingCardAdapter;

import java.util.ArrayList;

/**
 * Collect Firebase-related constants and methods
 */

public final class FirebaseUtils {
    // Global database items, visible to everyone
    private static final String DB_KEY_DECK = "deck";
    private static final String DB_KEY_PILE = "pile";
    // Database items nested under the player name
    private static final String DB_KEY_HAND = "hand";
    private static final String DB_KEY_FACE_UP = "face_up";
    private static final String DB_KEY_FACE_DOWN = "face_down";

    private FirebaseUtils() {
    }

    public static void putCards(ArrayList<PlayingCard> deck,
                                ArrayList<PlayingCard> pile,
                                ArrayList<PlayingCard> playerHand,
                                ArrayList<PlayingCard> playerFaceUp,
                                ArrayList<PlayingCard> playerFaceDown) {
        putDeck(deck);
        putPile(pile);
        putPlayerHand(playerHand);
        putPlayerFaceUp(playerFaceUp);
        putPlayerFaceDown(playerFaceDown);
    }

    public static void putPlayerFaceDown(ArrayList<PlayingCard> playerFaceDown) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCurrentPlayer = database.getReference(LocalPreferences.getInstance().getPlayerNickname());
        DatabaseReference dbFaceDown = dbCurrentPlayer.child(DB_KEY_FACE_DOWN);
        dbFaceDown.setValue(playerFaceDown);
    }

    public static void putPlayerFaceUp(ArrayList<PlayingCard> playerFaceUp) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCurrentPlayer = database.getReference(LocalPreferences.getInstance().getPlayerNickname());
        DatabaseReference dbFaceUp = dbCurrentPlayer.child(DB_KEY_FACE_UP);
        dbFaceUp.setValue(playerFaceUp);
    }

    public static void putPlayerHand(ArrayList<PlayingCard> playerHand) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCurrentPlayer = database.getReference(LocalPreferences.getInstance().getPlayerNickname());
        DatabaseReference dbHand = dbCurrentPlayer.child(DB_KEY_HAND);
        dbHand.setValue(playerHand);
    }

    public static void putPile(ArrayList<PlayingCard> pile) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbPile = database.getReference(DB_KEY_PILE);
        dbPile.setValue(pile);
    }

    public static void putDeck(ArrayList<PlayingCard> deck) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbDeck = database.getReference(DB_KEY_DECK);
        dbDeck.setValue(deck);
    }

    public static void getRemoteDeck(final PlayingCardAdapter deckAdapter, final ResultListener resultListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbDeck = database.getReference(DB_KEY_DECK);
        dbDeck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean deckExists = false;
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    deckAdapter.add(card.getValue(PlayingCard.class));
                    deckExists = true;
                }
                resultListener.onResult(deckExists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void getRemoteCards(final PlayingCardAdapter deckAdapter,
                                      final PlayingCardAdapter pileAdapter,
                                      final PlayingCardAdapter playerHandAdapter,
                                      final PlayingCardAdapter faceUpAdapter,
                                      final PlayingCardAdapter faceDownAdapter,
                                      final ResultListener resultListener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Deck
        DatabaseReference dbDeck = database.getReference(DB_KEY_DECK);
        dbDeck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    if (deckAdapter != null) {
                        deckAdapter.add(card.getValue(PlayingCard.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // Pile
        DatabaseReference dbPile = database.getReference(DB_KEY_PILE);
        dbPile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    pileAdapter.add(card.getValue(PlayingCard.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // Get current player
        DatabaseReference dbCurrentPlayer = database.getReference(LocalPreferences.getInstance().getPlayerNickname());
        // Player Hand
        DatabaseReference dbHand = dbCurrentPlayer.child(DB_KEY_HAND);
        dbHand.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    playerHandAdapter.add(card.getValue(PlayingCard.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Player Face-Up
        DatabaseReference dbFaceUp = dbCurrentPlayer.child(DB_KEY_FACE_UP);
        dbFaceUp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    faceUpAdapter.add(card.getValue(PlayingCard.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Player Face-Down
        DatabaseReference dbFaceDown = dbCurrentPlayer.child(DB_KEY_FACE_DOWN);
        dbFaceDown.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean playerWasDealt = false;
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    faceDownAdapter.add(card.getValue(PlayingCard.class));
                    playerWasDealt = true;
                }
                resultListener.onResult(playerWasDealt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void clearGame() {
        putCards(null, null, null, null, null);
    }

    interface ResultListener {
        void onResult(boolean success);
    }
}
