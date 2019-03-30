package net.crunkhouse.shaitzov.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.playing_card_view.view.*

import net.crunkhouse.shaitzov.R
import net.crunkhouse.shaitzov.cards.PlayingCard
import net.crunkhouse.shaitzov.dp

/**
 * A view that draws a PlayingCard model to an image.
 */

class PlayingCardView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(R.drawable.card_outline)
        val width = 60.dp(context)
        val height = 84.dp(context)
        minimumHeight = height
        minimumWidth = width
        inflate(context, R.layout.playing_card_view, this)
    }

    fun setFaceDown(faceDown: Boolean) {
        if (faceDown) {
            value.visibility = View.GONE
            suit.visibility = View.GONE
            back_face.visibility = View.VISIBLE
        }
    }

    fun setCard(card: PlayingCard) {
        value.text = card.valueName
        val suitImage: Drawable = when (card.suit) {
            PlayingCard.Suit.CLUB -> resources.getDrawable(R.drawable.suit_club)
            PlayingCard.Suit.DIAMOND -> resources.getDrawable(R.drawable.suit_diamond)
            PlayingCard.Suit.SPADE -> resources.getDrawable(R.drawable.suit_spade)
            PlayingCard.Suit.HEART -> resources.getDrawable(R.drawable.suit_heart)
            else -> resources.getDrawable(R.drawable.suit_heart)
        }
        suit.setImageDrawable(suitImage)
        suit.contentDescription = card.suit.toString()
    }
}
