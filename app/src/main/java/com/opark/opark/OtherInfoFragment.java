package com.opark.opark;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class OtherInfoFragment extends Fragment {
    private Spinner spinner;
    private TextInputLayout inputLayout;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> genders;
    private View btnNext;

    private String gender;

    private OnInfoChangedListener callback;


    // Container Activity must implement this interface
    public interface OnInfoChangedListener {
        public void onInfoChanged(String age, String gender);
    }



    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstancedState){

        return  inflater.inflate(R.layout.activity_other_info_fragment, null, false);
    }





}
