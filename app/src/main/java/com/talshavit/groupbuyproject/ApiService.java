package com.talshavit.groupbuyproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/api/markets")
    Call<List<Market>> getAllMarkets();

    @POST("/api/markets")
    Call<Market> addMarket(@Body Market market);
}
