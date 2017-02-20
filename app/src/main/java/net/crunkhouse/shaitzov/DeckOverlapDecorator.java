package net.crunkhouse.shaitzov;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * A decoration that creates a tight "deck" view
 */

class DeckOverlapDecorator extends RecyclerView.ItemDecoration {
    private int overlapAmount;

    public DeckOverlapDecorator(Context context) {
        overlapAmount = DensityUtils.dpToPixel(context, -58);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        // We don't want to apply the offset to the last item
        if (itemPosition == parent.getAdapter().getItemCount() - 1) {
            return;
        }
        outRect.left = overlapAmount;
    }
}
