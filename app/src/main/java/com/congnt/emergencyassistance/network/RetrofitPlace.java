package com.congnt.emergencyassistance.network;

import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.entity.RetrofitPlaceEntity.PlaceDetailEntity;
import com.congnt.emergencyassistance.entity.RetrofitPlaceEntity.PlaceEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by congnt24 on 04/10/2016.
 */

public interface RetrofitPlace {
        @GET("api/place/nearbysearch/json?sensor=true&key="+ AppConfig.GOOGLE_PLACE_KEY)
        Call<PlaceEntity> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
        @GET("api/place/details/json?key="+ AppConfig.GOOGLE_PLACE_KEY)
        Call<PlaceDetailEntity> getDetailPlace(@Query("placeid") String place_id);
}
