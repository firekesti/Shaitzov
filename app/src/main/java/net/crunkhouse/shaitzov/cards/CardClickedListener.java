package net.crunkhouse.shaitzov.cards;

public interface CardClickedListener {
    /**
     * Return true if successfully played, false if not
     */
    boolean onCardClicked(CardSource source, PlayingCard card);

    void switchDirection();
}
