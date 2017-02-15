package com.zowdow.direct_api.network.models.init;

import com.google.gson.annotations.SerializedName;
import com.zowdow.direct_api.network.models.unified.MetaData;

import java.util.HashMap;

public class InitResponse {
    @SerializedName("_meta") private MetaData mMeta;
    @SerializedName("records") private HashMap<String, Object> mRecords;

    public MetaData getMeta() {
        return mMeta;
    }

    public HashMap<String, Object> getRecords() {
        return mRecords;
    }
}
