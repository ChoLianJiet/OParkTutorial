package com.opark.opark.CardSwipeView;

import java.util.Arrays;
import java.util.List;

public enum SwipeDirection {
    Left, Right, Top, Bottom;

    public static final List<com.opark.opark.CardSwipeView.SwipeDirection> FREEDOM = Arrays
            .asList(com.opark.opark.CardSwipeView.SwipeDirection.values());
    public static final List<com.opark.opark.CardSwipeView.SwipeDirection> FREEDOM_NO_BOTTOM = Arrays
            .asList(com.opark.opark.CardSwipeView.SwipeDirection.Top, com.opark.opark.CardSwipeView.SwipeDirection.Left, com.opark.opark.CardSwipeView.SwipeDirection.Right);
    public static final List<com.opark.opark.CardSwipeView.SwipeDirection> HORIZONTAL = Arrays
            .asList(com.opark.opark.CardSwipeView.SwipeDirection.Left, com.opark.opark.CardSwipeView.SwipeDirection.Right);
    public static final List<com.opark.opark.CardSwipeView.SwipeDirection> VERTICAL = Arrays
            .asList(com.opark.opark.CardSwipeView.SwipeDirection.Top, com.opark.opark.CardSwipeView.SwipeDirection.Bottom);

    public static List<com.opark.opark.CardSwipeView.SwipeDirection> from(int value) {
        switch (value) {
            case 0:
                return FREEDOM;
            case 1:
                return FREEDOM_NO_BOTTOM;
            case 2:
                return HORIZONTAL;
            case 3:
                return VERTICAL;
            default:
                return FREEDOM;
        }
    }
}
