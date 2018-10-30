package com.opark.opark;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InsufficientPointsDialog extends Dialog implements
        android.view.View.OnClickListener{

    public Context mContext;
    public TextView closeButton;

    public InsufficientPointsDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insufficient_points_dialog);
        closeButton = findViewById(R.id.close_dialog);

        closeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
