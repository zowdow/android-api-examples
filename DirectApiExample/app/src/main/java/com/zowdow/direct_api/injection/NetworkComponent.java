package com.zowdow.direct_api.injection;

import com.zowdow.direct_api.network.services.UnifiedApiService;
import com.zowdow.direct_api.ui.HomeDemoActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    void inject(HomeDemoActivity activity);
    UnifiedApiService provideUnifiedApiService();
}
