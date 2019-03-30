package net.crunkhouse.shaitzov.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import net.crunkhouse.shaitzov.dp

/**
 * A decoration that creates a tight "deck" view
 */

class DeckOverlapDecorator(context: Context) : RecyclerView.ItemDecoration() {
    private val overlapAmount: Int = (-58).dp(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        // We don't want to apply the offset to the last item
        if (itemPosition == parent.adapter!!.itemCount - 1) {
            return
        }
        outRect.left = overlapAmount
    }
}
