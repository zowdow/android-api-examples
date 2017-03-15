package com.zowdow.direct_api.injection;

import com.zowdow.direct_api.injection.scopes.PerApplication;
import com.zowdow.direct_api.ui.adapters.CardsAdapter;
import com.zowdow.direct_api.ui.adapters.SuggestionsAdapter;
import com.zowdow.direct_api.ui.views.CardImageView;

import dagger.Component;

@PerApplication
@Component(modules = TrackingModule.class, dependencies = NetworkComponent.class)
public interface TrackingComponent {
    void inject(CardsAdapter cardsAdapter);
    void inject(CardImageView cardImageView);
    void inject(SuggestionsAdapter suggestionsAdapter);
}