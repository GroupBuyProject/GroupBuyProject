package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.Models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;

public class AllItemsFragment extends Fragment {

    private ArrayList<Item> allItemsByCategories;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;
    private ShapeableImageView imgBackGround;
    private AppCompatTextView title;
    private String categoryName;
    private ImageButton backButton;
    private String img;
    private int category;


    public AllItemsFragment() {
    }

    public AllItemsFragment(ArrayList<Item> allItemsByCategories, String categoryName, String img, int category) {
        this.allItemsByCategories = allItemsByCategories;
        this.categoryName = categoryName;
        this.img = img;
        this.category = category;
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
        Glide.with(getContext()).load(img).into(imgBackGround);
        initAdapter(recyclerViewItem, itemsAdapterView);
        title.setText(categoryName);
        onBackButton();
    }

    private void onBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void findViews(View view) {
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        imgBackGround = view.findViewById(R.id.imgBackGround);
        itemsAdapterView = new ItemsAdapterView(getContext(), allItemsByCategories, "AllItemsFragment",category);
        title = view.findViewById(R.id.title);
        backButton = view.findViewById(R.id.backButton);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}