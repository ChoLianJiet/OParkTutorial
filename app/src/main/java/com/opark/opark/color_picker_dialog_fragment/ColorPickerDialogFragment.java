package com.opark.opark.color_picker_dialog_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.opark.opark.R;

public class ColorPickerDialogFragment extends DialogFragment {

    public ColorPickerDialogFragment() {
    }


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.profile_nav_fragment, container, false);






        return v;
    }


}
