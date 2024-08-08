package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.History.HistoryAdapter;
import com.talshavit.groupbuyproject.R;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter orderAdapter;

    public HistoryFragment() {
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
        orderAdapter = new HistoryAdapter(requireActivity().getSupportFragmentManager(), GlobalResources.user.getHistories());
        initAdapter(recyclerView, orderAdapter);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}