package com.zowdow.direct_api.utils;

import android.content.res.Resources;

import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.utils.constants.CardFormats;

public class ImageParams {
    private static final float INLINE_SCALE_FACTOR = 1.25f;

    private static final float EMBELLISHMENT_FRACTION_INLINE_HORIZONTAL = 0.218f;
    private static final float EMBELLISHMENT_FRACTION_STAMP_VERTICAL = 0.56f;
    private static final float EMBELLISHMENT_FRACTION_TICKET_HORIZONTAL = 0.361f;

    private static final int SCHEDULE_MARGIN_INLINE = ViewUtils.dpToPx(10);
    private static final int SCHEDULE_MARGIN_STAMP = ViewUtils.dpToPx(16);
    private static final int SCHEDULE_MARGIN_TICKET = ViewUtils.dpToPx(14);

    public String path;
    public int width;
    public int height;
    public int distanceLabelMargin;
    public int scheduleLabelMargin;

    private ImageParams(String path, int width, int height,  int distanceLabelMargin, int scheduleLabelMargin) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.distanceLabelMargin = distanceLabelMargin;
        this.scheduleLabelMargin = scheduleLabelMargin;
    }

    public static ImageParams create(final Card card) {
        return create(card, false);
    }

    public static ImageParams create(final Card card, final int density) {
        return create(card, density, false);
    }

    public static ImageParams create(final Card card, final boolean isTablet) {
        return create(card, Math.round(Resources.getSystem().getDisplayMetrics().density), isTablet);
    }

    public static ImageParams create(final Card card, final int density, boolean isTablet) {
        final String path;
        final int width;
        final int height;
        int distanceLabelMargin = 0;
        int scheduleLabelMarginTop = 0;

        if (density == 1) {
            path = card.getX1();
        } else if (density == 2) {
            path = card.getX2();
        } else if (density == 3 || isTablet) {
            path = card.getX3();
        } else {
            path = card.getX4();
        }

        switch (card.getCardFormat()) {
            case CardFormats.CARD_FORMAT_ANIMATED_GIF:
                width = card.getX1w();
                height = card.getX1h();
                break;
            case CardFormats.CARD_FORMAT_INLINE:
                width = (int) (ViewUtils.dpToPx(card.getX1w()) * INLINE_SCALE_FACTOR);
                height = (int) (ViewUtils.dpToPx(card.getX1h()) * INLINE_SCALE_FACTOR);
                scheduleLabelMarginTop = SCHEDULE_MARGIN_INLINE;
                if (card.getLatitude() != null && card.getLongitude() != null) {
                    distanceLabelMargin = (int) (width * EMBELLISHMENT_FRACTION_INLINE_HORIZONTAL);
                }
                break;
            case CardFormats.CARD_FORMAT_STAMP:
                width = ViewUtils.dpToPx(card.getX1w());
                height = ViewUtils.dpToPx(card.getX1h());
                scheduleLabelMarginTop = SCHEDULE_MARGIN_STAMP;
                if (card.getLatitude() != null && card.getLongitude() != null) {
                    distanceLabelMargin = (int) (height * EMBELLISHMENT_FRACTION_STAMP_VERTICAL);
                }
                break;
            case CardFormats.CARD_FORMAT_TICKET:
                width = ViewUtils.dpToPx(card.getX1w());
                height = ViewUtils.dpToPx(card.getX1h());
                scheduleLabelMarginTop = SCHEDULE_MARGIN_TICKET;
                if (card.getLatitude() != null && card.getLongitude() != null) {
                    distanceLabelMargin = (int) (width * EMBELLISHMENT_FRACTION_TICKET_HORIZONTAL);
                }
                break;
            default:
                width = ViewUtils.dpToPx(card.getX1w());
                height = ViewUtils.dpToPx(card.getX1h());
                break;
        }

        return new ImageParams(path, width, height, distanceLabelMargin, scheduleLabelMarginTop);
    }
}