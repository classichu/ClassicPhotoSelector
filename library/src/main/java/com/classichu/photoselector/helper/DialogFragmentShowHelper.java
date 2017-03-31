package com.classichu.photoselector.helper;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by louisgeek on 2017/1/19.
 *
 * 解决IllegalStateException: Can not perform this action after onSaveInstanceState
 */

public class DialogFragmentShowHelper {
    /**
     *
     * @param fragmentManager
     * @param dialogFragment
     * @param tag
     */
    public static void show(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(dialogFragment,tag);
        transaction.commitAllowingStateLoss();
    }

    /**
     * @param transaction
     * @param dialogFragment
     * @param tag
     * @return
     */
    public static int show(FragmentTransaction transaction, DialogFragment dialogFragment, String tag) {
        transaction.add(dialogFragment,tag);
        return transaction.commitAllowingStateLoss();
    }
}
