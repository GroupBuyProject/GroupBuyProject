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
import android.widget.ImageButton;

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.SpecificOrderFromHistory.SpecificOrderFromHistoryAdapter;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Order;

public class OrderFragment extends Fragment {
    private Order order;
    private RecyclerView recyclerView;
    private SpecificOrderFromHistoryAdapter itemInHistoryAdapter;
    private AppCompatTextView textStatus;
    private ImageButton backButton;

    public OrderFragment() {
    }

    public OrderFragment(Order order) {
        this.order = order;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initViews();
    }

    private void initViews() {
        itemInHistoryAdapter = new SpecificOrderFromHistoryAdapter(getContext(), order.getCopiedCart().items);
        initAdapter(recyclerView, itemInHistoryAdapter);
        onStatusText();
        onBackButton();
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void onStatusText() {
        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new OrderStatusFragment());
            }
        });
    }

    private void onBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalResources.backToPrevFragment(requireActivity().getSupportFragmentManager());
            }
        });
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        textStatus = view.findViewById(R.id.textStatus);
        backButton = view.findViewById(R.id.backButton);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}