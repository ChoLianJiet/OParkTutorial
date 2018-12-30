package com.opark.opark.search_view_behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SearchViewBehavior3 extends CoordinatorLayout.Behavior<AppBarLayout> {

    private int distance;

    public SearchViewBehavior3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {


        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            child.animate().cancel();
            distance = 0;
        }
        distance += dy;
        Log.d("offset", "onNestedPreScroll:  " + distance);
        final int height = child.getHeight() > 0 ? (child.getHeight()) : 600/*update this accordingly*/;

    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d("offset", "onNestedScroll: ");

        if (distance > 0 ) {

            Log.d("offset", "onNestedPreScroll: distance > height ");
            child.measure(0,0);
            float maxDistance = child.getTotalScrollRange();
            Log.d("offset", "  \nMaxScrollRange: " + maxDistance + "\nHeight : " + child.getMeasuredHeight() + "Y " + child.getY());

//            hide(child);
        } else if (distance < 0 ) {
            Log.d("offset", "onNestedPreScroll: distance < 0 ");
            show(child);
        }
    }

    private void hide(AppBarLayout view) {
//        view.setVisibility(View.INVISIBLE);// use animate.translateY(height); instead
        view.setExpanded(false);
    }

    private void show(AppBarLayout view) {

        view.setExpanded(true);
//        view.setVisibility(View.VISIBLE);// use animate.translateY(-height); instead
    }

}
