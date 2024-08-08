package com.talshavit.groupbuyproject.Checkout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Signup_Login.MyPageAdapter;


public class CheckoutTabFragment extends Fragment {

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ImageButton checkoutBackButton;
    private double price;


    public CheckoutTabFragment(double price) {
        this.price = price;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initTab();
        initViews();
    }

    private void initViews() {
        onBackButton();
    }

    private void onBackButton() {
        checkoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    private void initTab() {
        tableLayout.addTab(tableLayout.newTab().setText("פרטי אשראי"));
        tableLayout.addTab(tableLayout.newTab().setText("אשראי קיים"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyCheckoutAdapter adapter = new MyCheckoutAdapter(requireActivity().getSupportFragmentManager(), tableLayout.getTabCount(), price);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void findViews(View view) {
        tableLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        checkoutBackButton = view.findViewById(R.id.checkoutBackButton);
    }
}