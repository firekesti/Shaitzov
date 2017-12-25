package net.crunkhouse.shaitzov.cards;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * A model for a playing card.
 */
@IgnoreExtraProperties
public class PlayingCard implements Comparable<PlayingCard> {

    public enum Suit {
        SPADE, HEART, CLUB, DIAMOND
    }

    private int value;
    private Suit suit;

    public PlayingCard() {
        // Default constructor required for calls to DataSnapshot.getValue(PlayingCard.class)
    }

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

    @Exclude
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
        return getValueName() + " of " + suit.toString().toLowerCase() + "s";
    }

    /**
     * Returns 1 if otherCard is less than this card,
     * Returns 0 if they are the same value,
     * Returns -1 if otherCard is greater than this card
     * @param otherCard the other card
     * @return the comparator
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
