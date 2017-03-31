package com.classichu.photoselector.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.classichu.photoselector.helper.DialogFragmentShowHelper;
import com.classichu.photoselector.wrapper.DialogConfigWrapper;


/**
 * Created by louisgeek on 2016/6/7.
 */
public class ClassicDialogFragment extends AppCompatDialogFragment {
    private final static String TITLE_KEY = "title_key";
    private final static String MESSAGE_KEY = "message_key";
    private DialogConfigWrapper mWrapperDialogConfig = new DialogConfigWrapper();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String title = getArguments().getString(TITLE_KEY);
        String message = getArguments().getString(MESSAGE_KEY);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        //这样设置无效 需要设置DialogFragment的setCancelable方法才有效
        //!!! builder.setCancelable(mWrapperDialogConfig.isCancelable());
        this.setCancelable(mWrapperDialogConfig.isCancelable());
        builder.setView(mWrapperDialogConfig.getContentView());
        if (!TextUtils.isEmpty(mWrapperDialogConfig.getOkText())) {
            builder.setPositiveButton(mWrapperDialogConfig.getOkText(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mWrapperDialogConfig.getOnBtnClickListener() != null) {
                        mWrapperDialogConfig.getOnBtnClickListener().onBtnClickOk(dialogInterface);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(mWrapperDialogConfig.getCancelText())) {
            builder.setNegativeButton(mWrapperDialogConfig.getCancelText(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mWrapperDialogConfig.getOnBtnClickListener() != null) {
                        mWrapperDialogConfig.getOnBtnClickListener().onBtnClickCancel(dialogInterface);
                    }
                }
            });
        }


        Dialog dialog = builder.create();

        if (!TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
            builder.setTitle(title);
        } else {
            //隐藏标题
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }



        // return super.onCreateDialog(savedInstanceState);
        return dialog;

    }

    private static ClassicDialogFragment newInstance(String title, String message) {
        ClassicDialogFragment dialogFragment = new ClassicDialogFragment();
        Bundle args = new Bundle();
        // 自定义的标题
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static abstract class OnBtnClickListener {
        public void onBtnClickOk(DialogInterface dialogInterface) {

        }

        public void onBtnClickCancel(DialogInterface dialogInterface) {

        }
    }

    private void setWrapperDialogConfig(DialogConfigWrapper wrapperDialogConfig) {
        mWrapperDialogConfig = wrapperDialogConfig;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //#### super.show(manager, tag);
        DialogFragmentShowHelper.show(manager, this, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        //#### return super.show(transaction, tag);
        return DialogFragmentShowHelper.show(transaction, this, tag);
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * 设置 宽度
         */
        Dialog dialog = getDialog();
        if (dialog != null&&dialog.getWindow()!=null&&mWrapperDialogConfig.getWidthPercentValue()>0) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * mWrapperDialogConfig.getWidthPercentValue()), ViewGroup.LayoutParams.WRAP_CONTENT);
            //对话框背景透明
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public View getDialogContentView() {
        return mWrapperDialogConfig.getContentView();
    }

    public static class Builder {
        private String title;
        private String message;
        private Context context;
        private DialogConfigWrapper wrapperDialogConfig = new DialogConfigWrapper();


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(int layoutResId) {
            setContentView(LayoutInflater.from(context).inflate(layoutResId, null));
            return this;
        }

        public Builder setContentView(View contentView) {
            this.wrapperDialogConfig.setContentView(contentView);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int resid) {
            setTitle(context.getString(resid));
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int resid) {
            setMessage(context.getString(resid));
            return this;
        }

        public Builder setOkText(String okText) {
            wrapperDialogConfig.setOkText(okText);
            return this;
        }

        public Builder setOkText(int resid) {
            setOkText(context.getString(resid));
            return this;
        }

        public Builder setCancelText(String cancelText) {
            wrapperDialogConfig.setCancelText(cancelText);
            return this;
        }

        public Builder setCancelText(int resid) {
            setCancelText(context.getString(resid));
            return this;
        }
        public Builder setWidthPercentValue(double widthPercentValue) {
            wrapperDialogConfig.setWidthPercentValue(widthPercentValue);
            return this;
        }
        public Builder setCancelable(boolean cancelable) {
            wrapperDialogConfig.setCancelable(cancelable);
            return this;
        }

        public Builder setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
            this.wrapperDialogConfig.setOnBtnClickListener(onBtnClickListener);
            return this;
        }

        public ClassicDialogFragment build() {
            ClassicDialogFragment classicDialogFragment = ClassicDialogFragment.newInstance(title, message);
            classicDialogFragment.setWrapperDialogConfig(wrapperDialogConfig);
            return classicDialogFragment;
        }
    }
}
