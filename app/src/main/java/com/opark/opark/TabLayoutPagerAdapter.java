package com.opark.opark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;

public class TabLayoutPagerAdapter  extends FragmentPagerAdapter {

    private FragmentManager tablayoutfragman;
    private Context mContext;
    AllOfferFragment allOfferFragment;
    Fragment fragment;

    public TabLayoutPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabLayoutPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                allOfferFragment = new AllOfferFragment();
                fragment = allOfferFragment;
//                executeFragmentTransaction(fragment);
                return allOfferFragment;
            case 1:

                return new CategoriesOfferFragment();
            case 2:
                return new BrandsFragment();
            default:
                return new AllOfferFragment();
        }
//        if (position == 0) {
//            Log.d("tab", "getItem: 1st");
//            return new AllOfferFragment();
//        }
//        else if (position == 1){
//            Log.d("tab", "getItem: 2nd");
//            return new CategoriesOfferFragment();
//
//        } else {
//            return new BrandsFragment();
//        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Categories";
            case 2:
                return "Brands";
            default:
                return null;
        }
    }


//    private void executeFragmentTransaction(Fragment frag) {
//        if ((frag != null) ) {
//
//            tablayoutfragman= getAc
//            navFragmentManager.beginTransaction()
//                    .replace(R.id.nav_view_selection_container, frag)
//                    .addToBackStack(null)
//                    .commit();
//            Log.d(TAG, "executing fragment transaction , added to backstack");
//            mapContainer.setVisibility(View.GONE);
//            currentFragment = frag;
//            Log.d(TAG, "executeFragmentTransaction: currentFragment is " + currentFragment);
//        }
//        else{
//            Log.d(TAG, "same fragment");
//            return;
//        }
//    }

}

