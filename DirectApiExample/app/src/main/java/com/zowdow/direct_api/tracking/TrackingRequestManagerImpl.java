package com.zowdow.direct_api.tracking;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.zowdow.direct_api.network.services.UnifiedApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
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
    public void trackCardImpression(@NonNull final List<String> impressionUrl) {
        Observable.fromIterable(impressionUrl)
                .map(url -> new Pair<>(url, unifiedApiService.performTracking(url)))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        pair -> Log.d(TAG, "Tracked impression successfully: " + pair.first),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void trackCardClick(@NonNull final List<String> clickUrl) {
        Observable.fromIterable(clickUrl)
                .map(url -> new Pair<>(url, unifiedApiService.performTracking(url)))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        pair -> Log.d(TAG, "Tracked click successfully: " + pair.first),
                        Throwable::printStackTrace
                );
    }
}
