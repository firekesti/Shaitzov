package net.crunkhouse.shaitzov;

import java.util.ArrayList;

/**
 * A util class to encompass some game logic.
 */

final class GameRuleUtils {
    private GameRuleUtils() {
    }

    public static boolean canPlayCardOnPile(PlayingCard card, ArrayList<PlayingCard> pile, PileDirection direction) {
        // Game Rules:
        // 2, 10, J can be played on ANYTHING.
        int value = card.getValue();
        if (value == 2 || value == 10 || value == 11) {
            return true;
        }
        // If the pile is empty, ANYTHING can be played on it.
        if (pile.size() == 0) {
            return true;
        }
        PlayingCard topPileCard = pile.get(pile.size() - 1);
        // Lower cards can be played on a J if the J is on top of a lower card.
        if (topPileCard.getValue() == 11) {
            // Go through the top cards until we find one that's not a J
            for (int i = pile.size() - 1; i > 0; i--) {
                if (pile.get(i).getValue() != 11) {
                    topPileCard = pile.get(i);
                    break;
                }
            }
            if (topPileCard.getValue() == 11) {
                // If it's still a jack, that means it's jacks all the way down. Play anything.
                return true;
            }
        }

        // Otherwise, directly compare the values of the cards.
        if (direction == PileDirection.UP) {
            return value >= topPileCard.getValue();
        } else {
            return value <= topPileCard.getValue();
        }
    }

    public static boolean shouldDirectionSwitch(PlayingCard card) {
        return card.getValue() == 7;
    }

    public static boolean shouldSkipPlayer(PlayingCard card) {
        return card.getValue() == 8;
    }

    public static boolean shouldBurnPile(PlayingCard card, ArrayList<PlayingCard> cards) {
        // 10 always burns
        if (card.getValue() == 10) {
            return true;
        }
        // Then, check if we have 4 in a row on the pile (the pile includes the active card).
        // TODO: WRITE A TEST FOR THIS (and others!!!)
        return cards.size() >= 4
                && cards.get(cards.size() - 2).getValue() == card.getValue()
                && cards.get(cards.size() - 3).getValue() == card.getValue()
                && cards.get(cards.size() - 4).getValue() == card.getValue();
    }

    public static boolean shouldDirectionReset(PlayingCard card) {
        return card.getValue() == 2;
    }
}
