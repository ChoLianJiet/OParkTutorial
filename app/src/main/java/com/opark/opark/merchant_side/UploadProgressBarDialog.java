package com.opark.opark.merchant_side;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.opark.opark.R;

public class UploadProgressBarDialog extends DialogFragment {

    public static ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_progress_bar_dialog,container,false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);

        bindViews(view);

        return view;
    }



    private void bindViews(View view){
        progressBar = view.findViewById(R.id.upload_progress);

    }

}



