package com.talshavit.groupbuyproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.talshavit.groupbuyproject.Fragments.CartFragment;
import com.talshavit.groupbuyproject.Fragments.HistoryFragment;
import com.talshavit.groupbuyproject.Fragments.HomeFragment;
import com.talshavit.groupbuyproject.Fragments.SalesFragment;
import com.talshavit.groupbuyproject.Fragments.SearchFragment;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.models.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private HistoryFragment historyFragment;
    private SalesFragment salesFragment;
    private SearchFragment searchFragment;
    private ImageView menu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView user_name, time_of_the_day;
    private ShapeableImageView imageTime;

    public static boolean isPaid = false;
    private MeowBottomNavigation.ReselectListener reselectListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalResources.initItems();

        findviews();
        initViews();

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
                        GlobalResources.replaceFragment(getSupportFragmentManager(), historyFragment);
                        break;
                    case 3:
                        if (isPaid) {
                            cartFragment = new CartFragment();
                            isPaid = false;
                        }
                        GlobalResources.replaceFragment(getSupportFragmentManager(), cartFragment);
                        break;
                    case 4:
                        GlobalResources.replaceFragment(getSupportFragmentManager(), salesFragment);
                        break;
                    case 5:
                        GlobalResources.replaceFragment(getSupportFragmentManager(), new SearchFragment());
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
        historyFragment = new HistoryFragment();
        salesFragment = new SalesFragment();
        cartFragment = new CartFragment();
        //searchFragment = new SearchFragment();
        onMenu();
        onNavigation();
    }

    private void onNavigation() {
        checkCurrentTime();
        user_name.setText("שלום " + GlobalResources.user.getName());

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

    private void checkCurrentTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jerusalem");
        Calendar calendar = Calendar.getInstance(timeZone);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        initTimeInNav(hour);
    }

    private void initTimeInNav(int hour) {
        if (hour >= 5 && hour < 12) {
            time_of_the_day.setText("בוקר טוב");
            imageTime.setImageResource(R.drawable.morning);
        } else if (hour >= 12 && hour < 16) {
            time_of_the_day.setText("צהריים טובים");
            imageTime.setImageResource(R.drawable.noon);
        } else if (hour >= 16 && hour < 19) {
            time_of_the_day.setText("אחר הצהריים טובים");
            imageTime.setImageResource(R.drawable.afternoon);
        } else if (hour >= 19 && hour < 21) {
            time_of_the_day.setText("ערב טוב");
            imageTime.setImageResource(R.drawable.evening);
        } else {
            time_of_the_day.setText("לילה טוב");
            imageTime.setImageResource(R.drawable.night);
        }
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
        imageTime = headerView.findViewById(R.id.imageTime);
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
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.sale));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.search));
    }
}