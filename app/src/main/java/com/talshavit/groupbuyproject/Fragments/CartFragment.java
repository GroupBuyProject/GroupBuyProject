package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Category;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private Cart cart;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;

    private AppCompatButton checkout;


    public CartFragment() {
        this.cart = GlobalResources.cart;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initView();
    }

    private void initView() {
        initAdapter(recyclerViewItem, itemsAdapterView);
        onCheckOut();
    }

    private void onCheckOut() {
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = calcPrice();
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new DetailsUserForOrderFragment());
                //GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new CheckoutFragment(price));
            }
        });
    }

    private double calcPrice() {
        double price = 0.0;
        for (int i = 0; i < cart.getItems().size(); i++) {
            price += Double.parseDouble(cart.getItems().get(i).getPrice()) * cart.getItems().get(i).getCount();
        }
        return price;
    }

    private void findViews(View view) {
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        itemsAdapterView = new ItemsAdapterView(getContext(), cart.items, "CartFragment");
        checkout = view.findViewById(R.id.checkout);
    }


    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}