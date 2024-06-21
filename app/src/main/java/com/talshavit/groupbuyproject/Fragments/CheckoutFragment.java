package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Cart;
import com.talshavit.groupbuyproject.models.Order;

import java.time.LocalDate;

public class CheckoutFragment extends Fragment {

    private TextInputEditText etCardNumber, etExpiryDate, etCVV;
    private MaterialButton btnPay;

    private ItemsAdapterView itemsAdapterView;

    private double price;

    public CheckoutFragment(double price) {
        this.price = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initViews();
    }

    private void initViews() {
        itemsAdapterView = new ItemsAdapterView(getContext(), GlobalResources.items, "");
        onPayBtn();
    }

    private void onPayBtn() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order(GlobalResources.cart, price);
                GlobalResources.user.addHistory(order);
                MainActivity.isPaid = true;
                GlobalResources.cart = new Cart();
                itemsAdapterView.changeCount();
            }
        });
    }

    private void findViews(View view) {
        btnPay = view.findViewById(R.id.btnPay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }
}