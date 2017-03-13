package com.zowdow.direct_api.network.models.tracking;

import com.zowdow.direct_api.utils.tracker.TrackHelper;

import java.util.Timer;
import java.util.TimerTask;

class CardImpressionInfo {
    private Timer timer;
    private String cardId;
    private String impressionUrl;
    private boolean isTracked;
    private boolean cardShown;
    private TrackHelper trackHelper;

    CardImpressionInfo(String cardId, String impressionUrl) {
        this.timer = new Timer();
        this.isTracked = false;
        this.cardShown = false;
        this.cardId = cardId;
        this.impressionUrl = impressionUrl;
    }

    CardImpressionInfo(String cardId, String impressionUrl, TrackHelper trackHelper) {
        this(cardId, impressionUrl);
        this.trackHelper = trackHelper;
    }

    void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                trackHelper.trackImpression(impressionUrl);
                isTracked = true;
            }
        }, 1000, 100);
    }

    void stopTimer() {
        timer.purge();
        timer.cancel();
    }

    public String getCardId() {
        return cardId;
    }

    public boolean isTracked() {
        return isTracked;
    }

    public void setTracked(boolean tracked) {
        isTracked = tracked;
    }

    public boolean isCardShown() {
        return cardShown;
    }

    void cardShown() {
        this.cardShown = true;
    }

    void cardHidden() {
        this.cardShown = false;
    }
}
