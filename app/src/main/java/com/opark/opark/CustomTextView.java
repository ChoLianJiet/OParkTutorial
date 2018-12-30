package com.opark.opark;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import com.opark.opark.search_view_behavior.SearchViewBehavior1;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView implements CoordinatorLayout.AttachedBehavior {


    private float mCustomFinalYPosition;
    private float mCustomFinalHeight;

    private float mCustomStartXPosition;
    private float mCustomStartToolbarPosition;
    private float mCustomStartHeight;
    private float mStartPosition;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Used when the layout has a behavior attached via xml (Within the xml file e.g.
        //<app:layout_behavior=".link.to.your.behavior">


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchViewBehavior1);
        mCustomFinalYPosition = a.getDimension(R.styleable.SearchViewBehavior1_finalYPosition, 0);
        mCustomStartXPosition = a.getDimension(R.styleable.SearchViewBehavior1_startXPosition, 0);
        mCustomStartToolbarPosition = a.getDimension(R.styleable.SearchViewBehavior1_startToolbarPosition, 0);
        mCustomStartHeight = a.getDimension(R.styleable.SearchViewBehavior1_startHeight, 0);
        mCustomFinalHeight = a.getDimension(R.styleable.SearchViewBehavior1_finalHeight, 0);

        a.recycle();
    }

    @NonNull
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return new SearchViewBehavior1();
    }
}
