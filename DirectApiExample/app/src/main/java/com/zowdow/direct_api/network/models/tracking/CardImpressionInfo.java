package com.zowdow.direct_api.network.models.tracking;

import android.os.CountDownTimer;
import android.util.Log;

import com.zowdow.direct_api.utils.tracker.TrackHelper;

class CardImpressionInfo {
    private CountDownTimer timer;
    private String cardId;
    private String impressionUrl;
    private boolean isTracked;
    private TrackHelper trackHelper;

    private CardImpressionInfo(String cardId, String impressionUrl) {
        this.isTracked = false;
        this.cardId = cardId;
        this.impressionUrl = impressionUrl;
    }

    CardImpressionInfo(String cardId, String impressionUrl, TrackHelper trackHelper) {
        this(cardId, impressionUrl);
        this.trackHelper = trackHelper;
        Log.d("CardInfo", "Created card info: " + cardId);
    }

    private void startTimer() {
        Log.d("CardImpr", "Starting timer for card: " + cardId);
        timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Log.d("CardImpr", "Firing timer event for card: " + cardId);
                trackHelper.trackImpression(impressionUrl);
                isTracked = true;
                timer = null;
            }
        };
        timer.start();
    }

    private void stopTimer() {
        Log.d("CardImpr", "Stopping timer for card: " + this);
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer = null;
    }

    void cardShown() {
        if (!isTracked && timer == null) {
            startTimer();
        }
    }

    void cardHidden() {
        if (!isTracked) {
            stopTimer();
        }
    }
}
