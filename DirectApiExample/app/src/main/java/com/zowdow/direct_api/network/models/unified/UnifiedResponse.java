package com.zowdow.direct_api.network.models.unified;

import com.google.gson.annotations.SerializedName;

public class UnifiedResponse {
    @SerializedName("suggestion") private Suggestion mSuggestion;

    public Suggestion getSuggestion() {
        return mSuggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        mSuggestion = suggestion;
    }
}
