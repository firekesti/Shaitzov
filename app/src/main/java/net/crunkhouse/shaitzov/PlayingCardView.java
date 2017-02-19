package net.crunkhouse.shaitzov;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A view that draws a PlayingCard model to an image.
 */

public class PlayingCardView extends LinearLayout {

    private TextView valueView;
    private ImageView suitView;
    private View backFaceView;

    public PlayingCardView(Context context) {
        super(context);
        init(context);
    }

    public PlayingCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.card_background);
        int width = DensityUtils.dpToPixel(context, 92);
        int height = DensityUtils.dpToPixel(context, 128);
        setMinimumHeight(height);
        setMinimumWidth(width);
        View rootView = inflate(context, R.layout.playing_card_view, this);
        valueView = (TextView) rootView.findViewById(R.id.value);
        suitView = (ImageView) rootView.findViewById(R.id.suit);
        backFaceView = rootView.findViewById(R.id.back_face);
    }

    public void setFaceDown(boolean faceDown) {
        if (faceDown) {
            valueView.setVisibility(View.GONE);
            suitView.setVisibility(View.GONE);
            backFaceView.setVisibility(VISIBLE);
        }
    }

    public void setCard(PlayingCard card) {
        valueView.setText(card.getValueName());
        Drawable suitImage;
        switch (card.getSuit()) {
            case CLUB:
                suitImage = getResources().getDrawable(R.drawable.suit_club);
                break;
            case DIAMOND:
                suitImage = getResources().getDrawable(R.drawable.suit_diamond);
                break;
            case SPADE:
                suitImage = getResources().getDrawable(R.drawable.suit_spade);
                break;
            default:
            case HEART:
                suitImage = getResources().getDrawable(R.drawable.suit_heart);
                break;
        }
        suitView.setImageDrawable(suitImage);
        suitView.setContentDescription(card.getSuit().toString());
    }
}
