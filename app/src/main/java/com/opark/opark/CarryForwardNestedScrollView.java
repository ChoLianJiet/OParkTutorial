package com.opark.opark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class CarryForwardNestedScrollView extends NestedScrollView implements NestedScrollingParent2 {

    private View currentTarget;
    private final NestedScrollingParentHelper parentHelper;


    public CarryForwardNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parentHelper = new NestedScrollingParentHelper(this);

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        final RecyclerView rv = (RecyclerView) target;


        if ((dy < 0 && isRvScrolledToTop(rv)) || (dy > 0 && !isNsvScrolledToBottom(this))) {
            Log.d("nsv", "onNestedPreScroll: rv scrolled to top nsv not bottom" );

            scrollBy(0, dy);
            consumed[1] = dy;
            return;
        }

        else {
            Log.d("nsv", "onNestedPreScroll: rv scrolled to bottom" );
            stopNestedScroll();
        }

        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(
            @NonNull View child, @NonNull View target, int axes, int type) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type);
    }

    @Override
    public void onNestedPreScroll(
            @NonNull View target, int dx, int dy, @Nullable int[] consumed, int type) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type);
    }

    @Override
    public void onNestedScroll(
            @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        final int oldScrollY = getScrollY();
        scrollBy(0, dyUnconsumed);
        final int myConsumed = getScrollY() - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        parentHelper.onStopNestedScroll(target, type);
        stopNestedScroll(type);
    }

    // NestedScrollingParent methods. For the most part these methods delegate
    // to the NestedScrollingParent2 methods above, passing TYPE_TOUCH as the
    // type to maintain API compatibility.

    @Override
    public boolean onStartNestedScroll(
            @NonNull View child, @NonNull View target, int axes) {
        return onStartNestedScroll(child, target, axes, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onNestedScrollAccepted(
            @NonNull View child, @NonNull View target, int axes) {
        onNestedScrollAccepted(child, target, axes, ViewCompat.TYPE_TOUCH);
    }



    @Override
    public void onNestedScroll(
            @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH);
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }

    public static void setDisableScroll(){

    }

    /**
     * Returns true iff the {@link NestedScrollView} is scrolled to the bottom
     * of its content (i.e. the card is completely expanded).
     */
    private static boolean isNsvScrolledToBottom(NestedScrollView nsv) {
        return !nsv.canScrollVertically(1);
    }

    /**
     * Returns true iff the {@link RecyclerView} is scrolled to the
     * top of its content (i.e. its first item is completely visible).
     */
    private static boolean isRvScrolledToTop(RecyclerView rv) {

        final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();

        Log.d("nsv", "isRvScrolledToTop:  Last visible item position " + lm.findLastVisibleItemPosition());
        Log.d("nsv", "isRvScrolledToTop: childCount of rv "+ rv.getChildCount());
        return lm.findFirstVisibleItemPosition() == 0
                && lm.findViewByPosition(0).getTop() == 0;
    }

    private static boolean isRvScrolledToBottom(RecyclerView rv) {
        final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        return lm.findLastVisibleItemPosition() == rv.getChildCount();

    }


}
