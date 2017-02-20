package net.crunkhouse.shaitzov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

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
        faceDownAdapter = new PlayingCardAdapter(playerFaceDown, CardSource.FACE_DOWN);
        faceDownView.setAdapter(faceDownAdapter);
        faceDownView.addItemDecoration(new CardSpacingDecorator(faceDownView.getContext()));
        faceDownView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player face-up cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceUp.add(PlayingCardUtils.drawFrom(deck));
        }
        RecyclerView faceUpView = (RecyclerView) findViewById(R.id.player_faceup);
        faceUpAdapter = new PlayingCardAdapter(playerFaceUp, CardSource.FACE_UP);
        faceUpView.setAdapter(faceUpAdapter);
        faceUpView.addItemDecoration(new CardSpacingDecorator(faceUpView.getContext()));
        faceUpView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player hand
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            playerHand.add(PlayingCardUtils.drawFrom(deck));
        }
        handView = (RecyclerView) findViewById(R.id.player_hand);
        playerHandAdapter = new PlayingCardAdapter(playerHand, CardSource.HAND);
        handView.setAdapter(playerHandAdapter);
        handView.addItemDecoration(new CardOverlapDecorator(handView.getContext()));
        handView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add the deck
        RecyclerView deckView = (RecyclerView) findViewById(R.id.deck);
        deckAdapter = new PlayingCardAdapter(deck, CardSource.DECK);
        deckView.setAdapter(deckAdapter);
        deckView.addItemDecoration(new DeckOverlapDecorator(deckView.getContext()));
        deckView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        // Add the pile
        pileView = (RecyclerView) findViewById(R.id.pile);
        pileAdapter = new PlayingCardAdapter(pile, CardSource.PILE);
        pileView.setAdapter(pileAdapter);
        pileView.addItemDecoration(new PileOverlapDecorator(pileView.getContext()));
        pileView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Subscribe
    public void onCardClicked(CardClickedEvent event) {
        PlayingCard card = event.getCard();
        switch (event.getSource()) {
            case HAND:
                // Can always play from the hand. Let's start here.
                playerHandAdapter.remove(card);
                pileAdapter.add(card);
                pileView.scrollToPosition(pileAdapter.getItemCount() - 1);
                break;
            case DECK:
                // If a deck card was clicked, we want to actually draw the top card
                card = deckAdapter.getCards().get(deckAdapter.getItemCount() - 1);
                deckAdapter.remove(card);
                playerHandAdapter.add(card);
                handView.scrollToPosition(playerHandAdapter.getItemCount() - 1);
                break;
            case FACE_UP:
                // Only do something if all player-hand cards are gone
                if (playerHandAdapter.getItemCount() == 0) {
                    faceUpAdapter.remove(card);
                    pileAdapter.add(card);
                    pileView.scrollToPosition(pileAdapter.getItemCount() - 1);
                }
                break;
            case FACE_DOWN:
                // Only do something if all face-up cards AND hand cards are gone
                if (faceUpAdapter.getItemCount() == 0 && playerHandAdapter.getItemCount() == 0) {
                    faceDownAdapter.remove(card);
                    pileAdapter.add(card);
                    pileView.scrollToPosition(pileAdapter.getItemCount() - 1);
                }
                break;
            case PILE:
                // take all cards from pile, put in hand
                ArrayList<PlayingCard> cards = pileAdapter.getCards();
                playerHandAdapter.addAll(cards);
                handView.scrollToPosition(playerHandAdapter.getItemCount() - 1);
                pileAdapter.clear();
            default:
                break;
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
