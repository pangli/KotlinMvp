package com.zorro.kotlin.baselibs.widget.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zorro on 2019/10/16.
 * BottomNavigationView 上滑隐藏 下滑显示
 */

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<View> {

    private ObjectAnimator outAnimator, inAnimator;

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    // 垂直滑动
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
//    }
//
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
//        if (dy > 0) {// 上滑隐藏
//            if (outAnimator == null) {
//                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0, child.getHeight());
//                outAnimator.setDuration(200);
//            }
//            if (!outAnimator.isRunning() && child.getTranslationY() <= 0) {
//                outAnimator.start();
//            }
//        } else if (dy < 0) {// 下滑显示
//            if (inAnimator == null) {
//                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getHeight(), 0);
//                inAnimator.setDuration(200);
//            }
//            if (!inAnimator.isRunning() && child.getTranslationY() >= child.getHeight()) {
//                inAnimator.start();
//            }
//        }
//    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy > 0) {// 上滑隐藏
            if (outAnimator == null) {
                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0, child.getHeight());
                outAnimator.setDuration(200);
            }
            if (!outAnimator.isRunning() && child.getTranslationY() <= 0) {
                outAnimator.start();
            }
        } else if (dy < 0) {// 下滑显示
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getHeight(), 0);
                inAnimator.setDuration(200);
            }
            if (!inAnimator.isRunning() && child.getTranslationY() >= child.getHeight()) {
                inAnimator.start();
            }
        }
    }
}