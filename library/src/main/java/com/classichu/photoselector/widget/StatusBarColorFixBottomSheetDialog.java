package com.classichu.photoselector.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.ViewGroup;

/**
 * Created by louisgeek on 2016/12/20.
 */

public class StatusBarColorFixBottomSheetDialog extends BottomSheetDialog {
    public StatusBarColorFixBottomSheetDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *  getOwnerActivity();
         *  getContext();
         获取不到   为null
         */
        int screenHeight = getScreenHeight();
        int statusBarHeight = getStatusBarHeight();
        //
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }

    /**
     * 屏幕高度  Context  get from Dialog
     *
     * @return
     */
    private int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 状态栏高度 Context  get from Dialog
     *
     * @return
     */
    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        Resources resources = getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }
}
