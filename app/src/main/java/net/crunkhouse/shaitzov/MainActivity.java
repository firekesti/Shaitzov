package net.crunkhouse.shaitzov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int INITIAL_HAND_SIZE = 3;
    private static final int FACE_UP_AND_DOWN_CARD_AMOUNT = 3;

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
        PlayingCardAdapter faceDownAdapter = new PlayingCardAdapter(playerFaceDown, true);
        faceDownView.setAdapter(faceDownAdapter);
        faceDownView.addItemDecoration(new CardSpacingDecorator(faceDownView.getContext()));
        faceDownView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player face-up cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceUp.add(PlayingCardUtils.drawFrom(deck));
        }
        RecyclerView faceUpView = (RecyclerView) findViewById(R.id.player_faceup);
        PlayingCardAdapter faceUpAdapter = new PlayingCardAdapter(playerFaceUp);
        faceUpView.setAdapter(faceUpAdapter);
        faceUpView.addItemDecoration(new CardSpacingDecorator(faceUpView.getContext()));
        faceUpView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add player hand
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            playerHand.add(PlayingCardUtils.drawFrom(deck));
        }
        RecyclerView handView = (RecyclerView) findViewById(R.id.player_hand);
        PlayingCardAdapter playerHandAdapter = new PlayingCardAdapter(playerHand);
        handView.setAdapter(playerHandAdapter);
        handView.addItemDecoration(new CardOverlapDecorator(handView.getContext()));
        handView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add the deck
        RecyclerView deckView = (RecyclerView) findViewById(R.id.deck);
        PlayingCardAdapter deckAdapter = new PlayingCardAdapter(deck, true);
        deckView.setAdapter(deckAdapter);
        deckView.addItemDecoration(new DeckOverlapDecorator(deckView.getContext()));
        deckView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

    }
}
