package com.zowdow.direct_api.injection;

import com.zowdow.direct_api.injection.scopes.PerApplication;
import com.zowdow.direct_api.network.services.UnifiedApiService;
import com.zowdow.direct_api.tracking.CardImpressionTracker;
import com.zowdow.direct_api.tracking.TrackHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackingModule {
    @Provides
    @PerApplication
    TrackHelper provideTrackHelper(UnifiedApiService unifiedApiService) {
        return new TrackHelper(unifiedApiService);
    }

    @Provides
    @PerApplication
    CardImpressionTracker provideCardImpressionTracker(TrackHelper trackHelper) {
        return new CardImpressionTracker(trackHelper);
    }
}
