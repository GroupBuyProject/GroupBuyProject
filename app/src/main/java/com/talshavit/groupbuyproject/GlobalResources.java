package com.talshavit.groupbuyproject;

import android.util.Log;
import android.view.View;

import com.talshavit.groupbuyproject.Helpers.ApiService;
import com.talshavit.groupbuyproject.Helpers.RetrofitClient;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalResources {
    public static Cart cart = new Cart();
    public static ArrayList<Item> items;


    public static void initItems() {
        ApiService apiService;
        items = new ArrayList<>();
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        getAllMarkets(apiService);
    }

    public static void getAllMarkets(ApiService apiService) {
        apiService.getAllItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    items = (ArrayList<Item>) response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
            }
        });
    }

}
