package net.crunkhouse.shaitzov;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Utilities for card-related functions.
 */

final class PlayingCardUtils {
    private static final int DECK_SIZE = 52;

    private PlayingCardUtils() {
    }

    static ArrayList<PlayingCard> makeDeck() {
        ArrayList<PlayingCard> deck = new ArrayList<>(DECK_SIZE);

        PlayingCard.Suit suit;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    suit = PlayingCard.Suit.CLUB;
                    break;
                case 1:
                    suit = PlayingCard.Suit.DIAMOND;
                    break;
                case 2:
                    suit = PlayingCard.Suit.SPADE;
                    break;
                case 3:
                default:
                    suit = PlayingCard.Suit.HEART;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                deck.add(new PlayingCard(j, suit));
            }
            // TODO: evil extra cards
        }

        Collections.shuffle(deck);

        return deck;
    }

    static PlayingCard drawFrom(ArrayList<PlayingCard> deck) {
        return deck.remove(0);
    }
}
