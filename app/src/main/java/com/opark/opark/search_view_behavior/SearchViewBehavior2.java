package com.opark.opark.search_view_behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.opark.opark.R;
import com.opark.opark.SearchViewLinLay;

public class SearchViewBehavior2 extends CoordinatorLayout.Behavior<SearchViewLinLay> {

    private static final String TAG = "scroll";
    private Context mContext;

    private int mStartMarginLeft;
    private int mEndMarginLeft;
    private int mMarginRight;
    private int mStartMarginBottom;
    private float mTitleStartSize;
    private float mTitleEndSize;
    private boolean isHide;


    public SearchViewBehavior2(Context mContext) {
        this.mContext = mContext;
    }

    public SearchViewBehavior2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SearchViewBehavior2(Context context, AttributeSet attrs, Context mContext) {
        super(context, attrs);
        this.mContext = mContext;
    }

    public static int getToolbarHeight(Context context) {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        Log.d(TAG, "getToolbarHeight: toolbar height is " + result);
        return result;
    }



    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchViewLinLay child, View dependency){
                return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, SearchViewLinLay child, View dependency){

        initDimen();

        int maxScrollDistance = ((AppBarLayout)dependency).getTotalScrollRange();
        Log.d(TAG, "onDependentViewChanged: ");
        float percentage = Math.abs(dependency.getY()) / (float) maxScrollDistance;

        float childPosition = dependency.getHeight()
                + dependency.getY()
                - child.getHeight()
                - (getToolbarHeight(mContext) - child.getHeight()) * percentage / 2;


        Log.d(TAG, "onDependentViewChanged: Dependency getY" + dependency.getY());
        Log.d(TAG, "onDependentViewChanged: Dependency Height " + dependency.getHeight());
        Log.d(TAG, "onDependentViewChanged: Child height " + child.getHeight());
        Log.d(TAG, "onDependentViewChanged:  StartMargin Bottom " + mStartMarginBottom);

        childPosition = childPosition - mStartMarginBottom * (1f - percentage);


//        Log.d(TAG, "onDependentViewChanged: childPosition" + childPosition);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();


        Log.d(TAG, "");
        if (Math.abs(dependency.getY()) >= maxScrollDistance / 2) {
            float layoutPercentage = (Math.abs(dependency.getY()) - (maxScrollDistance / 2)) / Math.abs(maxScrollDistance / 2);
            lp.leftMargin = (int) (layoutPercentage * mEndMarginLeft) + mStartMarginLeft;
            Log.d(TAG, "\n\n");
            Log.d(TAG, "BEFORE: title start size, height " + child.getHeight() +"width " +  child.getWidth());
            child.setTextSize(getTranslationOffset(mTitleStartSize, mTitleEndSize, layoutPercentage));

            child.measure(0,0);

            Log.d(TAG, "AFTER: title END size, height " + child.getMeasuredHeight()+ "width " +  child.getMeasuredWidth());



        } else {
            lp.leftMargin = mStartMarginLeft;
    }



        lp.rightMargin = mMarginRight;
        child.setLayoutParams(lp);
        child.setY(childPosition);


        Log.d(TAG, "child getY " + child.getY());
        Log.d(TAG, "child getX " + child.getX());


        if (isHide && percentage < 1) {
            Log.d(TAG, "child visibility" + child.getVisibility());
            child.setVisibility(View.VISIBLE);
            isHide = false;

        } else if (!isHide && percentage == 1) {
            child.setVisibility(View.GONE);
            isHide = true;
        }
        return true;
    }




    protected float getTranslationOffset(float expandedOffset, float collapsedOffset, float ratio) {
        Log.d(TAG, "getTranslationOffset:  ");
        float translationResult = expandedOffset + ratio * (collapsedOffset - expandedOffset);
        if (translationResult < collapsedOffset){
            Log.d(TAG, "getTranslationOffset: trans < coll");
            translationResult = expandedOffset;
            Log.d(TAG, "getTranslationOffset: translation result "+ translationResult);
        }



        return translationResult;
    }



   private void initDimen(){
        if(mStartMarginLeft == 0 ){
            Log.d(TAG, "initDimen:  start margin left is 0");
            mStartMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
        }

        if (mEndMarginLeft ==0 ){
            mEndMarginLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_end_margin_left);
        }

        if (mStartMarginBottom ==0){
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin);
        }

        if (mMarginRight ==0 ){
            mMarginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);

        }

        if (mTitleStartSize == 0 ){
            mTitleStartSize = mContext.getResources().getDimensionPixelSize(R.dimen.search_view_text_start_size);
        }

        if(mTitleEndSize == 0 ){
            mTitleEndSize = mContext.getResources().getDimensionPixelSize(R.dimen.search_view_text_end_size);
        }


    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SearchViewLinLay child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {

        Log.d("fling", "onNestedFling:  ");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SearchViewLinLay child, @NonNull View target, float velocityX, float velocityY) {

        Log.d("fling", "onNestedPreFling: ");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
