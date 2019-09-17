package com.prag.stickertabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.prag.stickertabs.Adapter.ViewPagerAdapter;
import com.prag.stickertabs.Fragments.Category1;
import com.prag.stickertabs.Fragments.Category2;
import com.prag.stickertabs.Fragments.Category3;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();


    }

    private void initViews() {
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Category1(),"Category 1");
        viewPagerAdapter.addFragments(new Category2(),"Category 2");
        viewPagerAdapter.addFragments(new Category3(),"Category 3");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
