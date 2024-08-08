package com.talshavit.groupbuyproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.R;

public class OrderStatusFragment extends Fragment {

    private ImageButton backButton;

    public OrderStatusFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.backButton);
        onBackButton();
    }

    private void onBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalResources.backToPrevFragment(requireActivity().getSupportFragmentManager());
            }
        });
    }
}