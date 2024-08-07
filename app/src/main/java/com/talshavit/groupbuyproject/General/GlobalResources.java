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
    public static int limitAmount = 0;
    public static double limitPercent;
    public static double orderPrice = 0.0;
    public static int countForShowingDialog = 0;
    public static int countForShowingDialogCompletion = 0;
    public static boolean isSwitchForCompleteOrder = true;
    public static ArrayList<Item>[] allItemsByCategories;

    public static void initCities() {
        City city1 = new City("תל אביב", new LatLng(32.0853, 34.7818));
        city1.points.put("אוניברסיטת תל אביב", new LatLng(32.1093, 34.8554));
        city1.points.put("דיזינגוף סנטר", new LatLng(32.0753, 34.7747));

        City city2 = new City("ירושלים", new LatLng(31.7683, 35.2137));
        city2.points.put("הכותל המערבי", new LatLng(31.7767, 35.2345));
        city2.points.put("גן סאקר", new LatLng(31.7814, 35.2050));

        City city3 = new City("באר שבע", new LatLng(31.2520, 34.7915));
        city3.points.put("אוניברסיטת בן-גוריון בנגב", new LatLng(31.2622, 34.8013));
        city3.points.put("העיר העתיקה", new LatLng(31.2439, 34.7931));

        City city4 = new City("חיפה", new LatLng(32.7940, 34.9896));
        city4.points.put("הגנים הבהאיים", new LatLng(32.8151, 34.9896));
        city4.points.put("חוף דדו", new LatLng(32.7940, 34.9620));

        allCities.add(city1);
        allCities.add(city2);
        allCities.add(city3);
        allCities.add(city4);
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
