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
import android.widget.TextView;
import android.widget.Toast;

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;

public class CartFragment extends Fragment {

    private Cart cart;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;
    private AppCompatButton checkout;
    private TextView totalAmount;

    private RecyclerView.AdapterDataObserver dataObserver;


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
        registerDataObserver();
    }

    private void initView() {
        initAdapter(recyclerViewItem, itemsAdapterView);
        onCheckOut();
        setTotalAmount();
    }

    private void onCheckOut() {
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = calcPrice();
                if (cart.getItems().size() != 0)
                    GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new MapFragment(price));
                else
                    Toast.makeText(getContext(), "איו פירטים בעגלה", Toast.LENGTH_SHORT).show();
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

    private void setTotalAmount() {
        totalAmount.setText("₪" + calcPrice());
    }

    private void registerDataObserver() {
        dataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setTotalAmount();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setTotalAmount();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                setTotalAmount();
            }
        };

        itemsAdapterView.registerAdapterDataObserver(dataObserver);
    }

    private void findViews(View view) {
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        itemsAdapterView = new ItemsAdapterView(getContext(), cart.items, "CartFragment", -1);
        checkout = view.findViewById(R.id.checkout);
        totalAmount = view.findViewById(R.id.totalAmount);
    }


    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}