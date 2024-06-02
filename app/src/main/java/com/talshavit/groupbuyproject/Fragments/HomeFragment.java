package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.talshavit.groupbuyproject.ApiService;
import com.talshavit.groupbuyproject.Helpers.GridViewAdapter;
import com.talshavit.groupbuyproject.Market;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.RetrofitClient;
import com.talshavit.groupbuyproject.models.ArrayListCategory;
import com.talshavit.groupbuyproject.models.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

   private ApiService apiService;
   private List<Market> markets;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markets = new ArrayList<>();
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        getAllMarkets(apiService);
    }

    private void getAllMarkets(ApiService apiService) {
        apiService.getAllMarkets().enqueue(new Callback<List<Market>>() {
            @Override
            public void onResponse(Call<List<Market>> call, Response<List<Market>> response) {
                if (response.isSuccessful()) {
                    markets = response.body();
                    loadFirstMarketImage(getView());
                }
            }

            @Override
            public void onFailure(Call<List<Market>> call, Throwable t) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = view.findViewById(R.id.gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), new ArrayListCategory().setArrayList());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        CategoriesModel model = (CategoriesModel) parent.getItemAtPosition(position);
    }

    private void loadFirstMarketImage(View view) {
        if (!markets.isEmpty()) {
            ShapeableImageView imageView = view.findViewById(R.id.img);
            String image = markets.get(4).getImg();
            Glide.with(this).load(image).into(imageView);
        }
    }
}