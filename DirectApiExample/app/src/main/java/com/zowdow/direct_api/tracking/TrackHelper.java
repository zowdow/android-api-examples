package com.zowdow.direct_api.tracking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zowdow.direct_api.network.services.UnifiedApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

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
        unifiedApiService.performTracking(impressionUrl).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Tracked impression successfully: " + impressionUrl);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Tracked impression with failure");
            }
        });
    }

    public void trackClick(@NonNull final String clickUrl) {
        unifiedApiService.performTracking(clickUrl).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Tracked clicking successfully!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Tracked clicking with failure");
            }
        });
    }
}
