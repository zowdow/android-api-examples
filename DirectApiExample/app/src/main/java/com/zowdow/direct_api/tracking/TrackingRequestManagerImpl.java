package com.zowdow.direct_api.tracking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zowdow.direct_api.network.services.UnifiedApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;

/**
 * Class which implements custom behavior for cards' clicks and impressions detection events by
 * sending requests to tracking-related URLs provided by Zowdow API.
 */
@Singleton
public class TrackingRequestManagerImpl implements TrackingRequestManager {
    private static final String TAG = TrackingRequestManagerImpl.class.getSimpleName();

    private UnifiedApiService unifiedApiService;

    @Inject
    public TrackingRequestManagerImpl(@Singleton UnifiedApiService unifiedApiService) {
        this.unifiedApiService = unifiedApiService;
    }

    @Override
    public void trackCardImpression(@NonNull final String impressionUrl) {
        unifiedApiService.performTracking(impressionUrl)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "Tracked impression successfully: " + impressionUrl),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void trackCardClick(@NonNull final String clickUrl) {
        unifiedApiService.performTracking(clickUrl)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "Tracked click successfully: " + clickUrl),
                        Throwable::printStackTrace
                );
    }
}
