package com.zowdow.direct_api.network.models.tracking;

import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.utils.tracker.TrackHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardImpressionTracker {
    private Map<String, CardImpressionInfo> cardImpressionInfoMap;
    private TrackHelper trackHelper;

    public CardImpressionTracker(TrackHelper trackHelper) {
        this.cardImpressionInfoMap = new HashMap<>();
        this.trackHelper = trackHelper;
    }

    public void setNewCardImpressionsData(List<Card> newCardsList) {
        Map<String, CardImpressionInfo> tempMap = new HashMap<>();
        for (Card card : newCardsList) {
            String cardId = card.getId();
            CardImpressionInfo cardImpressionInfo = new CardImpressionInfo(cardId, card.getImpressionUrl());
            tempMap.put(cardId, cardImpressionInfo);
        }
        Map<String, CardImpressionInfo> resultMap = new HashMap<>(cardImpressionInfoMap);
        resultMap.keySet().retainAll(tempMap.keySet());
        cardImpressionInfoMap = resultMap;
        cardImpressionInfoMap.putAll(tempMap);
    }

    public void setCardShown(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardShown();
        }
    }

    public void setCardHidden(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardHidden();
        }
    }
}
