package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.Models.Category;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Item;

import java.util.ArrayList;

public class SalesFragment extends Fragment {

    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewSales;
    private ArrayList<Item> saleItems;
    private AutoCompleteTextView category;
    private String[] allCategories;
    private TextView noItemsTxt;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initViews();
    }

    private void initViews() {
        initSaleArray();
        itemsAdapterView = new ItemsAdapterView(getContext(), saleItems, "AllSalesItems", -1);
        initAdapter(recyclerViewSales, itemsAdapterView);
        initCategory();
    }

    private void initSaleArray() {
        saleItems = new ArrayList<>();
        for (int i = 0; i < GlobalResources.items.size(); i++) {
            Item item = GlobalResources.items.get(i);
            if (Double.parseDouble(item.getSale()) > 0.0) {
                saleItems.add(item);
            }
        }
    }

    private void initCategory() {
        ArrayList<String> allCategoriesList = new ArrayList<>();
        allCategoriesList.add("הכל");
        allCategoriesList.add("פירות וירקות");
        allCategoriesList.add("מוצרי חלב וביצים");
        allCategoriesList.add("בשר עוף ודגים");
        allCategoriesList.add("לחמים");
        allCategoriesList.add("יין אלכוהול וטבק");
        allCategoriesList.add("משקאות קלים");
        allCategoriesList.add("סלטים ונקניקים");
        allCategoriesList.add("מוצרי אפיה ושימורים");
        allCategoriesList.add("חטיפים, עוגות ועוגיות");
        allCategoriesList.add("דגני בוקר");
        allCategoriesList.add("פיצוחים, תבלינים ופירות יבשים");
        allCategoriesList.add("מוצרי ניקיון וחד פעמי");
        allCategoriesList.add("פארם ותינוקות");

        allCategories = allCategoriesList.toArray(new String[0]);

        initSpinnerAdapter(category, allCategories);
    }

    private void initSpinnerAdapter(AutoCompleteTextView autoCompleteTextView, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = (String) adapterView.getItemAtPosition(i);
                Category category1 = null;

                switch (selectedCategory) {
                    case "הכל":
                        category1 = null;
                        break;
                    case "פירות וירקות":
                        category1 = Category.FruitsAndVegetables;
                        break;
                    case "מוצרי חלב וביצים":
                        category1 = Category.DairyAndEggs;
                        break;
                    case "בשר עוף ודגים":
                        category1 = Category.MeatPoultryAndFish;
                        break;
                    case "לחמים":
                        category1 = Category.Bread;
                        break;
                    case "יין אלכוהול וטבק":
                        category1 = Category.WineAlcoholAndTobacco;
                        break;
                    case "משקאות קלים":
                        category1 = Category.Drinks;
                        break;
                    case "סלטים ונקניקים":
                        category1 = Category.SaladsAndDeli;
                        break;
                    case "מוצרי אפיה ושימורים":
                        category1 = Category.BakingProductsAndCannedGoods;
                        break;
                    case "חטיפים, עוגות ועוגיות":
                        category1 = Category.SnacksCakesAndCookies;
                        break;
                    case "דגני בוקר":
                        category1 = Category.Cereal;
                        break;
                    case "פיצוחים, תבלינים ופירות יבשים":
                        category1 = Category.NutsSpicesAndDriedFruits;
                        break;
                    case "מוצרי ניקיון וחד פעמי":
                        category1 = Category.CleaningProductsAndDisposables;
                        break;
                    case "פארם ותינוקות":
                        category1 = Category.PharmacyAndBabyProducts;
                        break;
                }
                itemsAdapterView.filterByCategory(category1, noItemsTxt);
            }
        });
    }

    private void findViews(View view) {
        category = view.findViewById(R.id.category);
        recyclerViewSales = view.findViewById(R.id.recyclerViewSales);
        noItemsTxt = view.findViewById(R.id.noItemsTxt);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}