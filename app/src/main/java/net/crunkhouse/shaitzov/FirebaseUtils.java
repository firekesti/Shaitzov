package net.crunkhouse.shaitzov;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.crunkhouse.shaitzov.cards.PlayingCard;

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference dbDeck = database.getReference(DB_KEY_DECK);
        dbDeck.setValue(deck);
        DatabaseReference dbPile = database.getReference(DB_KEY_PILE);
        dbPile.setValue(pile);

        // TODO: unique player IDs
        DatabaseReference dbCurrentPlayer = database.getReference("Player1");
        DatabaseReference dbHand = dbCurrentPlayer.child(DB_KEY_HAND);
        dbHand.setValue(playerHand);
        DatabaseReference dbFaceUp = dbCurrentPlayer.child(DB_KEY_FACE_UP);
        dbFaceUp.setValue(playerFaceUp);
        DatabaseReference dbFaceDown = dbCurrentPlayer.child(DB_KEY_FACE_DOWN);
        dbFaceDown.setValue(playerFaceDown);
    }
}
