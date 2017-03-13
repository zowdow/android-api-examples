package com.zowdow.direct_api.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.models.tracking.CardImpressionTracker;
import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.utils.tracker.TrackHelper;

import javax.inject.Inject;

/**
 * Customized ImageView class which provides interaction with
 * Zowdow tracking mechanism out-of-the-box.
 */
public class CardImageView extends AppCompatImageView {
    private Card currentCard;

    @Inject TrackHelper trackHelper;
    @Inject CardImpressionTracker impressionTracker;

    {
        ZowdowDirectApplication.getNetworkComponent().inject(this);
    }

    public CardImageView(Context context) {
        super(context);
    }

    public CardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            post(() -> {
                if (currentCard != null && isShown() && !currentCard.hasBeenTracked()) {
                    track();
                }
            });
        }
        super.setImageDrawable(drawable);
    }

    public void setTrackInfo(Card card) {
        this.currentCard = card;
    }

    public void sendTrackInfo() {
        if (currentCard != null && !currentCard.hasBeenTracked()) {
            track();
        }
    }

    private void track() {
        trackHelper.trackImpression(currentCard.getImpressionUrl());
    }
}
