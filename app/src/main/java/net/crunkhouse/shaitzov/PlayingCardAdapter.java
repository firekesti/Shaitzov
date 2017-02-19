package net.crunkhouse.shaitzov;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

class PlayingCardAdapter extends RecyclerView.Adapter<PlayingCardAdapter.ViewHolder> {
    private ArrayList<PlayingCard> cards;
    private boolean areCardsFaceDown;

    public PlayingCardAdapter(ArrayList<PlayingCard> cards) {
        this(cards, false);
    }

    public PlayingCardAdapter(ArrayList<PlayingCard> cards, boolean faceDown) {
        this.cards = cards;
        this.areCardsFaceDown = faceDown;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new PlayingCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setCard(cards.get(position));
        holder.view.setFaceDown(areCardsFaceDown);
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
