package com.talshavit.groupbuyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talshavit.groupbuyproject.Fragments.CartFragment;
import com.talshavit.groupbuyproject.Fragments.ChangePasswordFragment;
import com.talshavit.groupbuyproject.Fragments.HistoryFragment;
import com.talshavit.groupbuyproject.Fragments.HomeFragment;
import com.talshavit.groupbuyproject.Fragments.SalesFragment;
import com.talshavit.groupbuyproject.Fragments.SearchFragment;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnCoinsUpdateListener;
import com.talshavit.groupbuyproject.Signup_Login.StartActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements OnCoinsUpdateListener {

    private MeowBottomNavigation bottomNavigation;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private HistoryFragment historyFragment;
    private SalesFragment salesFragment;
    private SearchFragment searchFragment;
    private ImageView menu, coins;
    private AppCompatTextView coinsTxt;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView user_name, time_of_the_day;
    private ShapeableImageView imageTime;

    public static boolean isPaid = false;
    private MeowBottomNavigation.ReselectListener reselectListener;
    private Menu navMenu;
    private MenuItem coinsMenuItem;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
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
        //coinsMenuItem.setTitle("23.0 מטבעות"); //לשנות אחרי שהמסך מקבל נתונים
        animateToCoin(coins);
        homeFragment = new HomeFragment(bottomNavigation);
        historyFragment = new HistoryFragment();
        salesFragment = new SalesFragment();
        cartFragment = new CartFragment();
        //searchFragment = new SearchFragment();
        onMenu();
        setCoins();
        onNavigation();
    }

    private void setCoins() {
        double virtualCurrencies = GlobalResources.user.getVirtualCurrencies();
        String formattedValue = String.format("%.2f", virtualCurrencies);
        coinsTxt.setText(formattedValue + " מטבעות");
    }

    private void onNavigation() {
        checkCurrentTime();
        user_name.setText("שלום " + GlobalResources.user.getName());

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_deals:
                    openDialog();
                    break;
                case R.id.complete_order:
                    onCompleteOrder();
                    break;
                case R.id.privacy_policy:
                    onPrivacyPolicy();
                    break;
                case R.id.terms:
                    onTerms();
                    break;
                case R.id.change_password:
                    onChangePassword();
                    break;
                case R.id.delete_user:
                    onDeleteAccount();
                    break;
                case R.id.log_out:
                    openStartActivity();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
    }

    private void onCompleteOrder() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_for_complete_order, null);
        dialog.setContentView(dialogView);

        Switch switch_complete_order = dialogView.findViewById(R.id.switch_complete_order);
        if(GlobalResources.isSwitchForCompleteOrder){
            switch_complete_order.setChecked(true);
        }
        else{
            switch_complete_order.setChecked(false);
        }
        onSwitch(switch_complete_order);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = 450;
            params.height = 150;
            window.setAttributes(params);
        }
    }

    private void onSwitch(Switch switch_complete_order) {
        switch_complete_order.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d("lala", GlobalResources.isSwitchForCompleteOrder+"");
                if(!isChecked){
                    GlobalResources.isSwitchForCompleteOrder = false;
                }
                else
                    GlobalResources.isSwitchForCompleteOrder = true;
            }
        });
    }

    private void onChangePassword() {
        GlobalResources.replaceFragment(getSupportFragmentManager(), new ChangePasswordFragment());
    }

    private void onDeleteAccount() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("מחיקת משתמש");
        builder.setMessage("האם את/ה בטוח/ה שתרצה/י למחוק את המשתמש?");
        builder.setCancelable(false);
        positiveButton(builder);
        negativeButton(builder);
        builder.show();
    }

    private void negativeButton(android.app.AlertDialog.Builder builder) {
        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void positiveButton(android.app.AlertDialog.Builder builder) {
        builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (user != null) {
                    String userID = user.getUid();
                    DatabaseReference userToDelete = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    userToDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseAuth.signOut();
                                            openStartActivity();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
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
        coins = findViewById(R.id.coins);
        coinsTxt = findViewById(R.id.coinsTxt);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();
        coinsMenuItem = navMenu.findItem(R.id.coins);
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

    public void selectCartTab() {
        bottomNavigation.show(3, true); // Cart ID 3
    }

    public void selectHomeTab() {
        bottomNavigation.show(1, true); // Cart ID 1
    }

    private void animateToCoin(View imageView) {
        imageView.animate().setDuration(1000)
                .rotationYBy(360f)
                .start();
    }

    private void openStartActivity() {
        Intent myIntent = new Intent(this, StartActivity.class);
        startActivity(myIntent);
    }

    private void onPrivacyPolicy() {
        String htmlContent = loadHtmlFromAsset("privacy_policy.html");

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent));
        }
        materialAlertDialogBuilder.setPositiveButton("סגור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //Close the dialog
            }
        });

        materialAlertDialogBuilder.setCancelable(false);
        materialAlertDialogBuilder.show();
    }

    private String loadHtmlFromAsset(String filename) {
        String textFile = "";
        try {
            InputStream inputStream = this.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            textFile = new String(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFile;
    }

    private void onTerms() {
        String htmlContent = loadHtmlFromAsset("terms_of_conditions.html");
        dialogFunc(htmlContent);
    }

    private void dialogFunc(String htmlContent) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(htmlContent));
        }
        materialAlertDialogBuilder.setPositiveButton("סגור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //Close the dialog
            }
        });

        materialAlertDialogBuilder.setCancelable(false);
        materialAlertDialogBuilder.show();
    }

    @Override
    public void onCoinsUpdated(double newCoinValue) {
        animateToCoin(coins);
        String formattedValue = String.format("%.2f", newCoinValue);
        coinsTxt.setText(formattedValue + " מטבעות");
        coinsMenuItem.setTitle(formattedValue + " מטבעות");
    }
}