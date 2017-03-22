package com.zowdow.direct_api.injection;

import com.zowdow.direct_api.injection.scopes.PerApplication;
import com.zowdow.direct_api.network.services.UnifiedApiService;
import com.zowdow.direct_api.tracking.CardImpressionTracker;
import com.zowdow.direct_api.tracking.TrackingRequestManager;
import com.zowdow.direct_api.tracking.TrackingRequestManagerImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Module which provides tracking-specific functionality.
 */
@Module
class TrackingModule {
    @Provides
    @PerApplication
    TrackingRequestManager provideTrackHelper(UnifiedApiService unifiedApiService) {
        return new TrackingRequestManagerImpl(unifiedApiService);
    }

    @Provides
    @PerApplication
    CardImpressionTracker provideCardImpressionTracker(TrackingRequestManager trackHelper) {
        return new CardImpressionTracker(trackHelper);
    }
}
