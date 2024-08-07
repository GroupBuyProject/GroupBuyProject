package com.talshavit.groupbuyproject.General;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.talshavit.groupbuyproject.Helpers.Retrofit.ApiService;
import com.talshavit.groupbuyproject.Helpers.Retrofit.RetrofitClient;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.City;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.models.User;
import com.talshavit.groupbuyproject.CityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GlobalResources {
    public static Cart cart = new Cart();
    public static ArrayList<Item> items;
    public static User user = new User();
    public static ArrayList<City> allCities = new ArrayList<>();
    public static CityManager cityManager = new CityManager();
    public static int limitAmount = 0;
    public static double limitPercent;
    public static double orderPrice = 0.0;
    public static int countForShowingDialog = 0;
    public static int countForShowingDialogCompletion = 0;
    public static boolean isSwitchForCompleteOrder = true;
    public static ArrayList<Item>[] allItemsByCategories;
    public static int selectedCardPosition = -1;

    public static void initCities() {
        allCities = cityManager.getCities();
    }

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

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void setLimitAmount(int amount) {
        limitAmount = amount;
    }

    public static void setUser(User userObject) {
        user = userObject;
    }

}
