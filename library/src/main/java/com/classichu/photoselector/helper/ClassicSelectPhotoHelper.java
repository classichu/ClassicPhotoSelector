package com.classichu.photoselector.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.classichu.photoselector.R;
import com.classichu.photoselector.listener.OnNotFastClickListener;
import com.classichu.photoselector.widget.StatusBarColorFixBottomSheetDialog;
import com.yalantis.ucrop.UCrop;



/**
 * Created by louisgeek on 2016/12/22.
 */

public class ClassicSelectPhotoHelper {
    private static StatusBarColorFixBottomSheetDialog mBottomSheetDialog;
    public static void initBottomSheetDialog(final Activity activity) {
        mBottomSheetDialog = new StatusBarColorFixBottomSheetDialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_classic_bottom_sheet, null, false);
        Button id_btn_camera = (Button) view.findViewById(R.id.id_btn_camera);
        id_btn_camera.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                ClassicPhotoHelper.getPhotoFromCamera(activity);
            }
        });
        Button id_btn_photo = (Button) view.findViewById(R.id.id_btn_photo);
        id_btn_photo.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                ClassicPhotoHelper.getPhotoFromGallery(activity);
            }
        });
        Button id_btn_cancel = (Button) view.findViewById(R.id.id_btn_cancel);
        id_btn_cancel.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        /**
         *
         */
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();

        /**
         *
         */
       /* int baseStatusBarColor = ContextCompat.getColor(this, R.color.baseStatusBarColor);
        //StatusBarUtil.setColor(this, baseStatusBarColor);
         StatusBarCompat.setStatusBarColor(this, baseStatusBarColor);*/
    }

    private static void dismissBottomSheetDialog() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    public static void callAtOnActivityResult(Activity activity, int requestCode, int resultCode,
                                              Intent data, OnUCropBackImagePathListener onUCropBackImagePathListener) {
        switch (requestCode) {
            case ClassicPhotoHelper.CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    ClassicPhotoHelper.getPhotoFromCameraBackAndGoToUCrop(activity);
                    dismissBottomSheetDialog();
                }
                break;
            case ClassicPhotoHelper.GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    ClassicPhotoHelper.getPhotoFromGalleryAndGoToUCrop(activity, data);
                    dismissBottomSheetDialog();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    String imagePath = ClassicPhotoHelper.getFileAbsolutePathFormUriSupport(activity, resultUri);

                    if (onUCropBackImagePathListener != null) {
                        onUCropBackImagePathListener.onUCropBackImagePath(imagePath);
                    }
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                    if (onUCropBackImagePathListener != null) {
                        onUCropBackImagePathListener.onUCropBackError(cropError);
                    }
                }
                break;
        }
    }

    public interface OnUCropBackImagePathListener {
        void onUCropBackImagePath(String imagePath);
        void onUCropBackError(Throwable cropError);
    }
}
