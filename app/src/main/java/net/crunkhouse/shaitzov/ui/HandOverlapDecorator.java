package net.crunkhouse.shaitzov.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.crunkhouse.shaitzov.DensityUtils;

/**
 * An ItemDecorator to overlap cards.
 */

public class HandOverlapDecorator extends RecyclerView.ItemDecoration {

    private int overlapAmount;

    public HandOverlapDecorator(Context context) {
        overlapAmount = DensityUtils.dpToPixel(context, -16);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        // We don't want to apply the offset to the first item
        if (itemPosition == RecyclerView.NO_POSITION || itemPosition == 0) {
            return;
        }
        outRect.left = overlapAmount;
    }
}
