package net.crunkhouse.shaitzov.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

import net.crunkhouse.shaitzov.dp

/**
 * An ItemDecorator that adds some space between cards.
 */

class CardSpacingDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val overlapAmount: Int = 12.dp(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.right = overlapAmount
    }
}
