package com.talshavit.groupbuyproject.Signup_Login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.talshavit.groupbuyproject.R;

public class StartActivity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        initViews();
    }

    private void initViews() {
        tableLayout.addTab(tableLayout.newTab().setText("התחברות"));
        tableLayout.addTab(tableLayout.newTab().setText("הרשמה"));
        tableLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager(), tableLayout.getTabCount());
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

    private void findViews() {
        tableLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
}