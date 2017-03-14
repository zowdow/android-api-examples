package com.zowdow.direct_api.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zowdow.direct_api.R;
import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.models.tracking.CardImpressionTracker;
import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.network.models.unified.suggestions.Suggestion;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter for suggestions list.
 */
public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionViewHolder> {
    private Context context;
    private RecyclerView parentView;
    private List<Suggestion> suggestions;
    private OnCardClickListener cardClickListener;
    @Inject CardImpressionTracker impressionTracker;

    private SuggestionsAdapter(Context context, List<Suggestion> suggestions) {
        this.context = context;
        this.suggestions = new ArrayList<>();
        this.suggestions.addAll(suggestions);
        ZowdowDirectApplication.getNetworkComponent().inject(this);
    }

    public SuggestionsAdapter(Context context, List<Suggestion> suggestions, OnCardClickListener cardClickListener) {
        this(context, suggestions);
        this.cardClickListener = cardClickListener;
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        parentView = (RecyclerView) parent;
        return new SuggestionViewHolder(itemView, cardClickListener);
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        Suggestion currentSuggestion = suggestions.get(position);
        holder.setupCarousel(currentSuggestion);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    private void trackCardsStateChange() {
        List<Card> allCards = new ArrayList<>();
        for (Suggestion suggestion : suggestions) {
            allCards.addAll(suggestion.getCards());
        }
        impressionTracker.setNewCardImpressionsData(allCards);
        updateSuggestionsTrackingState();
    }

    private void updateSuggestionsTrackingState() {
        for (int i = 0; i < getItemCount(); i++) {
            SuggestionViewHolder holder = (SuggestionViewHolder) parentView.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                holder.performCardsTracking();
            }
        }
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions.clear();
        this.suggestions.addAll(suggestions);
        notifyDataSetChanged();
        trackCardsStateChange();
    }
}
