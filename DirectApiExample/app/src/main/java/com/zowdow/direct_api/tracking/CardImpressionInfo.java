package com.zowdow.direct_api.tracking;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Class represents the entity associated to each specific card in a current suggestions response
 * for a given keyword, which stores card's current impression tracking state.
 *
 * Each impression-info instance for card contains timer, which starts as soon as the card gets
 * at least 50%-visible on the screen for the first time, if it hasn't been shown in a current or
 * previous suggestions/cards bundle, and fires the impression tracking event if the card with a
 * given id remained min. 50%-visible on the screen for at least 1 second.
 *
 * Actions applied to the visibility-timer are handled with isTracked flag and by
 * startTimer() / stopTimer() methods.
 */
class CardImpressionInfo {
    private static final long MIN_TRACKING_LIMIT_MILLIS = 1000;
    private static final String TAG = CardImpressionInfo.class.getSimpleName();

    private CountDownTimer timer;
    private String cardId;
    private String impressionUrl;
    private boolean isTracked;
    private TrackingRequestManager trackManager;

    private CardImpressionInfo(String cardId, String impressionUrl) {
        this.isTracked = false;
        this.cardId = cardId;
        this.impressionUrl = impressionUrl;
    }

    CardImpressionInfo(String cardId, String impressionUrl, TrackingRequestManager trackManager) {
        this(cardId, impressionUrl);
        this.trackManager = trackManager;
    }

    /**
     * Starts visibility-timer associated with a card with a given cardId.
     * If it expires, the card impression event will be fired followed by
     * this timer instance removal (at some point it will be garbage collected)
     * as it becomes redundant.
     */
    private void startTimer() {
        timer = new CountDownTimer(MIN_TRACKING_LIMIT_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "Firing timer event for card: " + cardId);
                trackManager.trackCardImpression(impressionUrl);
                isTracked = true;
                timer = null;
            }
        };
        timer.start();
    }

    /**
     * Stops the timer tied to the current cardId and releases it.
     */
    private void stopTimer() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer = null;
    }

    /**
     * This method should be called as soon as >= 50% of the card area becomes visible on screen.
     * The impression-tracking timer will be started only if the card associated with a given cardId
     * wasn't tracked earlier and the timer tied to it is not active at the moment.
     */
    void cardShown() {
        if (!isTracked && timer == null) {
            startTimer();
        }
    }

    /**
     * This method should be called as soon as the visible card's area takes less than 50%
     * of its' full area or as it simply gets invisible.
     */
    void cardHidden() {
        if (!isTracked) {
            stopTimer();
        }
    }
}
