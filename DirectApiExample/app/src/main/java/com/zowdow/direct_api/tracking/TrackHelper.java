package com.zowdow.direct_api.tracking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zowdow.direct_api.network.models.unified.Schedule;
import com.zowdow.direct_api.network.services.UnifiedApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class TrackHelper {
    private static final String TAG = TrackHelper.class.getSimpleName();

    private UnifiedApiService unifiedApiService;

    @Inject
    public TrackHelper(@Singleton UnifiedApiService unifiedApiService) {
        this.unifiedApiService = unifiedApiService;
    }

    void trackImpression(@NonNull final String impressionUrl) {
        unifiedApiService.performTracking(impressionUrl)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "Tracked impression successfully: " + impressionUrl),
                        Throwable::printStackTrace
                );
    }

    public void trackClick(@NonNull final String clickUrl) {
        unifiedApiService.performTracking(clickUrl)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () -> Log.d(TAG, "Tracked click successfully: " + clickUrl),
                        Throwable::printStackTrace
                );
    }
}
