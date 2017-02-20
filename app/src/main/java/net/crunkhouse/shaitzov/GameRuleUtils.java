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
        }

        // Otherwise, directly compare the values of the cards.
        if (direction == PileDirection.UP) {
            return value >= topPileCard.getValue();
        } else {
            return value <= topPileCard.getValue();
        }
    }
}
