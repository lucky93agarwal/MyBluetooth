package com.mslji.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.mslji.mybluetooth.Adapter.TabsAdapter;
import com.mslji.mybluetooth.Database.SessionManager;
import com.mslji.mybluetooth.R;
import com.mslji.mybluetooth.databinding.ActivityTestDetailsBinding;

public class TestDetailsActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.top_one_two,
            R.drawable.top_two_one,
            R.drawable.bagnew
    };
    private String[] titles = {
            "Result",
            "Form",
            "Report"
    };

    public String number,id,date,taken;
    SessionManager sessionManager;


    ActivityTestDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sessionManager = new SessionManager(this);





        number = getIntent().getStringExtra("num");
        id = getIntent().getStringExtra("id");
        date = getIntent().getStringExtra("date");
        taken = getIntent().getStringExtra("taken");
        Log.i("karnikalucky","TestDetailsActivity number =  "+number);
        Log.i("karnikalucky","TestDetailsActivity date =  "+date);
        Log.i("karnikalucky","TestDetailsActivity id =  "+id);
        Log.i("karnikalucky","TestDetailsActivity taken =  "+taken);

        sessionManager.setTNum(number);
        sessionManager.setTId(id);
        sessionManager.setTDate(date);
        sessionManager.setTTaken(taken);

        Log.i("karnikalucky","TestDetailsActivity id =  "+id);





        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Result"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tab.setText(titles[tab.getPosition()]);

                if(position == 0){
                    Log.d("WhatisNuameLcky", "postion 0 = = " + String.valueOf(position));
                    binding.tabLayout.getTabAt(1).setIcon(R.drawable.startnewicon);
                    binding.tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
                    binding.tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);
                    int tabIconColor = ContextCompat.getColor(TestDetailsActivity.this, R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    binding.tabLayout.setTabTextColors(ContextCompat.getColorStateList(TestDetailsActivity.this, R.color.tabSelectedIconColor));

                }else if (position == 1){
                    binding.tabLayout.getTabAt(1).setIcon(R.drawable.startnewicon);
                    binding.tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
                    binding.tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);


                    int tabIconColor = ContextCompat.getColor(TestDetailsActivity.this, R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    binding.tabLayout.setTabTextColors(ContextCompat.getColorStateList(TestDetailsActivity.this, R.color.tabSelectedIconColor));
                }else if (position == 2){
                    binding.tabLayout.getTabAt(1).setIcon(R.drawable.startnewicon);
                    binding.tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
                    binding.tabLayout.getTabAt(2).setIcon(R.drawable.bagnewnew);


                    int tabIconColor = ContextCompat.getColor(TestDetailsActivity.this, R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    binding.tabLayout.setTabTextColors(ContextCompat.getColorStateList(TestDetailsActivity.this, R.color.tabSelectedIconColor));
                }

                Log.d("WhatisNuameLcky", "postion  = = " + String.valueOf(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(TestDetailsActivity.this, R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                tab.setText("");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupTabIcons();

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(tabsAdapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setupTabIcons() {
        Log.d("WhatisNuameLcky", "setupTabIcons = = " );

        binding.tabLayout.getTabAt(1).setIcon(R.drawable.startnewicon);
        binding.tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
        binding.tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);
    }
}