package com.opark.opark.search_view_behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.opark.opark.R;

import org.w3c.dom.Text;

public class SearchViewBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {

    private static final int DIRECTION_UP = 1 ;
    private static final int DIRECTION_DOWN = -1;
    private Context mContext;

    private int mScrollingDirection;
    private int mScrollTrigger;
    private int mScrollDistance  ;
    private float mFinalHeight;
    private int mScrollThreshold;
    ObjectAnimator mAnimator;

    public SearchViewBehavior() {
        super();
    }

    public SearchViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme()
                .obtainStyledAttributes(new int[] {R.attr.actionBarSize});
        //Use half the standard action bar height
        mScrollThreshold = a.getDimensionPixelSize(0, 0) / 2;
        a.recycle();

    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  AppBarLayout child, View target,
                                  int dx, int dy,
                                  int[] consumed, int type) {
        //Determine direction changes here
        if (dy < 0 && mScrollingDirection != DIRECTION_UP) {
            Log.d("scroll", "onNestedPreScroll: direction up");
            mScrollingDirection = DIRECTION_UP;
            mScrollDistance = 0;
        }
// else if (dy < 0 && mScrollingDirection != DIRECTION_DOWN) {
//            mScrollingDirection = DIRECTION_DOWN;
//            mScrollDistance = 0;
//        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

        mScrollDistance += dyConsumed;

        Log.d("scroll", "scroll distance:  " + mScrollDistance);
        if (mScrollDistance > mScrollThreshold
                && mScrollTrigger != DIRECTION_UP) {

            Log.d("scroll", "onNestedScroll: direction up");
            child.setExpanded(false);



            //Hide the target view
            mScrollTrigger = DIRECTION_UP;
//            restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
        } else if (mScrollDistance < -mScrollThreshold
                && mScrollTrigger != DIRECTION_DOWN) {
            //Return the target view
            mScrollTrigger = DIRECTION_DOWN;
            child.setExpanded(true);

//            restartAnimator(child, 0f);
            }




    }

    private void restartAnimator(View target, float value) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        mAnimator = ObjectAnimator
                .ofFloat(target, View.TRANSLATION_Y, value)
                .setDuration(250);
        mAnimator.start();
    }


    private float getTargetHideValue(ViewGroup parent, View target) {
        if (target instanceof TextView) {
            Log.d("scroll", "getTargetHideValue: " + -target.getHeight());

            target.setScaleX(1f);
//            target.setScaleY(10f);
            return -target.getHeight();
        } else if (target instanceof AppBarLayout) {
            Log.d("scroll", "getTargetHideValue:  layout is appbar");
            return parent.getHeight() - target.getTop();
        }

        return 0f;
    }


//    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View childLayout, View dependency){
//        return  dependency instanceof RecyclerView;
//    }
//
//
//
//    public boolean onDependentViewChanged (CoordinatorLayout coordinatorLayout, View childLayout, View dependency){
//        if(childLayout instanceof EditText && dependency instanceof RecyclerView){
//
//            EditText editText = (EditText) childLayout;
//            editText.setHint("Search");
//            RecyclerView recyclerView = (RecyclerView) dependency;
//            int viewPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//
//
//            if(viewPosition >= 3){
//                editText.setTextSize(20);
//            }
//            else if(viewPosition < 3){
//                editText.setTextSize(80);
//            }
//        } }
}
