package com.zowdow.direct_api.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.tracking.CardImpressionTracker;
import com.zowdow.direct_api.tracking.TrackHelper;

import javax.inject.Inject;

/**
 * Customized ImageView class which provides interaction with
 * Zowdow tracking mechanism out-of-the-box.
 */
public class CardImageView extends AppCompatImageView {
    private Card currentCard;

    @Inject CardImpressionTracker impressionTracker;

    {
        ZowdowDirectApplication.getTrackingComponent().inject(this);
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

    public void setTrackInfo(Card card) {
        this.currentCard = card;
    }

    public void setCardMostlyVisible() {
        if (currentCard != null) {
            impressionTracker.setCardShown(currentCard.getId());
        }
    }

    public void setCardHidden() {
        if (currentCard != null) {
            impressionTracker.setCardHidden(currentCard.getId());
        }
    }
}
