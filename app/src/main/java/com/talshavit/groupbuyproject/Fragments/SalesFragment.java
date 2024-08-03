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

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Item;

import java.util.ArrayList;

public class SalesFragment extends Fragment {

    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewSales;
    private ArrayList<Item> saleItems;

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
    }

    private void initSaleArray() {
        saleItems = new ArrayList<>();
        for (int i = 0; i < GlobalResources.items.size(); i++) {
            Item item = GlobalResources.items.get(i);
            if(Double.parseDouble(item.getSale()) > 0.0){
                saleItems.add(item);
            }
        }
    }

    private void findViews(View view) {
        recyclerViewSales = view.findViewById(R.id.recyclerViewSales);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}