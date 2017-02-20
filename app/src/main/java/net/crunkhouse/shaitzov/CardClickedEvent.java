package net.crunkhouse.shaitzov;

class CardClickedEvent {
    private CardSource source;
    private PlayingCard card;

    public CardClickedEvent(CardSource source, PlayingCard card) {
        this.source = source;
        this.card = card;
    }

    public CardSource getSource() {
        return source;
    }

    public PlayingCard getCard() {
        return card;
    }
}
