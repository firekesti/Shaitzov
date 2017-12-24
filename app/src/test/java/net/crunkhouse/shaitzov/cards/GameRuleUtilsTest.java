package net.crunkhouse.shaitzov.cards;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GameRuleUtilsTest {
    @Test
    public void canPlayCardOnPile() throws Exception {
        // Game Rules:
        // 2, 10, J can be played on ANYTHING.
        PlayingCard two = new PlayingCard(2, PlayingCard.Suit.HEART);
        PlayingCard ten = new PlayingCard(10, PlayingCard.Suit.HEART);
        PlayingCard jack = new PlayingCard(11, PlayingCard.Suit.HEART);

        assertTrue(GameRuleUtils.canPlayCardOnPile(two, null, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(two, null, PileDirection.DOWN));

        assertTrue(GameRuleUtils.canPlayCardOnPile(ten, null, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(ten, null, PileDirection.DOWN));

        assertTrue(GameRuleUtils.canPlayCardOnPile(jack, null, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(jack, null, PileDirection.DOWN));

        // If the pile is empty, ANYTHING can be played on it.
        final ArrayList<PlayingCard> emptyPile = new ArrayList<>();
        PlayingCard card;
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
                    suit = PlayingCard.Suit.HEART;
                    break;
                default:
                case 3:
                    suit = PlayingCard.Suit.SPADE;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                card = new PlayingCard(j, suit);
                assertTrue(GameRuleUtils.canPlayCardOnPile(card, emptyPile, PileDirection.UP));
                assertTrue(GameRuleUtils.canPlayCardOnPile(card, emptyPile, PileDirection.DOWN));
            }
        }

        // Lower cards can be played on a J if the J is on top of a lower card.
        ArrayList<PlayingCard> pileWithJackOnTop = new ArrayList<>();
        PlayingCard three = new PlayingCard(3, PlayingCard.Suit.HEART);
        PlayingCard four = new PlayingCard(4, PlayingCard.Suit.HEART);
        PlayingCard five = new PlayingCard(5, PlayingCard.Suit.HEART);
        pileWithJackOnTop.add(three);
        pileWithJackOnTop.add(jack);
        assertTrue(GameRuleUtils.canPlayCardOnPile(three, pileWithJackOnTop, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(four, pileWithJackOnTop, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(three, pileWithJackOnTop, PileDirection.DOWN));
        assertTrue(GameRuleUtils.canPlayCardOnPile(four, pileWithJackOnTop, PileDirection.DOWN));

        ArrayList<PlayingCard> pileWithOnlyFour = new ArrayList<>();
        pileWithOnlyFour.add(four);
        assertFalse(GameRuleUtils.canPlayCardOnPile(three, pileWithOnlyFour, PileDirection.UP));
        assertTrue(GameRuleUtils.canPlayCardOnPile(three, pileWithOnlyFour, PileDirection.DOWN));
        assertTrue(GameRuleUtils.canPlayCardOnPile(five, pileWithOnlyFour, PileDirection.UP));
        assertFalse(GameRuleUtils.canPlayCardOnPile(five, pileWithOnlyFour, PileDirection.DOWN));
    }

    @Test
    public void shouldDirectionSwitch() throws Exception {
        PlayingCard card;
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
                    suit = PlayingCard.Suit.HEART;
                    break;
                default:
                case 3:
                    suit = PlayingCard.Suit.SPADE;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                card = new PlayingCard(j, suit);
                if (j == 7) {
                    assertTrue(GameRuleUtils.shouldDirectionSwitch(card));
                } else {
                    assertFalse(GameRuleUtils.shouldDirectionSwitch(card));
                }
            }
        }
    }

    @Test
    public void shouldSkipPlayer() throws Exception {
        PlayingCard card;
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
                    suit = PlayingCard.Suit.HEART;
                    break;
                default:
                case 3:
                    suit = PlayingCard.Suit.SPADE;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                card = new PlayingCard(j, suit);
                if (j == 8) {
                    assertTrue(GameRuleUtils.shouldSkipPlayer(card));
                } else {
                    assertFalse(GameRuleUtils.shouldSkipPlayer(card));
                }
            }
        }
    }

    @Test
    public void shouldBurnPile() throws Exception {
        PlayingCard card;
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
                    suit = PlayingCard.Suit.HEART;
                    break;
                default:
                case 3:
                    suit = PlayingCard.Suit.SPADE;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                card = new PlayingCard(j, suit);
                ArrayList<PlayingCard> pile = new ArrayList<>();
                pile.add(new PlayingCard(card.getValue(), PlayingCard.Suit.HEART));
                pile.add(new PlayingCard(card.getValue(), PlayingCard.Suit.HEART));
                pile.add(new PlayingCard(card.getValue(), PlayingCard.Suit.HEART));
                if (j == 10) {
                    assertTrue(GameRuleUtils.shouldBurnPile(card, null));
                } else {
                    assertFalse(GameRuleUtils.shouldBurnPile(card, pile));
                    pile.add(new PlayingCard(card.getValue(), PlayingCard.Suit.HEART));
                    assertTrue(GameRuleUtils.shouldBurnPile(card, pile));
                }
            }
        }
    }

    @Test
    public void shouldDirectionReset() throws Exception {
        PlayingCard card;
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
                    suit = PlayingCard.Suit.HEART;
                    break;
                default:
                case 3:
                    suit = PlayingCard.Suit.SPADE;
                    break;
            }
            for (int j = 2; j <= 14; j++) {
                card = new PlayingCard(j, suit);
                if (j == 2) {
                    assertTrue(GameRuleUtils.shouldDirectionReset(card));
                } else {
                    assertFalse(GameRuleUtils.shouldDirectionReset(card));
                }
            }
        }
    }
}