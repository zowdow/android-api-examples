package com.zowdow.direct_api.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.zowdow.direct_api.R;
import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.models.unified.ActionModel;
import com.zowdow.direct_api.network.models.unified.suggestions.Card;
import com.zowdow.direct_api.network.models.unified.suggestions.Suggestion;
import com.zowdow.direct_api.tracking.TrackingRequestManager;
import com.zowdow.direct_api.ui.VideoActivity;
import com.zowdow.direct_api.ui.views.CardImageView;
import com.zowdow.direct_api.utils.ViewUtils;
import com.zowdow.direct_api.utils.constants.ActionTypes;
import com.zowdow.direct_api.utils.ImageParams;
import com.zowdow.direct_api.utils.constants.CardFormats;
import com.zowdow.direct_api.utils.location.LocationManager;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for cards list inside each suggestion carousel.
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {
    private Context context;
    private Suggestion currentSuggestion;
    private List<Card> cards;
    private OnCardClickListener cardClickListener;

    @Inject
    TrackingRequestManager trackManager;

    private CardsAdapter() {
        ZowdowDirectApplication.getTrackingComponent().inject(this);
    }

    CardsAdapter(@NonNull Context context, @NonNull Suggestion currentSuggestion, @NonNull List<Card> cards) {
        this();
        this.context = context;
        this.currentSuggestion = currentSuggestion;
        this.cards = cards;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card currentCard = cards.get(position);
        ImageParams imageParamsForCard = ImageParams.create(currentCard);

        DrawableTypeRequest<String> glideRequest = Glide.with(context).load(imageParamsForCard.path);

        if (currentCard.getCardFormat().equals(CardFormats.CARD_FORMAT_ANIMATED_GIF)) {
            GlideDrawableImageViewTarget gifTarget = new GlideDrawableImageViewTarget(holder.cardImageView);
            glideRequest
                    .override(imageParamsForCard.width, imageParamsForCard.height)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(gifTarget);
        } else {
            glideRequest
                    .asBitmap()
                    .fitCenter()
                    .override(imageParamsForCard.width, imageParamsForCard.height)
                    .into(new ImageViewTarget<Bitmap>(holder.cardImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.cardRootView.setCardElevation(ViewUtils.dpToPx(2));
                            holder.cardImageView.setImageBitmap(resource);
                            holder.cardImageView.setTrackInfo(currentCard);
                        }
                    });
        }

        holder.cardImageView.setOnClickListener(v -> {
            processCardClick(context, currentCard);
        });

        setupDistanceEmbellishment(context, holder.distanceTextView, currentCard, imageParamsForCard);
    }

    /**
     * Sets distance embellishment that overlays the card image view.
     * @param context
     * @param distanceTextView
     * @param card
     * @param imageParams
     */
    private void setupDistanceEmbellishment(@NonNull Context context, @NonNull final TextView distanceTextView, @NonNull final Card card, @NonNull ImageParams imageParams) {
        if (card.getLongitude() != null && card.getLatitude() != null) {
            distanceTextView.setText(
                    LocationManager.getFormattedDistance(card.getDistance()) + " " + context.getString(LocationManager.getLocalUnitsRes())
            );
            distanceTextView.setVisibility(View.VISIBLE);
            setDistanceEmbellishmentMargins(distanceTextView, card, imageParams);
        } else {
            distanceTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Sets distance embellishment margins.
     * @param distanceTextView
     * @param card
     * @param imageParams
     */
    private void setDistanceEmbellishmentMargins(@NonNull final TextView distanceTextView, @NonNull final Card card, @NonNull ImageParams imageParams) {
        distanceTextView.measure(0, 0);
        final int labelWidth = distanceTextView.getMeasuredWidth();
        final int labelHeight = distanceTextView.getMeasuredHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) distanceTextView.getLayoutParams();
        switch (card.getCardFormat()) {
            case CardFormats.CARD_FORMAT_INLINE:
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.setMargins(imageParams.distanceLabelMargin - labelWidth, 0, 0, ViewUtils.dpToPx(3));
                break;
            case CardFormats.CARD_FORMAT_STAMP:
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.setMargins(imageParams.width - labelWidth, imageParams.distanceLabelMargin - labelHeight, ViewUtils.dpToPx(6), 0);
                break;
            case CardFormats.CARD_FORMAT_TICKET:
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                layoutParams.setMargins(imageParams.distanceLabelMargin - labelWidth, 0, 0, ViewUtils.dpToPx(8));
                break;
            default:
                distanceTextView.setVisibility(View.GONE);
                break;
        }
        distanceTextView.setLayoutParams(layoutParams);
    }

    /**
     * Handles card click events depending on their type & target.
     * @see ActionTypes
     * @param context
     * @param card
     */
    private void processCardClick(Context context, Card card) {
        HashMap<String, String> actions = new HashMap<>();
        for (ActionModel action : card.getActions()) {
            actions.put(action.getActionType(), action.getActionTarget());
        }
        String actionType;
        String actionTarget;
        if (actions.containsKey(ActionTypes.ACTION_VIDEO)) {
            actionType = ActionTypes.ACTION_VIDEO;
            actionTarget = actions.get(actionType);
            onVideoCardClicked(context, actionTarget, card.getClickUrl());
        } else if (actions.containsKey(ActionTypes.ACTION_DEEP_LINK) &&
                new Intent(Intent.ACTION_VIEW, Uri.parse(actions.get(ActionTypes.ACTION_DEEP_LINK))).resolveActivity(context.getPackageManager()) != null) {
            actionType = ActionTypes.ACTION_DEEP_LINK;
            actionTarget = actions.get(actionType);
            onWebContentCardClicked(actionTarget, card.getClickUrl());
        } else if (actions.containsKey(ActionTypes.ACTION_WEB_URL)) {
            actionType = ActionTypes.ACTION_WEB_URL;
            actionTarget = actions.get(actionType);
            onWebContentCardClicked(actionTarget, card.getClickUrl());
        }
    }

    private void onWebContentCardClicked(String actionTarget, String clickUrl) {
        trackManager.trackCardClick(clickUrl);
        if (cardClickListener != null && actionTarget != null) {
            cardClickListener.onCardClicked(actionTarget, currentSuggestion.getSuggestion());
        }
    }

    private void onVideoCardClicked(Context context, String actionTarget, String clickUrl) {
        trackManager.trackCardClick(clickUrl);
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(VideoActivity.EXTRA_VIDEO, actionTarget);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    void setCardClickListener(OnCardClickListener cardClickListener) {
        this.cardClickListener = cardClickListener;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_image_view)
        CardImageView cardImageView;
        @BindView(R.id.card_root_view)
        CardView cardRootView;
        @BindView(R.id.distance_text_view)
        TextView distanceTextView;

        CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}