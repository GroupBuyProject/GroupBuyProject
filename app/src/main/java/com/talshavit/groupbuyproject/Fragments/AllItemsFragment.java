package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.models.Category;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;

public class AllItemsFragment extends Fragment {

    private ArrayList<Item> allItemsByCategories;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;

    private AppCompatTextView title;

    private String categoryName;

    public AllItemsFragment() {
    }

    public AllItemsFragment(ArrayList<Item> allItemsByCategories, String categoryName) {
        this.allItemsByCategories = allItemsByCategories;
        this.categoryName = categoryName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initView();
    }

    private void initView() {
        initAdapter(recyclerViewItem, itemsAdapterView);
        title.setText(categoryName);
    }

    private void findViews(View view) {
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        itemsAdapterView = new ItemsAdapterView(getContext(), allItemsByCategories, "AllItemsFragment");
        title = view.findViewById(R.id.title);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}