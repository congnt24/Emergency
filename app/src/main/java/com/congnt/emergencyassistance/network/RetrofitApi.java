package com.congnt.emergencyassistance.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by congnt24 on 04/10/2016.
 */

public interface RetrofitApi {
    @GET("selfdefense")
    Call<ResponseBody> getSelfDefense();
}
