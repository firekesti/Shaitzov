package net.crunkhouse.shaitzov.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.crunkhouse.shaitzov.cards.CardClickedListener;
import net.crunkhouse.shaitzov.cards.CardSource;
import net.crunkhouse.shaitzov.cards.GameRuleUtils;
import net.crunkhouse.shaitzov.cards.PlayingCard;

import java.util.ArrayList;
import java.util.Collections;

public class PlayingCardAdapter extends RecyclerView.Adapter<PlayingCardAdapter.ViewHolder> {
    private ArrayList<PlayingCard> cards;
    private boolean cardsFaceDown;
    private CardSource source;
    private CardClickedListener listener;
    private ArrayList<PlayingCard> activeCards = new ArrayList<>(4);

    public PlayingCardAdapter(ArrayList<PlayingCard> cards, CardSource source, CardClickedListener listener) {
        this.cards = cards;
        this.source = source;
        if (source == CardSource.FACE_DOWN || source == CardSource.DECK) {
            cardsFaceDown = true;
        }
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new PlayingCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.view.setSelected(activeCards.contains(cards.get(position)));
        holder.view.setCard(cards.get(position));
        holder.view.setFaceDown(cardsFaceDown);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    PlayingCard currentCard = cards.get(holder.getAdapterPosition());

                    // If this is the DECK or PILE or FACE-DOWN, proceed with single-select:
                    if (source == CardSource.DECK || source == CardSource.PILE || source == CardSource.FACE_DOWN) {
                        listener.onCardClicked(source, currentCard);
                        return;
                    }

                    // Check to see if the current card has any sibling cards (of the same value)
                    int countOfEqualValueCards = 0;
                    for (int i = 0; i < cards.size(); i++) {
                        if (cards.get(i).getValue() == currentCard.getValue()) {
                            countOfEqualValueCards++;
                        }
                    }
                    // If we only have the one card, proceed with single-select.
                    if (countOfEqualValueCards == 1) {
                        listener.onCardClicked(source, currentCard);
                        return;
                    }

                    // If we have no active cards, or if we do but the values match and it's not the same card:
                    if (activeCards.size() == 0 ||
                            (activeCards.size() > 0 && activeCards.get(0).getValue() == currentCard.getValue() &&
                                    !activeCards.contains(currentCard))) {
                        holder.view.setSelected(true);
                        activeCards.add(currentCard);
                    } else if (holder.view.isSelected()) {
                        // Otherwise, if we're already selected, unselect and remove from active cards
                        holder.view.setSelected(false);
                        activeCards.remove(currentCard);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void remove(PlayingCard card) {
        int pos = cards.indexOf(card);
        cards.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(PlayingCard card) {
        if (source == CardSource.HAND) {
            boolean added = false;
            // We want to keep the hand sorted by value.
            for (int i = 0; i < cards.size(); i++) {
                if (card.compareTo(cards.get(i)) <= 0) {
                    // Once we've iterated to a pos where the new card is equal or greater, insert it
                    cards.add(i, card);
                    notifyItemInserted(i);
                    added = true;
                    break;
                }
            }
            if (!added) {
                cards.add(card);
                notifyItemInserted(getItemCount() - 1);
            }
        } else {
            cards.add(card);
            notifyItemInserted(cards.indexOf(card));
        }
    }

    public void addAll(ArrayList<PlayingCard> cardsToAdd) {
        int oldsize = cards.size();
        cards.addAll(cardsToAdd);
        if (source == CardSource.HAND) {
            // We want to keep the hand sorted by value.
            Collections.sort(cards);
            notifyItemRangeChanged(0, cards.size());
        } else {
            notifyItemRangeInserted(oldsize, cards.size());
        }
    }

    public ArrayList<PlayingCard> getCards() {
        return cards;
    }

    public void clear() {
        notifyItemRangeRemoved(0, cards.size());
        cards.clear();
    }

    public void cardSwipedAtPosition(int adapterPosition) {
        PlayingCard swipedCard = cards.get(adapterPosition);
        if (activeCards.size() == 0) {
            // If there are no selected cards, the user just wants to swipe one to play it, so play it:
            if (!listener.onCardClicked(source, swipedCard)) {
                // If we didn't play it successfully, reset swipe offset
                notifyItemChanged(adapterPosition);
            }
        } else {
            // If the adapter-position item wasn't one of the active cards, don't play it
            if (!activeCards.contains(swipedCard)) {
                notifyItemChanged(adapterPosition);
            } else {
                boolean success = false;
                // For each selected card, try to play it - if one is successful, they all are
                for (int i = 0; i < activeCards.size(); i++) {
                    PlayingCard card = activeCards.get(i);
                    if (listener.onCardClicked(source, card)) {
                        success = true;
                    }
                }
                if (!success) {
                    // If it wasn't successful, reset swipe offset and selected-ness for all cards
                    notifyDataSetChanged();
                } else {
                    // If successful, check if the card should switch directions
                    if (GameRuleUtils.shouldDirectionSwitch(activeCards.get(0))) {
                        // and if so, how many active cards there were.
                        if (activeCards.size() == 2) {
                            // If an even number of direction-switches were played simultaneously, the direction
                            // would be the same as before, but it should actually be different.
                            // We don't have to worry about 4, though, because it burns, so only check 2 of a kind.
                            listener.switchDirection();
                        }
                    }
                    // TODO: also check for 8, shouldn't skip multiple people, but 8 is out of scope for now
                }
            }
            activeCards.clear();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PlayingCardView view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = (PlayingCardView) itemView;
        }
    }
}
