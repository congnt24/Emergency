package com.congnt.emergencyassistance.RetrofitPlaceEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by congnt24 on 14/10/2016.
 */

public class PlaceDetailEntity {
    @SerializedName("result")
    @Expose
    public DetailResult result;

    public class DetailResult {
        @SerializedName("formatted_phone_number")
        @Expose
        public String phone;
    }
}
