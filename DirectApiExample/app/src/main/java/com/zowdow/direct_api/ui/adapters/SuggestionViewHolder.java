package com.zowdow.direct_api.ui.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zowdow.direct_api.R;
import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.network.models.unified.suggestions.Suggestion;
import com.zowdow.direct_api.ui.views.DividerItemDecoration;
import com.zowdow.direct_api.ui.views.CardImageView;
import com.zowdow.direct_api.utils.ViewUtils;
import com.zowdow.direct_api.utils.ImageParams;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Vieholder for each suggestion item. Most suggestions contain cards,
 * so RecyclerView for cards is also initialized here.
 */
public class SuggestionViewHolder extends RecyclerView.ViewHolder {
    private static final float SCALE_FACTOR = 1.3f;
    private static final float MIN_TRACKING_AREA = 0.5f;
    private static final int ITEMS_SPACING = 8;
    private static final int SUGGESTION_HEIGHT = 36;

    private Context context;
    private LinearLayoutManager layoutManager;
    private CardsAdapter cardsAdapter;
    private Suggestion currentSuggestion;
    private DividerItemDecoration dividerItemDecoration;
    private OnCardClickListener cardClickListener;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.suggestion_text_view)
    TextView suggestionTextView;
    @BindView(R.id.cards_list_view)
    RecyclerView cardsListView;

    SuggestionViewHolder(@NonNull View itemView, OnCardClickListener clickListener) {
        super(itemView);
        context = itemView.getContext();
        dividerItemDecoration = new DividerItemDecoration();
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        cardClickListener = clickListener;
        ButterKnife.bind(this, itemView);
        ZowdowDirectApplication.getNetworkComponent().inject(this);
    }

    void setupCarousel(Suggestion currentSuggestion) {
        this.currentSuggestion = currentSuggestion;
        setupSuggestionView();
        setupCarousel();
    }

    private void setupSuggestionView() {
        suggestionTextView.setText(currentSuggestion.getSuggestion());
    }

    /**
     * Sets list of cards with horizontal scroll.
     */
    private void setupCarousel() {
        cardsAdapter = new CardsAdapter(context, currentSuggestion, currentSuggestion.getCards());
        cardsAdapter.setCardClickListener(cardClickListener);

        cardsListView.clearOnScrollListeners();
        cardsListView.setChildDrawingOrderCallback(null);
        cardsListView.setLayoutManager(layoutManager);
        cardsListView.setAdapter(cardsAdapter);
        cardsListView.addOnScrollListener(createTrackOnScrollListener());

        dividerItemDecoration.setTopBottomPadding(ITEMS_SPACING);
        rootLayout.getLayoutParams().height = getRowHeight(SUGGESTION_HEIGHT + ITEMS_SPACING);
    }

    private int getRowHeight(int additionalHeight) {
        Card card = currentSuggestion.getCards().get(0);
        ImageParams imageParams = ImageParams.create(card);
        return (int) (imageParams.height + ViewUtils.dpToPx(additionalHeight) * SCALE_FACTOR);
    }

    @NonNull
    private RecyclerView.OnScrollListener createTrackOnScrollListener() {
        final HashSet<Integer> checkedCardPositions = new HashSet<>();
        cardsListView.post(() -> performCardsTracking(checkedCardPositions, cardsListView));
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                recyclerView.post(() -> performCardsTracking(checkedCardPositions, recyclerView));
                super.onScrolled(recyclerView, dx, dy);
            }
        };
    }

    private void performCardsTracking(HashSet<Integer> checkedCardPositions, RecyclerView hostRecyclerView) {
        final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        updateSetOfVisibleCardsPositions(checkedCardPositions, firstVisibleItemPosition, lastVisibleItemPosition);

        for (int i = 0; i <= cardsAdapter.getItemCount(); i++) {
            CardsAdapter.CardViewHolder cardHolder = (CardsAdapter.CardViewHolder) hostRecyclerView.findViewHolderForAdapterPosition(i);
            CardView container = (cardHolder != null) ? cardHolder.cardRootView : null;
            if (container == null) {
                continue;
            }

            // Fetching the image view instance from a parent view by a given position.
            CardImageView cardImageView = (CardImageView) container.findViewById(R.id.card_image_view);
            if (checkedCardPositions.contains(i)) {
                Log.d("VH", "Card visible: " + i);
                trackVisibleCard(container, cardImageView);
            } else {
                Log.d("VH", "Card hidden: " + i);
                cardImageView.setCardHidden();
            }
        }
    }

    private void updateSetOfVisibleCardsPositions(HashSet<Integer> visibleIndexes, int firstVisiblePos, int lastVisiblePos) {
        visibleIndexes.clear();
        for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
            visibleIndexes.add(i);
        }
    }

    private void trackVisibleCard(@NonNull View cardRootView, @NonNull CardImageView cardImageView) {
        cardRootView.measure(0, 0);

        // Getting visible area of the card here.
        Rect visibleCardRect = new Rect();
        cardRootView.getGlobalVisibleRect(visibleCardRect);

        // Calculating width of the visible card area.
        float visibleCardWidth = visibleCardRect.right - visibleCardRect.left;
        float visibleCardHeight = Math.abs(visibleCardRect.bottom - visibleCardRect.top);

        float fullCardWidth = cardRootView.getMeasuredWidth();
        float fullCardHeight = cardRootView.getMeasuredHeight();

        float fullCardArea = fullCardHeight * fullCardWidth;
        float visibleCardArea = visibleCardHeight * visibleCardWidth;

        float visibleCardAreaPercentage = visibleCardArea / fullCardArea;

        if (visibleCardAreaPercentage >= MIN_TRACKING_AREA) {
            cardImageView.setCardMostlyVisible();
        } else {
            cardImageView.setCardHidden();
        }
    }
}
