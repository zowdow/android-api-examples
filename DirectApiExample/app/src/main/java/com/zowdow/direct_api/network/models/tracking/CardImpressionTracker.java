package com.zowdow.direct_api.network.models.tracking;

import android.util.Log;

import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.utils.tracker.TrackHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CardImpressionTracker {
    private Map<String, CardImpressionInfo> cardImpressionInfoMap;
    private TrackHelper trackHelper;

    @Inject
    public CardImpressionTracker(TrackHelper trackHelper) {
        this.cardImpressionInfoMap = new HashMap<>();
        this.trackHelper = trackHelper;
    }

    public void setNewCardImpressionsData(List<Card> newCardsList) {
        Map<String, CardImpressionInfo> tempMap = new HashMap<>();
        for (Card card : newCardsList) {
            String cardId = card.getId();
            CardImpressionInfo cardImpressionInfo = new CardImpressionInfo(cardId, card.getImpressionUrl(), trackHelper);
            tempMap.put(cardId, cardImpressionInfo);
        }
        Map<String, CardImpressionInfo> resultMap = new HashMap<>(cardImpressionInfoMap);
        resultMap.keySet().retainAll(tempMap.keySet());
        for (String cardId : cardImpressionInfoMap.keySet()) {
            if (!resultMap.containsKey(cardId)) {
                cardImpressionInfoMap.remove(cardId);
            }
        }
        for (Map.Entry<String, CardImpressionInfo> impressionEntry : cardImpressionInfoMap.entrySet()) {
            if (!tempMap.containsKey(impressionEntry.getKey())) {
                cardImpressionInfoMap.put(impressionEntry.getKey(), impressionEntry.getValue());
            }
        }
    }

    public void setCardShown(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        Log.d("Tracker", "Setting card shown for: " + cardImpressionInfo);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardShown();
        }
    }

    public void setCardHidden(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        Log.d("Tracker", "Setting card shown for: " + cardImpressionInfo);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardHidden();
        }
    }
}
