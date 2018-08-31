package com.opark.opark.login_auth;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.opark.opark.R;

public class ViewPagerMainHolder extends AppCompatActivity{
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_viewpager);

        viewPager = (ViewPager) findViewById(R.id.m_view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    public void goToNextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }





}
