package com.classichu.photoselector.listener;

import android.view.View;

import java.util.Calendar;

/**
 * 防止过快的点击
 * Created by louisgeek on 2016/12/13.
 */

public abstract class OnNotFastClickListener implements View.OnClickListener {

    private static final int MIN_FAST_CLICK_DELAY = 1000;
    private long mLastClickTime;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - mLastClickTime > MIN_FAST_CLICK_DELAY) {
            this.onNotFastClick(v);
            mLastClickTime = currentTime;
        }
    }

    protected abstract void onNotFastClick(View v);
}
