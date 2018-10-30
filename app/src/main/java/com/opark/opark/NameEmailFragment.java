package com.opark.opark;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.opark.opark.RegisterScroll;
import com.opark.opark.R;

public class NameEmailFragment extends Fragment {

    private TextInputLayout inputLayout;
    private View btnNext;
    private int position;

    private OnNameEmailChangedListener callback;

    // Container Activity must implement this interface
    public interface OnNameEmailChangedListener {
        public void onNameEmailChanged(int position, String name);
    }

    public static Fragment getInstance(int position) {
        NameEmailFragment f = new NameEmailFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.name_email_fragment, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputLayout = (TextInputLayout) view.findViewById(R.id.username_field);
        btnNext = view.findViewById(R.id.btn_next);

        position = getArguments().getInt("POSITION");
        Log.i("Fragment", "" + position);

        if (position == 1) {
            inputLayout.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            inputLayout.setHint("Your Email");
        }

        btnNext.setOnClickListener(onNextListener());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callback = (OnNameEmailChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    private View.OnClickListener onNextListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayout.getEditText().getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please Input your Name", Toast.LENGTH_SHORT).show();
                } else {
                    // Notify the parent activity of selected item
                    callback.onNameEmailChanged(position, inputLayout.getEditText().getText().toString());

                    //go to next page
//                    ((RegisterScroll) getActivity()).goToNextPage();
                }
            }
        };
    }
}