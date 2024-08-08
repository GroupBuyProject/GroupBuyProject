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

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.Models.Category;
import com.talshavit.groupbuyproject.Helpers.GridViewAdapter;
import com.talshavit.groupbuyproject.Models.Item;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.ArrayListCategory;
import com.talshavit.groupbuyproject.Models.CategoriesModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterListByCategory();
    }

    private void filterListByCategory() {
        ArrayList<Item> newItem = GlobalResources.items;
        int size = Category.values().length;
        GlobalResources.allItemsByCategories = new ArrayList[size];
        for (int i = 0; i < GlobalResources.allItemsByCategories.length; i++) {
            GlobalResources.allItemsByCategories[i] = new ArrayList<>();
        }
        for (int i = 0; i < newItem.size(); i++) {
            if (newItem.get(i).getCategory().equals(Category.FruitsAndVegetables)) {
                GlobalResources.allItemsByCategories[0].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.DairyAndEggs)) {
                GlobalResources.allItemsByCategories[1].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.MeatPoultryAndFish)) {
                GlobalResources.allItemsByCategories[2].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.Bread)) {
                GlobalResources.allItemsByCategories[3].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.WineAlcoholAndTobacco)) {
                GlobalResources.allItemsByCategories[4].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.Drinks)) {
                GlobalResources.allItemsByCategories[5].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.SaladsAndDeli)) {
                GlobalResources.allItemsByCategories[6].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.BakingProductsAndCannedGoods)) {
                GlobalResources.allItemsByCategories[7].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.SnacksCakesAndCookies)) {
                GlobalResources.allItemsByCategories[8].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.Cereal)) {
                GlobalResources.allItemsByCategories[9].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.NutsSpicesAndDriedFruits)) {
                GlobalResources.allItemsByCategories[10].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.CleaningProductsAndDisposables)) {
                GlobalResources.allItemsByCategories[11].add(newItem.get(i));
            }
            if (newItem.get(i).getCategory().equals(Category.PharmacyAndBabyProducts)) {
                GlobalResources.allItemsByCategories[12].add(newItem.get(i));
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
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new AllItemsFragment(GlobalResources.allItemsByCategories[i], listCategory.get(i).getCategoryName(), listCategory.get(i).getImg(), i));
                ((MainActivity) view.getContext()).selectHomeTab();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        CategoriesModel model = (CategoriesModel) parent.getItemAtPosition(position);
    }
}