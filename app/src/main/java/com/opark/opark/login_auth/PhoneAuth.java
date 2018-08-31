package com.opark.opark.login_auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opark.opark.R;

/** * Created by Supriya on 9/11/2016. */
public class PhoneAuth extends Fragment {
    @Nullable    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.phoneauth_page,null);
        return v;
    }
}