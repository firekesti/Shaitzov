package net.crunkhouse.shaitzov;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

class PlayingCardAdapter extends RecyclerView.Adapter<PlayingCardAdapter.ViewHolder> {
    private ArrayList<PlayingCard> cards;
    private boolean cardsFaceDown;
    private CardSource source;

    public PlayingCardAdapter(ArrayList<PlayingCard> cards, CardSource source) {
        this.cards = cards;
        this.source = source;
        if (source == CardSource.FACE_DOWN || source == CardSource.DECK) {
            cardsFaceDown = true;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new PlayingCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.view.setCard(cards.get(position));
        holder.view.setFaceDown(cardsFaceDown);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    EventBus.getDefault().post(new CardClickedEvent(source, cards.get(holder.getAdapterPosition())));
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
        cards.add(card);
        notifyItemInserted(cards.indexOf(card));
    }

    public void addAll(ArrayList<PlayingCard> cardsToAdd) {
        int oldsize = cards.size();
        cards.addAll(cardsToAdd);
        notifyItemRangeInserted(oldsize, cards.size());
    }

    public ArrayList<PlayingCard> getCards() {
        return cards;
    }

    public void clear() {
        notifyItemRangeRemoved(0, cards.size());
        cards.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PlayingCardView view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = (PlayingCardView) itemView;
        }
    }
}
