package net.crunkhouse.shaitzov;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int INITIAL_HAND_SIZE = 3;
    private static final int FACE_UP_AND_DOWN_CARD_AMOUNT = 3;

    private ArrayList<PlayingCard> deck;
    private ArrayList<PlayingCard> playerHand;
    private ArrayList<PlayingCard> playerFaceDown;
    private ArrayList<PlayingCard> playerFaceUp;
    private ArrayList<PlayingCard> pile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate cards
        playerHand = new ArrayList<>(INITIAL_HAND_SIZE);
        playerFaceDown = new ArrayList<>(FACE_UP_AND_DOWN_CARD_AMOUNT);
        playerFaceUp = new ArrayList<>(FACE_UP_AND_DOWN_CARD_AMOUNT);
        pile = new ArrayList<>(0);

        // Populate deck
        deck = PlayingCardUtils.makeDeck();

        // Add player face-down cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceDown.add(PlayingCardUtils.drawFrom(deck));
        }
        // Add player face-up cards
        for (int i = 0; i < FACE_UP_AND_DOWN_CARD_AMOUNT; i++) {
            playerFaceUp.add(PlayingCardUtils.drawFrom(deck));
        }

        // Add player hand
        for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
            playerHand.add(PlayingCardUtils.drawFrom(deck));
        }

        RecyclerView handView = (RecyclerView) findViewById(R.id.player_hand);
        handView.setAdapter(new PlayingCardAdapter(playerHand));
        handView.addItemDecoration(new CardOverlapDecorator(handView.getContext()));
        handView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView faceUpView = (RecyclerView) findViewById(R.id.player_faceup);
        faceUpView.setAdapter(new PlayingCardAdapter(playerFaceUp));
        faceUpView.addItemDecoration(new CardSpacingDecorator(faceUpView.getContext()));
        faceUpView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}
