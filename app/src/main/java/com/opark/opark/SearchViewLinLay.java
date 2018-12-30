package com.opark.opark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.opark.opark.search_view_behavior.SearchViewBehavior2;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchViewLinLay extends LinearLayout implements CoordinatorLayout.AttachedBehavior {


    @Bind(R.id.search_textview)
    EditText search;



    public SearchViewLinLay(Context context) {
        super(context);
    }

    public SearchViewLinLay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewLinLay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public SearchViewLinLay(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setTextSize(float size) {
        search.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }


    public void bindTo(String name) {
        Log.d("scroll", "Linlay bindTo: ");
        this.search.setText(name);
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return new SearchViewBehavior2(getContext());
    }

}
