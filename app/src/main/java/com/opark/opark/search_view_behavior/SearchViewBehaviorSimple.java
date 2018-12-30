package com.opark.opark.search_view_behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.opark.opark.R;

public class SearchViewBehaviorSimple extends CoordinatorLayout.Behavior<TextView> {
    private static final String TAG = "custombehavior" ;

    public SearchViewBehaviorSimple() {
    }

    public SearchViewBehaviorSimple(Context context, AttributeSet attrs) {
        super(context, attrs);


    }





    @Override
    public boolean layoutDependsOn(

            CoordinatorLayout parent,
            TextView child,
            View dependency) {


        Log.d(TAG, "layoutDependsOn: instance of ");


        return dependency instanceof Toolbar;
    }




    private void modifyAvatarDependingDependencyState(

            TextView avatar, View dependency) {


        Log.d(TAG, "modifyAvatarDependingDependencyState: ");
        avatar.setY(dependency.getY());
        avatar.setX(dependency.getScaleX()*3);
        avatar.findViewById(R.id.email_text).setTranslationY(5.0f);

        Log.d(TAG, "modifyAvatarDependingDependencyState: avatar get " + avatar.getX());
        //  avatar.setY(dependency.getY());
        //  avatar.setBlahBlah(dependency.blah / blah);
    }



    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          TextView avatar,
                                          View dependency) {


        Log.d(TAG, "onDependentViewChanged: ");

        modifyAvatarDependingDependencyState(avatar, dependency);

        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());



        // Note that the RelativeLayout gets translated.

        avatar.findViewById(R.id.email_text).setTranslationY(translationY);
        return true;

    }




}
