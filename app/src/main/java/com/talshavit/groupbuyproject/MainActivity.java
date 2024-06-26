package com.talshavit.groupbuyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.talshavit.groupbuyproject.Fragments.CartFragment;
import com.talshavit.groupbuyproject.Fragments.HistoryFragment;
import com.talshavit.groupbuyproject.Fragments.HomeFragment;
import com.talshavit.groupbuyproject.Fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private SearchFragment searchFragment;

    public static boolean isPaid = false;
    private MeowBottomNavigation.ReselectListener reselectListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        initViews();

        GlobalResources.initItems();

        initBottomNav();
        checkIfNavNull();
        onNavigationBar();
    }

    private void onNavigationBar() {
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        GlobalResources.replaceFragment(getSupportFragmentManager(),homeFragment);
                        break;
                    case 2:
                        GlobalResources.replaceFragment(getSupportFragmentManager(), new HistoryFragment());
                        break;
                    case 3:
                        if(isPaid){
                            cartFragment = new CartFragment();
                            isPaid = false;
                        }
                        GlobalResources.replaceFragment(getSupportFragmentManager(),cartFragment);
                        break;
                    case 4:
                        GlobalResources.replaceFragment(getSupportFragmentManager(),searchFragment);
                        break;
                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
            }
        });
    }

    private void initViews() {
        homeFragment = new HomeFragment(bottomNavigation);
        cartFragment = new CartFragment();
        searchFragment = new SearchFragment();
    }

    private void findviews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void checkIfNavNull() {
        bottomNavigation.setOnReselectListener(item -> {
            // Check if the listener is not null before invoking the method
            if (reselectListener != null)
                reselectListener.onReselectItem(item);
        });
    }

    private void initBottomNav() {
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.history));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.search));
    }
}