package net.crunkhouse.shaitzov;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

class PlayingCardAdapter extends RecyclerView.Adapter<PlayingCardAdapter.ViewHolder> {
    private ArrayList<PlayingCard> cards;

    public PlayingCardAdapter(ArrayList<PlayingCard> cards) {
        this.cards = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new PlayingCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setCard(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PlayingCardView view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = (PlayingCardView) itemView;
        }
    }
}
