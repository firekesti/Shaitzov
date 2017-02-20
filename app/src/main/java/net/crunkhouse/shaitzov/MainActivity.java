package net.crunkhouse.shaitzov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

import static net.crunkhouse.shaitzov.CardSource.DECK;
import static net.crunkhouse.shaitzov.CardSource.FACE_DOWN;
import static net.crunkhouse.shaitzov.CardSource.FACE_UP;
import static net.crunkhouse.shaitzov.CardSource.PILE;

public class MainActivity extends AppCompatActivity {
    private static final int INITIAL_HAND_SIZE = 3;
    private static final int FACE_UP_AND_DOWN_CARD_AMOUNT = 3;

    private PlayingCardAdapter playerHandAdapter;
    private PlayingCardAdapter pileAdapter;
    private PlayingCardAdapter deckAdapter;
    private PlayingCardAdapter faceDownAdapter;
    private PlayingCardAdapter faceUpAdapter;
    private RecyclerView handView;
    private RecyclerView pileView;
    private PileDirection currentDirection = PileDirection.UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate cards
        ArrayList<PlayingCard> playerHand = new ArrayList<>(INITIAL_HAND_SIZE);
        ArrayList<PlayingCard> playerFaceDown = new ArrayList<>(FACE_UP_AND_DOWN_CARD_AMOUNT);
        ArrayList<PlayingCard> playerFaceUp = new ArrayList<>(FACE_UP_AND_DOWN_CARD_AMOUNT);
        ArrayList<PlayingCard> pile = new ArrayList<>(0);

        // Populate deck
        ArrayList<PlayingCard> deck = PlayingCardUtils.makeDeck();

        // Add player face-down cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceDown.add(PlayingCardUtils.drawFrom(deck));
        }
        RecyclerView faceDownView = (RecyclerView) findViewById(R.id.player_facedown);
        faceDownAdapter = new PlayingCardAdapter(playerFaceDown, FACE_DOWN);
        faceDownView.setAdapter(faceDownAdapter);
        faceDownView.addItemDecoration(new CardSpacingDecorator(faceDownView.getContext()));
        faceDownView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player face-up cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceUp.add(PlayingCardUtils.drawFrom(deck));
        }
        RecyclerView faceUpView = (RecyclerView) findViewById(R.id.player_faceup);
        faceUpAdapter = new PlayingCardAdapter(playerFaceUp, FACE_UP);
        faceUpView.setAdapter(faceUpAdapter);
        faceUpView.addItemDecoration(new CardSpacingDecorator(faceUpView.getContext()));
        faceUpView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player hand
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            playerHand.add(PlayingCardUtils.drawFrom(deck));
        }
        Collections.sort(playerHand);
        handView = (RecyclerView) findViewById(R.id.player_hand);
        playerHandAdapter = new PlayingCardAdapter(playerHand, CardSource.HAND);
        handView.setAdapter(playerHandAdapter);
        handView.addItemDecoration(new HandOverlapDecorator(handView.getContext()));
        handView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add the deck
        RecyclerView deckView = (RecyclerView) findViewById(R.id.deck);
        deckAdapter = new PlayingCardAdapter(deck, DECK);
        deckView.setAdapter(deckAdapter);
        deckView.addItemDecoration(new DeckOverlapDecorator(deckView.getContext()));
        deckView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        // Add the pile
        pileView = (RecyclerView) findViewById(R.id.pile);
        pileAdapter = new PlayingCardAdapter(pile, PILE);
        pileView.setAdapter(pileAdapter);
        pileView.addItemDecoration(new PileOverlapDecorator(pileView.getContext()));
        pileView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Subscribe
    public void onCardClicked(CardClickedEvent event) {
        PlayingCard card = event.getCard();
        switch (event.getSource()) {
            case HAND:
                // Can only play from the hand if it's a valid card.
                if (playCardSuccessful(card)) {
                    playerHandAdapter.remove(card);
                    // Now, if the user has less than 3 cards and there is still a deck,
                    // one of two options...remind them to draw, or auto-draw for them.
                    // Let's auto-draw for now.
                    // TODO: make this a setting?
                    if (deckAdapter.getItemCount() > 0 && playerHandAdapter.getItemCount() < 3) {
                        //Toast.makeText(this, "You should draw more cards!", Toast.LENGTH_SHORT).show();
                        onCardClicked(new CardClickedEvent(DECK, null));
                    }
                }
                break;
            case DECK:
                // If a deck card was clicked, we want to actually draw the top card as long as our hand is less than 3
                if (playerHandAdapter.getItemCount() < 3) {
                    card = deckAdapter.getCards().get(deckAdapter.getItemCount() - 1);
                    deckAdapter.remove(card);
                    playerHandAdapter.add(card);
                    handView.scrollToPosition(playerHandAdapter.getItemCount() - 1);
                    // TODO: snackbar it up?
                    Toast.makeText(this, "You drew a " + card.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: snackbar it up?
                    Toast.makeText(this, "You can't draw more cards, you have 3 or more.", Toast.LENGTH_SHORT).show();
                }
                break;
            case FACE_UP:
                // Only do something if all player-hand cards are gone
                if (playerHandAdapter.getItemCount() == 0) {
                    if (playCardSuccessful(card)) {
                        faceUpAdapter.remove(card);
                    }
                } else {
                    // Tell user they can't play that card right now.
                    // TODO: snackbar it up?
                    Toast.makeText(this, "You can't play a face-up card while you have a hand!", Toast.LENGTH_SHORT).show();
                }
                break;
            case FACE_DOWN:
                // Only do something if all face-up cards AND hand cards are gone
                if (faceUpAdapter.getItemCount() == 0 && playerHandAdapter.getItemCount() == 0) {
                    if (playCardSuccessful(card)) {
                        faceDownAdapter.remove(card);
                    } else {
                        // Tell user they can't play that card right now.
                        // TODO: snackbar it up?
                        Toast.makeText(this, "You can't play a face-down card while you have a hand or face-up cards!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case PILE:
                // take all cards from pile, put in hand
                ArrayList<PlayingCard> cards = pileAdapter.getCards();
                playerHandAdapter.addAll(cards);
                handView.scrollToPosition(playerHandAdapter.getItemCount() - 1);
                pileAdapter.clear();
                currentDirection = PileDirection.UP;
            default:
                break;
        }
    }

    private boolean playCardSuccessful(PlayingCard card) {
        if (GameRuleUtils.canPlayCardOnPile(card, pileAdapter.getCards(), currentDirection)) {
            pileAdapter.add(card);
            pileView.scrollToPosition(pileAdapter.getItemCount() - 1);

            // Resolve special effects for 10, 4x, 7, 8
            if (GameRuleUtils.shouldBurnPile(card, pileAdapter.getCards())) {
                pileAdapter.clear();
                currentDirection = PileDirection.UP;
            } else if (GameRuleUtils.shouldDirectionSwitch(card)) {
                if (currentDirection == PileDirection.DOWN) {
                    currentDirection = PileDirection.UP;
                } else {
                    currentDirection = PileDirection.DOWN;
                }
            } else if (GameRuleUtils.shouldSkipPlayer(card)) {
                // TODO: resolve special effects for 8 (doesn't matter for single player)
            }
            return true;
        } else {
            // Tell user they can't play that card right now.
            // TODO: snackbar it up?
            Toast.makeText(this, "You can't play a " + card.getValueName() + " on that pile", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
