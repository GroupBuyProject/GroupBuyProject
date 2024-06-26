package com.talshavit.groupbuyproject.Helpers.Retrofit;

import com.talshavit.groupbuyproject.models.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/api/markets")
    Call<List<Item>> getAllItems();

    @POST("/api/markets")
    Call<Item> addItem(@Body Item item);
}
