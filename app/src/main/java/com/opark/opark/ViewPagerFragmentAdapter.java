package com.opark.opark;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.opark.opark.NameEmailFragment;
import com.opark.opark.OtherInfoFragment;



public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position ==2 ){
            return new OtherInfoFragment();
        } else {
            return NameEmailFragment.getInstance(position);
        }

//        return null;
    }

    @Override
    public int getCount(){
        return 3;
    }
}
