package com.zowdow.direct_api.network.services;

import com.zowdow.direct_api.network.models.base.BaseResponse;
import com.zowdow.direct_api.network.models.unified.UnifiedResponse;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Service which is responsible for suggestions loading, and other operations related to
 * Zowdow API.
 */
public interface UnifiedApiService {
    /**
     * Loads suggestions for a given keyword.
     *
     * @param queryMap Map of request parameters incl. suggestions filtering params.
     * @return
     */
    @GET("unified")
    Observable<BaseResponse<UnifiedResponse>> loadSuggestions(@QueryMap Map<String, Object> queryMap);

    /**
     * Sends tracking call to Unified API.
     * Needs to be called when there's some info that needs to be tracked: in case of this demo app,
     * cards' impressions and clicks.
     *
     * @param urlToTrack
     * @return
     */
    @GET
    Completable performTracking(@Url String urlToTrack);
}