package com.zowdow.direct_api.injection;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.zowdow.direct_api.network.ApiBaseUrls;
import com.zowdow.direct_api.network.services.UnifiedApiService;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module which provides an access to network-related services which rely on Zowdow API.
 */
@Module
public class NetworkModule {
    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static final String CACHE_DIR_NAME = "zowdow";

    private Context appContext;

    public NetworkModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Context appContext) {
        File cacheDirectory = new File(appContext.getCacheDir(), CACHE_DIR_NAME);
        return new Cache(cacheDirectory, CACHE_SIZE);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache okHttpCache) {
        return new OkHttpClient.Builder()
                .cache(okHttpCache)
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
}