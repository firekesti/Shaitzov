package net.crunkhouse.shaitzov.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import net.crunkhouse.shaitzov.dp

/**
 * An ItemDecorator to overlap cards.
 */

class PileOverlapDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val overlapAmount: Int = (-44).dp(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        // We don't want to apply the offset to the first item
        if (itemPosition == RecyclerView.NO_POSITION || itemPosition == 0) {
            return
        }
        outRect.left = overlapAmount
    }
}
