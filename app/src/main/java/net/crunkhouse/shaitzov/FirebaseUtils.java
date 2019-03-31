package net.crunkhouse.shaitzov;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import net.crunkhouse.shaitzov.cards.PlayingCard;
import net.crunkhouse.shaitzov.cards.PlayingCardUtils;
import net.crunkhouse.shaitzov.ui.PlayingCardAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * Collect Firebase-related constants and methods
 */

final class FirebaseUtils {
    // Global database items, visible to everyone
    private static final String DB_KEY_GAMES = "games";
    private static final String DB_KEY_DECK = "deck";
    private static final String DB_KEY_PILE = "pile";
    private static final String DB_KEY_PLAYERS = "players";
    // Database items nested under the player name
    private static final String DB_KEY_HAND = "hand";
    private static final String DB_KEY_FACE_UP = "face_up";
    private static final String DB_KEY_FACE_DOWN = "face_down";

    private FirebaseUtils() {
    }

    static void putCards(@NonNull ArrayList<PlayingCard> deck,
                         @NonNull ArrayList<PlayingCard> pile,
                         @NonNull ArrayList<PlayingCard> playerHand,
                         @NonNull ArrayList<PlayingCard> playerFaceUp,
                         @NonNull ArrayList<PlayingCard> playerFaceDown,
                         LocalPreferences prefs) {
        putDeck(deck, prefs);
        putPile(pile, prefs);
        putPlayerHand(playerHand, prefs);
        putPlayerFaceUp(playerFaceUp, prefs);
        putPlayerFaceDown(playerFaceDown, prefs);
    }

    private static void putPlayerFaceDown(@NonNull ArrayList<PlayingCard> playerFaceDown,
                                          LocalPreferences prefs) {
        Map<String, ArrayList<PlayingCard>> map = new HashMap<>();
        map.put(DB_KEY_FACE_DOWN, playerFaceDown);
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .collection(DB_KEY_PLAYERS)
                .document(prefs.getPlayerNickname())
                .set(map, SetOptions.merge());
    }

    private static void putPlayerFaceUp(@NonNull ArrayList<PlayingCard> playerFaceUp,
                                        LocalPreferences prefs) {
        Map<String, ArrayList<PlayingCard>> map = new HashMap<>();
        map.put(DB_KEY_FACE_UP, playerFaceUp);
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .collection(DB_KEY_PLAYERS)
                .document(prefs.getPlayerNickname())
                .set(map, SetOptions.merge());
    }

    private static void putPlayerHand(@NonNull ArrayList<PlayingCard> playerHand,
                                      LocalPreferences prefs) {
        Map<String, ArrayList<PlayingCard>> map = new HashMap<>();
        map.put(DB_KEY_HAND, playerHand);
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .collection(DB_KEY_PLAYERS)
                .document(prefs.getPlayerNickname())
                .set(map, SetOptions.merge());
    }

    private static void putPile(@NonNull ArrayList<PlayingCard> pile,
                                LocalPreferences prefs) {
        Map<String, ArrayList<PlayingCard>> map = new HashMap<>();
        map.put(DB_KEY_PILE, pile);
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .set(map, SetOptions.merge());
    }

    private static void putDeck(@NonNull ArrayList<PlayingCard> deck,
                                LocalPreferences prefs) {
        Map<String, ArrayList<PlayingCard>> map = new HashMap<>();
        map.put(DB_KEY_DECK, deck);
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .set(map, SetOptions.merge());
    }

    static void checkGameExists(final ResultListener resultListener,
                                String gameId) {
        Timber.d("Requesting remote game");
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(gameId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Timber.d("Requesting remote game complete");
                        boolean deckExists = false;
                        DocumentSnapshot document = task.getResult();
                        if (task.isSuccessful() && document.exists()) {
                            deckExists = true;
                        }
                        resultListener.onResult(deckExists);
                    }
                });
    }

    static void getRemoteCards(final PlayingCardAdapter deckAdapter,
                               final PlayingCardAdapter pileAdapter,
                               final PlayingCardAdapter playerHandAdapter,
                               final PlayingCardAdapter faceUpAdapter,
                               final PlayingCardAdapter faceDownAdapter,
                               final ResultListener playerWasDealtListener,
                               LocalPreferences prefs) {
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (task.isSuccessful() && document.exists()) {
                            Map<String, Object> data = document.getData();
                            deckAdapter.addAll(PlayingCardUtils.makeCardListFrom((ArrayList<HashMap>) data.get(DB_KEY_DECK)));
                            pileAdapter.addAll(PlayingCardUtils.makeCardListFrom((ArrayList<HashMap>) data.get(DB_KEY_PILE)));
                        }
                    }
                });
        FirebaseFirestore.getInstance()
                .collection(DB_KEY_GAMES)
                .document(prefs.getGameId())
                .collection(DB_KEY_PLAYERS)
                .document(prefs.getPlayerNickname())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Player-specific
                        DocumentSnapshot user = task.getResult();
                        if (task.isSuccessful() && user.exists()) {
                            Map<String, Object> data = user.getData();
                            playerHandAdapter.addAll(PlayingCardUtils.makeCardListFrom((ArrayList<HashMap>) data.get(DB_KEY_HAND)));
                            faceUpAdapter.addAll(PlayingCardUtils.makeCardListFrom((ArrayList<HashMap>) data.get(DB_KEY_FACE_UP)));
                            faceDownAdapter.addAll(PlayingCardUtils.makeCardListFrom((ArrayList<HashMap>) data.get(DB_KEY_FACE_DOWN)));

                            playerWasDealtListener.onResult(true);
                        } else {
                            playerWasDealtListener.onResult(false);
                        }
                    }
                });
    }

    static void clearGameInBackground(LocalPreferences prefs) {
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_DECK));
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_PILE));

        String playerName = prefs.getPlayerNickname();
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_PLAYERS).document(playerName).collection(DB_KEY_HAND));
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_PLAYERS).document(playerName).collection(DB_KEY_FACE_UP));
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_PLAYERS).document(playerName).collection(DB_KEY_FACE_DOWN));
        deleteCollection(FirebaseFirestore.getInstance().collection(DB_KEY_PLAYERS));
    }

    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    private static void deleteCollection(final CollectionReference collection) {
        new DeleteTask().execute(collection);
    }

    // AsyncTask
    private static class DeleteTask extends AsyncTask<CollectionReference, Void, Void> {
        @Override
        protected Void doInBackground(CollectionReference... collectionReferences) {
            CollectionReference collection = collectionReferences[0];
            // Get the first batch of documents in the collection
            Query query = collection.orderBy(FieldPath.documentId()).limit(100);
            QuerySnapshot querySnapshot = null;
            try {
                querySnapshot = Tasks.await(query.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WriteBatch batch = query.getFirestore().batch();
            if (querySnapshot != null) {
                for (DocumentSnapshot snapshot : querySnapshot) {
                    batch.delete(snapshot.getReference());
                }
            }
            batch.commit();
            return null;
        }
    }

    interface ResultListener {
        void onResult(boolean success);
    }
}
