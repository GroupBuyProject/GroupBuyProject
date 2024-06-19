package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.ApiService;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText editTxtSearchByItem, editTxtSearchByCompany;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViews(View view) {
        itemsAdapterView = new ItemsAdapterView(getContext(), GlobalResources.items, "");
        editTxtSearchByItem = view.findViewById(R.id.editTxtSearchByItem);
        editTxtSearchByCompany = view.findViewById(R.id.editTxtSearchByCompany);
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        initAdapter(recyclerViewItem, itemsAdapterView);
        editTxtSearchByItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textItem = s.toString();
                String textCompany = editTxtSearchByCompany.getText().toString();
                itemsAdapterView.filterByItemAndCompany(textItem, textCompany);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editTxtSearchByCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textCompany = s.toString();
                String textItem = editTxtSearchByItem.getText().toString();
                itemsAdapterView.filterByItemAndCompany(textItem, textCompany);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }
}