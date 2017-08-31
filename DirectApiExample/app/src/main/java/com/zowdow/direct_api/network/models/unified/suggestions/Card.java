package com.zowdow.direct_api.network.models.unified.suggestions;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.zowdow.direct_api.network.models.unified.ActionModel;
import com.zowdow.direct_api.network.models.unified.Schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents Zowdow Card
 */
public class Card implements Parcelable {
    public static final float SIZE_MULTIPLIER = 1f;

    private static final String ACTION_PROPERTY_TYPE = "type";
    private static final String ACTION_PROPERTY_TARGET = "target";

    private static final String SCHEDULE_PROPERTY_START_TIME = "startMs";
    private static final String SCHEDULE_PROPERTY_END_TIME = "endMs";
    private static final String SCHEDULE_PROPERTY_VISUAL = "visual";
    private static final String SCHEDULE_PROPERTY_DAY = "day";

    @SerializedName("id") private           String              mId;
    private                                 String              mRid;
    @SerializedName("x1") private           String              mX1;
    @SerializedName("x1_h") private         int                 mX1h;
    @SerializedName("x1_w") private         int                 mX1w;
    @SerializedName("x2") private           String              mX2;
    @SerializedName("x2_h") private         int                 mX2h;
    @SerializedName("x2_w") private         int                 mX2w;
    @SerializedName("x3") private           String              mX3;
    @SerializedName("x3_h") private         int                 mX3h;
    @SerializedName("x3_w") private         int                 mX3w;
    @SerializedName("x4") private           String              mX4;
    @SerializedName("x4_h") private         int                 mX4h;
    @SerializedName("x4_w") private         int                 mX4w;
    @SerializedName("D1") private           String              mD1;
    @SerializedName("D1_h") private         int                 mD1h;
    @SerializedName("D1_w") private         int                 mD1w;
    @SerializedName("D2") private           String              mD2;
    @SerializedName("D2_h") private         int                 mD2h;
    @SerializedName("D2_w") private         int                 mD2w;
    @SerializedName("cardRank") private     int                 mCardRank;
    @SerializedName("card_format") private  String              mCardFormat;
    @SerializedName("actions") private      List<ActionModel>     mActions;
    private                                 boolean             mTracked;
    private                                 boolean             mGif;
    @SerializedName("card_impression_url")  private List<String>      mImpressionUrl;
    @SerializedName("card_click_url")       private List<String>      mClickUrl;
    @SerializedName("lat")                  private Double      mLatitude;
    @SerializedName("long")                 private Double      mLongitude;
    @SerializedName("dist")                 private Double      mDistance;
    @SerializedName("schedule") private     List<Schedule>   mSchedule;

    public Card() {}

    /**
     * Get card id
     *
     * @return card id
     */
    public String getId() {
        return mId;
    }

    /**
     * Set card id
     *
     * @param id card id
     */
    public void setId(String id) {
        mId = id;
    }

    /**
     * Get request id
     *
     * @return request id
     */
    public String getRid() {
        return mRid;
    }

    /**
     * Set request id
     *
     * @param rid request id
     */
    public void setRid(String rid) {
        mRid = rid;
    }

    /**
     * Get image url for mdpi
     *
     * @return image url for mdpi
     */
    public String getX1() {
        return mX1;
    }

    /**
     * Set image url for mdpi
     *
     * @param x1 image url for mdpi
     */
    public void setX1(String x1) {
        mX1 = x1;
    }

    /**
     * Get image height for mdpi in pixels
     *
     * @return image height for mdpi in pixels
     */
    public int getX1h() {
        return mX1h;
    }

    public int getMultipliedX1h() {
        return (int) (mX1h * SIZE_MULTIPLIER);
    }

    /**
     * Set image height for mdpi in pixels
     *
     * @param x1h image height for mdpi in pixels
     */
    public void setX1h(int x1h) {
        mX1h = x1h;
    }

    /**
     * Get image width for mdpi in pixels
     *
     * @return image width for mdpi in pixels
     */
    public int getX1w() {
        return mX1w;
    }

    public int getMultipliedX1w() {
        return (int) (mX1w * SIZE_MULTIPLIER);
    }

    /**
     * Set image width for mdpi in pixels
     *
     * @param x1w image width for mdpi in pixels
     */
    public void setX1w(int x1w) {
        mX1w = x1w;
    }

    /**
     * Get image url for hdpi
     *
     * @return image url for hdpi
     */
    public String getX2() {
        return mX2;
    }

    /**
     * Set image url for hdpi
     *
     * @param x2 image url for hdpi
     */
    public void setX2(String x2) {
        mX2 = x2;
    }

    /**
     * Get image height for hdpi in pixels
     *
     * @return image height for hdpi in pixels
     */
    public int getX2h() {
        return mX2h;
    }

    public int getMultipliedX2h() {
        return (int) (mX2h * SIZE_MULTIPLIER);
    }

    /**
     * Set image height for hdpi in pixels
     *
     * @param x2h image height for hdpi in pixels
     */
    public void setX2h(int x2h) {
        mX2h = x2h;
    }

    /**
     * Get image width for hdpi in pixels
     *
     * @return image width for hdpi in pixels
     */
    public int getX2w() {
        return mX2w;
    }

    public int getMultipliedX2w() {
        return (int) (mX2w * SIZE_MULTIPLIER);
    }

    /**
     * Set image width for hdpi in pixels
     *
     * @param x2w image width for hdpi in pixels
     */
    public void setX2w(int x2w) {
        mX2w = x2w;
    }

    /**
     * Get image url for xhdpi
     *
     * @return image url for xhdpi
     */
    public String getX3() {
        return mX3;
    }

    /**
     * Set image url for xhdpi
     *
     * @param x3 image url for xhdpi
     */
    public void setX3(String x3) {
        mX3 = x3;
    }

    /**
     * Get image height for xhdpi in pixels
     *
     * @return image height for xhdpi in pixels
     */
    public int getX3h() {
        return mX3h;
    }

    public int getMultipliedX3h() {
        return (int) (mX3h * SIZE_MULTIPLIER);
    }

    /**
     * Set image height for xhdpi in pixels
     *
     * @param x3h image height for xhdpi in pixels
     */
    public void setX3h(int x3h) {
        mX3h = x3h;
    }

    /**
     * Get image width for xhdpi in pixels
     *
     * @return image width for xhdpi in pixels
     */
    public int getX3w() {
        return mX3w;
    }

    public int getMultipliedX3w() {
        return (int) (mX3w * SIZE_MULTIPLIER);
    }

    /**
     * Set image width for xhdpi in pixels
     *
     * @param x3w image width for xhdpi in pixels
     */
    public void setX3w(int x3w) {
        mX3w = x3w;
    }

    /**
     * Get image url for xxhdpi
     *
     * @return image url for xxhdpi
     */
    public String getX4() {
        return mX4;
    }

    /**
     * Set image url for xxhdpi
     *
     * @param x4 image url for xxhdpi
     */
    public void setX4(String x4) {
        mX4 = x4;
    }

    /**
     * Get image height for xxhdpi in pixels
     *
     * @return image height for xxhdpi in pixels
     */
    public int getX4h() {
        return mX4h;
    }

    public int getMultipliedX4h() {
        return (int) (mX4h * SIZE_MULTIPLIER);
    }

    /**
     * Set image height for xxhdpi in pixels
     *
     * @param x4h image height for xxhdpi in pixels
     */
    public void setX4h(int x4h) {
        mX4h = x4h;
    }

    /**
     * Get image width for xxhdpi in pixels
     *
     * @return image width for xxhdpi in pixels
     */
    public int getX4w() {
        return mX4w;
    }

    public int getMultipliedX4w() {
        return (int) (mX4w * SIZE_MULTIPLIER);
    }

    /**
     * Set image width for xxhdpi in pixels
     *
     * @param x4w image width for xxhdpi in pixels
     */
    public void setX4w(int x4w) {
        mX4w = x4w;
    }

    /**
     * Get card rank
     *
     * @return card rank
     */
    public int getCardRank() {
        return mCardRank;
    }

    /**
     * Set card rank
     *
     * @param cardRank card rank
     */
    public void setCardRank(int cardRank) {
        mCardRank = cardRank;
    }

    /**
     * Set card format
     *
     * @param cardFormat
     */
    public void setCardFormat(String cardFormat) {
        this.mCardFormat = cardFormat;
    }

    /**
     * Get card format
     *
     * @return card format
     */
    public String getCardFormat() {
        return mCardFormat;
    }

    public String getD1() {
        return mD1;
    }

    public void setD1(String d1) {
        mD1 = d1;
    }

    public int getD1h() {
        return mD1h;
    }

    public void setD1h(int d1h) {
        mD1h = d1h;
    }

    public int getD1w() {
        return mD1w;
    }

    public void setD1w(int d1w) {
        mD1w = d1w;
    }

    public String getD2() {
        return mD2;
    }

    public void setD2(String d2) {
        mD2 = d2;
    }

    public int getD2h() {
        return mD2h;
    }

    public void setD2h(int d2h) {
        mD2h = d2h;
    }

    public int getD2w() {
        return mD2w;
    }

    public void setD2w(int d2w) {
        mD2w = d2w;
    }

    public void setGif(boolean gif) {
        mGif = gif;
    }

    public boolean isGif() {
        return mGif;
    }


    /**
     * Returns the latitude coordinate of the spot represented by a card.
     *
     * @return the latitude coordinate of the spot represented by a card.
     */
    public Double getLatitude() {
        return mLatitude;
    }

    /**
     * Sets the latitude coordinate of the spot represented by a card.
     *
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    /**
     * Returns the longitude coordinate of the spot represented by a card.
     *
     * @return the longitude coordinate of the spot represented by a card.
     */
    public Double getLongitude() {
        return mLongitude;
    }

    /**
     * Sets the longitude coordinate of the spot represented by a card.
     *
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    /**
     * Returns the distance to the spot represented by this card.
     *
     * @return the distance to the spot represented by this card.
     */
    public Double getDistance() {
        return mDistance;
    }

    /**
     * Sets the distance to the spot represented by this card.
     *
     * @param distance
     */
    public void setDistance(Double distance) {
        mDistance = distance;
    }

    /**
     * Get a list of actions for the card
     *
     * @return list of actions for the card
     */
    public List<ActionModel> getActions() {
        return mActions;
    }

    /**
     * Set a list of actions for the card
     *
     * @param actions list of actions for the card
     */
    public void setActions(List<ActionModel> actions) {
        mActions = actions;
    }

    public List<String> getImpressionUrl() {
        return mImpressionUrl;
    }

    public void setImpressionUrl(List<String> impressionUrl) {
        this.mImpressionUrl = impressionUrl;
    }

    public List<String> getClickUrl() {
        return mClickUrl;
    }

    public void setClickUrl(List<String> clickUrl) {
        this.mClickUrl = clickUrl;
    }

    /**
     * Returns the opening hours schedule for the spot represented by the card.
     *
     * @return the opening hours schedule for the spot represented by the card.
     */
    public List<Schedule> getSchedule() {
        return mSchedule;
    }

    /**
     * Sets the opening hours schedule for the spot represented by the card.
     *
     * @param schedule
     */
    public void setSchedule(List<Schedule> schedule) {
        this.mSchedule = schedule;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mRid);
        dest.writeString(mX1);
        dest.writeInt(mX1h);
        dest.writeInt(mX1w);
        dest.writeString(mX2);
        dest.writeInt(mX2h);
        dest.writeInt(mX2w);
        dest.writeString(mX3);
        dest.writeInt(mX3h);
        dest.writeInt(mX3w);
        dest.writeString(mX4);
        dest.writeInt(mX4h);
        dest.writeInt(mX4w);
        dest.writeString(mD1);
        dest.writeInt(mD1h);
        dest.writeInt(mD1w);
        dest.writeString(mD2);
        dest.writeInt(mD2h);
        dest.writeInt(mD2w);
        if (mLatitude != null) {
            dest.writeDouble(mLatitude);
        }
        if (mLongitude != null) {
            dest.writeDouble(mLongitude);
        }
        if (mDistance != null) {
            dest.writeDouble(mDistance);
        }
        dest.writeInt(mCardRank);
        dest.writeString(mCardFormat);
        dest.writeStringList(mClickUrl);
        dest.writeStringList(mImpressionUrl);
        JsonArray actions = new JsonArray();
        for (ActionModel action : mActions) {
            JsonObject jsonAction = new JsonObject();
            jsonAction.addProperty(ACTION_PROPERTY_TYPE, action.getActionType());
            jsonAction.addProperty(ACTION_PROPERTY_TARGET, action.getActionTarget());
            actions.add(jsonAction);
        }
        dest.writeString(actions.toString());
        JsonArray schedule = new JsonArray();
        for (Schedule dailySchedule : mSchedule) {
            JsonObject jsonSchedule = new JsonObject();
            jsonSchedule.addProperty(SCHEDULE_PROPERTY_START_TIME, dailySchedule.getStartMs());
            jsonSchedule.addProperty(SCHEDULE_PROPERTY_END_TIME, dailySchedule.getEndMs());
            jsonSchedule.addProperty(SCHEDULE_PROPERTY_VISUAL, dailySchedule.getOpeningHoursString());
            jsonSchedule.addProperty(SCHEDULE_PROPERTY_DAY, dailySchedule.getDay());
            schedule.add(jsonSchedule);
        }
        dest.writeString(schedule.toString());
    }

    /**
     * Used to restore object from Parcel
     */
    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    /**
     * Used to restore object from Parcel
     */
    private Card(Parcel source) {
        mId = source.readString();
        mRid = source.readString();
        mX1 = source.readString();
        mX1h = source.readInt();
        mX1w = source.readInt();
        mX2 = source.readString();
        mX2h = source.readInt();
        mX2w = source.readInt();
        mX3 = source.readString();
        mX3h = source.readInt();
        mX3w = source.readInt();
        mX4 = source.readString();
        mX4h = source.readInt();
        mX4w = source.readInt();
        mD1 = source.readString();
        mD1h = source.readInt();
        mD1w = source.readInt();
        mD2 = source.readString();
        mD2h = source.readInt();
        mD2w = source.readInt();
        mCardRank = source.readInt();
        mCardFormat = source.readString();
        source.readStringList(mClickUrl);
        source.readStringList(mImpressionUrl);
        mActions = new ArrayList<>();
        JsonArray actions = new JsonParser().parse(source.readString()).getAsJsonArray();
        for (JsonElement jsonElement : actions) {
            JsonObject jsonAction = jsonElement.getAsJsonObject();
            mActions.add(new ActionModel(
                    jsonAction.get(ACTION_PROPERTY_TYPE).getAsString(),
                    jsonAction.get(ACTION_PROPERTY_TARGET).getAsString()
            ));
        }
        mSchedule = new ArrayList<>();
        JsonArray schedules = new JsonParser().parse(source.readString()).getAsJsonArray();
        for (JsonElement jsonElement : schedules) {
            JsonObject jsonSchedule = jsonElement.getAsJsonObject();
            mSchedule.add(new Schedule(
                    jsonSchedule.get(SCHEDULE_PROPERTY_START_TIME).getAsLong(),
                    jsonSchedule.get(SCHEDULE_PROPERTY_END_TIME).getAsLong(),
                    jsonSchedule.get(SCHEDULE_PROPERTY_VISUAL).getAsString(),
                    jsonSchedule.get(SCHEDULE_PROPERTY_DAY).getAsString()
            ));
        }
    }

    /**
     * Returns has the card been tracked. If tracked was false, it'll become true
     *
     * @return true is the suggestion has been tracked
     */
    public boolean hasBeenTracked() {
        boolean result = mTracked;
        mTracked = true;
        return result;
    }

    /**
     * Returns has the card been tracked
     *
     * @return is Card tracked
     */
    public boolean isTracked() {
        return mTracked;
    }
}