package com.talshavit.groupbuyproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.talshavit.groupbuyproject.Fragments.CartFragment;
import com.talshavit.groupbuyproject.Fragments.HistoryFragment;
import com.talshavit.groupbuyproject.Fragments.HomeFragment;
import com.talshavit.groupbuyproject.Fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private SearchFragment searchFragment;
    private ImageView menu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView user_name, time_of_the_day;
//    private View headerView;


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
                        GlobalResources.replaceFragment(getSupportFragmentManager(), homeFragment);
                        break;
                    case 2:
                        GlobalResources.replaceFragment(getSupportFragmentManager(), new HistoryFragment());
                        break;
                    case 3:
                        if (isPaid) {
                            cartFragment = new CartFragment();
                            isPaid = false;
                        }
                        GlobalResources.replaceFragment(getSupportFragmentManager(), cartFragment);
                        break;
                    case 4:
                        GlobalResources.replaceFragment(getSupportFragmentManager(), searchFragment);
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
        onMenu();
        onNavigation();
    }

    private void onNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_deals:
                    openDialog();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_limit_order, null);
        builder.setView(dialogView);

        TextInputEditText inputAmount = dialogView.findViewById(R.id.edit_text_input_amount);

        builder.setTitle("")
                .setPositiveButton("אישור", (dialog, id) -> {
                    String amount = inputAmount.getText().toString();
                    if (amount != null && !amount.isEmpty()) {
                        int limit = Integer.parseInt(amount);
                        GlobalResources.setLimitAmount(limit);
                        GlobalResources.limitPercent = limit * 0.1;
                    }
                })
                .setNegativeButton("ביטול", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    private void onMenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });
    }

    private void findviews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.user_name);
        time_of_the_day = headerView.findViewById(R.id.time_of_the_day);
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