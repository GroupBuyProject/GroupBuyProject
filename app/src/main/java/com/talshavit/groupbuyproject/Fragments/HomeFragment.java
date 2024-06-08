package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.talshavit.groupbuyproject.Helpers.ApiService;
import com.talshavit.groupbuyproject.models.Category;
import com.talshavit.groupbuyproject.Helpers.GridViewAdapter;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Helpers.RetrofitClient;
import com.talshavit.groupbuyproject.models.ArrayListCategory;
import com.talshavit.groupbuyproject.models.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

   private ApiService apiService;
   private List<Item> items;
    private MeowBottomNavigation bottomNavigation;
    private ArrayList<Item>[] allItemsByCategories;

    public HomeFragment() {
    }

    public HomeFragment(MeowBottomNavigation bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new ArrayList<>();
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        getAllMarkets(apiService);
    }

    private void getAllMarkets(ApiService apiService) {
        apiService.getAllItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    items = response.body();
                    filterListByCategory(items);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
            }
        });
    }

    private void filterListByCategory(List<Item> items) {
        int size = Category.values().length;
        allItemsByCategories = new ArrayList[size];
        for(int i=0; i<allItemsByCategories.length; i++){
            allItemsByCategories[i] = new ArrayList<>();
        }
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getCategory().equals(Category.FruitsAndVegetables)){
                allItemsByCategories[0].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.DairyAndEggs)){
                allItemsByCategories[1].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.MeatPoultryAndFish)){
                allItemsByCategories[2].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.Bread)){
                allItemsByCategories[3].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.WineAlcoholAndTobacco)){
                allItemsByCategories[4].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.Drinks)){
                allItemsByCategories[5].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.SaladsAndDeli)){
                allItemsByCategories[6].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.BakingProductsAndCannedGoods)){
                allItemsByCategories[7].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.SnacksCakesAndCookies)){
                allItemsByCategories[8].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.Cereal)){
                allItemsByCategories[9].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.NutsSpicesAndDriedFruits)){
                allItemsByCategories[10].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.CleaningProductsAndDisposables)){
                allItemsByCategories[11].add(items.get(i));
            }
            if(items.get(i).getCategory().equals(Category.PharmacyAndBabyProducts)){
                allItemsByCategories[12].add(items.get(i));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<CategoriesModel> listCategory = new ArrayListCategory().setArrayList();
        GridView gridView = view.findViewById(R.id.gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getContext(), listCategory);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                replaceFragment(new AllItemsFragment(allItemsByCategories[i], listCategory.get(i).getCategoryName(), listCategory.get(i).getImg()));
                bottomNavigation.show(-1, true);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment, null)
                .setReorderingAllowed(true).addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        CategoriesModel model = (CategoriesModel) parent.getItemAtPosition(position);
    }
}