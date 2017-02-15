package com.zowdow.direct_api.utils.location;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.StringRes;

import com.zowdow.direct_api.R;
import com.zowdow.direct_api.utils.PermissionsUtils;

import java.text.DecimalFormat;

public class LocationManager {
    private static final double KILOMETERS_IN_MILE = 0.621;

    private static LocationManager locationManagerInstance;

    private int mClients = 0;

    private AndroidLocation mAndroidLocation;

    private LocationManager() {
        mAndroidLocation = AndroidLocation.getInstance();
    }

    public static synchronized LocationManager get() {
        if (locationManagerInstance == null) {
            locationManagerInstance = new LocationManager();
        }
        return locationManagerInstance;
    }

    /**
     * Check does we have location permissions
     *
     * @param context Android Context
     * @return true if an app has at least one of Fine or Coarse permissions
     */
    private boolean hasLocationPermissions(Context context) {
        return PermissionsUtils.checkFineLocationPermission(context)
                || PermissionsUtils.checkCoarseLocationPermission(context);
    }

    /**
     * Start receiving location updates
     *
     * @param context Android Context
     */
    public synchronized void start(Context context) {
        if (!hasLocationPermissions(context)) {
            return;
        }

        mClients++;
        mAndroidLocation.start(context);

    }

    /**
     * Stop receiving location updates
     */
    public synchronized void stop() {
        if (--mClients == 0) {
            mAndroidLocation.stop();
        }
    }

    /**
     * Get last known location
     *
     * @param context Android Context
     * @return Last known Location
     */
    @SuppressWarnings("MissingPermission")
    public Location getLocation(Context context) {
        if (!hasLocationPermissions(context)) {
            return null;
        }

        return mAndroidLocation.getLocation(context);
    }

    private static double convertMilesToKm(double distanceInMiles) {
        return distanceInMiles / KILOMETERS_IN_MILE;
    }

    /**
     * Returns formatted distance for spot distance embellishment.
     * @param originalDistance
     * @return
     */
    public static String getFormattedDistance(double originalDistance) {
        double rawDistance = getDistanceInLocalUnits(originalDistance) / 1000;
        if (rawDistance < 1000) {
            return new DecimalFormat("##.#").format(rawDistance);
        } else {
            return String.valueOf((int) Math.round(rawDistance));
        }
    }

    public static double getDistanceInLocalUnits(double originalDistance) {
        if (isUsLocale()) {
            return originalDistance;
        } else {
            return convertMilesToKm(originalDistance);
        }
    }

    private static boolean isUsLocale() {
        final String currentLocale = getCurrentLocale();
        return currentLocale.equalsIgnoreCase("usa") || currentLocale.equalsIgnoreCase("mmr");
    }

    /**
     * Returns current device locale.
     * @return
     */
    private static String getCurrentLocale() {
        return Resources.getSystem().getConfiguration().locale.getISO3Country();
    }

    /**
     * Returns local distance units string.
     * @return
     */
    public static @StringRes int getLocalUnitsRes() {
        if (isUsLocale()) {
            return R.string.distance_miles;
        } else {
            return R.string.distance_km;
        }
    }
}
