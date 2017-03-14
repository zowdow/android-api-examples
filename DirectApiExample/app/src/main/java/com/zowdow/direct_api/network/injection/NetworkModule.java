package com.zowdow.direct_api.network.injection;

import com.google.gson.GsonBuilder;
import com.zowdow.direct_api.network.ApiBaseUrls;
import com.zowdow.direct_api.network.services.UnifiedApiService;
import com.zowdow.direct_api.tracking.CardImpressionTracker;
import com.zowdow.direct_api.tracking.TrackHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module which provides an access to network-related services which rely on Zowdow API.
 */
@Module
public class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeSpecialFloatingPointValues().create()));
    }

    @Provides
    @Singleton
    UnifiedApiService provideUnifiedApiService(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder
                .baseUrl(ApiBaseUrls.ZOWDOW_API).build()
                .create(UnifiedApiService.class);
    }

    @Provides
    @Singleton
    TrackHelper provideTrackHelper(UnifiedApiService unifiedApiService) {
        return new TrackHelper(unifiedApiService);
    }

    @Provides
    @Singleton
    CardImpressionTracker provideCardImpressionTracker(TrackHelper trackHelper) {
        return new CardImpressionTracker(trackHelper);
    }
}