package com.opark.opark.login_auth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.opark.opark.OtherInfoFragment;
import com.opark.opark.login_auth.PhoneAuth;
import com.opark.opark.NameEmailFragment;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }




    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new EmailLogin();
        } else {
            return new PhoneAuth();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}