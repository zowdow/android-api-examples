package com.zowdow.direct_api;

import android.app.Application;

import com.zowdow.direct_api.injection.DaggerNetworkComponent;
import com.zowdow.direct_api.injection.DaggerTrackingComponent;
import com.zowdow.direct_api.injection.NetworkComponent;
import com.zowdow.direct_api.injection.TrackingComponent;

public class ZowdowDirectApplication extends Application {
    private static NetworkComponent networkComponent;
    private static TrackingComponent trackingComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder().build();
        trackingComponent = DaggerTrackingComponent.builder().networkComponent(networkComponent).build();
    }

    public static NetworkComponent getNetworkComponent() {
        return networkComponent;
    }

    public static TrackingComponent getTrackingComponent() {
        return trackingComponent;
    }
}