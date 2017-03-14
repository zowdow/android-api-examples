package com.zowdow.direct_api.tracking;

import android.util.Log;

import com.zowdow.direct_api.network.models.unified.suggestions.Card;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class that handles tracking events for each card inside the suggestions list.
 */
@Singleton
public class CardImpressionTracker {
    private Map<String, CardImpressionInfo> cardImpressionInfoMap;
    private TrackHelper trackHelper;

    @Inject
    public CardImpressionTracker(TrackHelper trackHelper) {
        this.cardImpressionInfoMap = new HashMap<>();
        this.trackHelper = trackHelper;
    }

    /**
     * Updates cards impression objects map with the newly requested ones.
     * The card impression objects of the new bundle which are similar to the ones from the previous
     * one are retained and their timers are not cancelled.
     *
     * The impression objects associated to the card ids from the previous bundle which are absent
     * in the current one are removed and all active timers for them get cancelled as they are not
     * shown on screen.
     *
     * @param newCardsList represents the full list of cards from all suggestions for current keyword.
     */
    public void setNewCardImpressionsData(List<Card> newCardsList) {
        // Creating the map full of all cards in this bundle whether some of them (with the same ids) are already shown or not.
        Map<String, CardImpressionInfo> allNewCardsMap = new HashMap<>();
        for (Card card : newCardsList) {
            String cardId = card.getId();
            CardImpressionInfo cardImpressionInfo = new CardImpressionInfo(cardId, card.getImpressionUrl(), trackHelper);
            allNewCardsMap.put(cardId, cardImpressionInfo);
        }

        // Defining the set of card ids which were displayed in a previous suggestions/cards bundle and should be kept alive.
        Set<String> retainedCardsSet = new HashSet<>(cardImpressionInfoMap.keySet());
        retainedCardsSet.retainAll(allNewCardsMap.keySet());

        // Removing all card impression info instances related to the cards that haven't survived and are not shown.
        for (Iterator<Map.Entry<String, CardImpressionInfo>> it = cardImpressionInfoMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, CardImpressionInfo> currentEntry = it.next();
            if (!retainedCardsSet.contains(currentEntry.getKey())) {
                it.remove();
            }
        }

        // Adding all fresh cards that haven't been shown in a previous suggestions and cards bundle.
        for (Map.Entry<String, CardImpressionInfo> newCardEntry : allNewCardsMap.entrySet()) {
            if (!cardImpressionInfoMap.containsKey(newCardEntry.getKey())) {
                cardImpressionInfoMap.put(newCardEntry.getKey(), newCardEntry.getValue());
            }
        }
    }

    /**
     * Marks the card with a given identifier as shown. Use this method only in the case
     * when the visible area of the view which represents this card takes more than a half
     * of its' full area.
     *
     * It makes sense to use this method as soon as onScrolled-event (you
     * can track such events inside ScrollListeners for LayoutManager-classes)
     * is invoked as it's quite easy to measure the currently visible list item area immediately.
     *
     * @param cardId represents the unique id of the card which state is nbeing handled here.
     */
    public void setCardShown(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        Log.d("Tracker", "Setting card shown: " + cardImpressionInfo);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardShown();
        }
    }

    /**
     * Marks the card with a given identifier as hidden. It is important to call this method
     * as soon as the card becomes less than 50% visible on screen.
     *
     * It makes sense to use this method as soon as onScrolled-event (you
     * can track such events inside ScrollListeners for LayoutManager-classes)
     * is invoked as it's quite easy to measure the currently visible list item area immediately.
     *
     * @param cardId represents the unique id of the card which state is being handled here.
     */
    public void setCardHidden(String cardId) {
        CardImpressionInfo cardImpressionInfo = cardImpressionInfoMap.get(cardId);
        Log.d("Tracker", "Setting card hidden: " + cardImpressionInfo);
        if (cardImpressionInfo != null) {
            cardImpressionInfo.cardHidden();
        }
    }
}
