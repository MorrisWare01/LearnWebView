package com.morrisware.android.learnwebview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Sjn on 16/6/22.
 */
public class SwipeViewPager extends ViewPager {
    //默认仅用滑动
    private boolean swipeEnable = false;

    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isSwipeEnable() {
        return swipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    // 1.禁掉viewpager左右滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isSwipeEnable() && super.onTouchEvent(event);
    }

    // 2.禁掉viewpager左右滑动事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isSwipeEnable() && super.onInterceptTouchEvent(event);
    }

}
