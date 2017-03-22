package com.zowdow.direct_api.tracking;

import android.support.annotation.NonNull;

/**
 * The interface which defines basic methods essential for cards tracking events such as
 * impressions and clicks.
 */
public interface TrackingRequestManager {
    /**
     * This method's implementation is intended to track card's impression event. It
     * should be invoked only if the following conditions are satisfied:
     *
     * a) The card's area visible on a device screen takes >= 50% of its full size (it affects width
     * and height measurements).
     *
     * b) The tracked card remains visible for more than a defined lapse of time. This method should
     * be invoked only after this period expires.
     *
     * @param impressionUrl
     */
    void trackCardImpression(@NonNull final String impressionUrl);

    /**
     * This method should be invoked as soon as the card gets clicked.
     *
     * @param clickUrl
     */
    void trackCardClick(@NonNull final String clickUrl);
}
