package net.crunkhouse.shaitzov;

interface CardClickedListener {
    /**
     * Return true if successfully played, false if not
     */
    boolean onCardClicked(CardSource source, PlayingCard card);
}
