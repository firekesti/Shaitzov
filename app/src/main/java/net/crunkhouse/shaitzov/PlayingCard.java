package net.crunkhouse.shaitzov;

import android.support.annotation.NonNull;

/**
 * A model for a playing card.
 */

public class PlayingCard implements Comparable<PlayingCard> {

    public enum Suit {
        SPADE, HEART, CLUB, DIAMOND
    }

    private int value;
    private Suit suit;

    public PlayingCard(int value, Suit suit) {
        if (value < 2 || value > 14) {
            throw new IllegalArgumentException("Value must be between 2 and 14");
        }
        this.value = value;
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public String getValueName() {
        switch (value) {
            case 11:
                return "J";
            case 12:
                return "Q";
            case 13:
                return "K";
            case 14:
                return "A";
            default:
                return String.valueOf(value);
        }
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return value + " of " + suit;
    }

    /**
     * Returns 1 if otherCard is less than this card,
     * Returns 0 if they are the same value,
     * Returns -1 if otherCard is greater than this card
     * @param otherCard
     * @return
     */
    @Override
    public int compareTo(@NonNull PlayingCard otherCard) {
        if (otherCard.getValue() < getValue()) {
            return 1;
        } else if (otherCard.getValue() == getValue()) {
            return 0;
        } else {
            return -1;
        }
    }
}
