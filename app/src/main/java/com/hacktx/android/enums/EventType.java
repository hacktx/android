package com.hacktx.android.enums;

import com.google.gson.annotations.SerializedName;

public enum EventType {
    @SerializedName("food")
    FOOD,
    @SerializedName("education")
    EDUCATION,
    @SerializedName("talk")
    TALK,
    @SerializedName("bus")
    BUS,
    @SerializedName("dev")
    DEV
}
