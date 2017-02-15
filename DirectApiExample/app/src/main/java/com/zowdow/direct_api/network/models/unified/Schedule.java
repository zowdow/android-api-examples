package com.zowdow.direct_api.network.models.unified;

import com.google.gson.annotations.SerializedName;

/**
 * Represents opening hours for different spots.
 */
public class Schedule {
    private @SerializedName("startMs") long mStartMs;
    private @SerializedName("endMs") long mEndMs;
    private @SerializedName("visual") String mOpeningHoursString;
    private @SerializedName("day") String mDay;

    public Schedule() {
    }

    public Schedule(long startMs, long endMs, String openingHoursString, String day) {
        this.mStartMs = startMs;
        this.mEndMs = endMs;
        this.mOpeningHoursString = openingHoursString;
        this.mDay = day;
    }

    /**
     * Returns opening hours.
     *
     * @return opening hours.
     */
    public long getStartMs() {
        return mStartMs;
    }

    /**
     * Sets opening hours.
     *
     * @param startMs
     */
    public void setStartMs(long startMs) {
        this.mStartMs = startMs;
    }

    /**
     * Returns closing hours.
     *
     * @return closing hours.
     */
    public long getEndMs() {
        return mEndMs;
    }

    /**
     * Sets closing hours.
     *
     * @param endMs
     */
    public void setEndMs(long endMs) {
        this.mEndMs = endMs;
    }

    /**
     * Returns work schedule for the current spot represented by card as a formatted string.
     *
     * @return work schedule for the current spot represented by card as a formatted string.
     */
    public String getOpeningHoursString() {
        return mOpeningHoursString;
    }

    /**
     * Sets work schedule for the current spot represented by card as a formatted string.
     *
     * @param openingHoursString
     */
    public void setOpeningHoursString(String openingHoursString) {
        this.mOpeningHoursString = openingHoursString;
    }

    /**
     * Returns the day associated with current schedule.
     *
     * @return the day associated with current schedule.
     */
    public String getDay() {
        return mDay;
    }

    /**
     * Sets the day associated with current schedule.
     *
     * @param day
     */
    public void setDay(String day) {
        this.mDay = day;
    }
}