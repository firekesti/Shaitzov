package net.crunkhouse.shaitzov.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.crunkhouse.shaitzov.DensityUtils;

/**
 * An ItemDecorator that adds some space between cards.
 */

public class CardSpacingDecorator extends RecyclerView.ItemDecoration {

    private int overlapAmount;

    public CardSpacingDecorator(Context context) {
        overlapAmount = DensityUtils.dpToPixel(context, 12);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = overlapAmount;
    }
}
