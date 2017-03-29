package com.classichu.photoselector.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.classichu.photoselector.R;
import com.classichu.photoselector.listener.OnNotFastClickListener;
import com.classichu.photoselector.widget.StatusBarColorFixBottomSheetDialog;
import com.yalantis.ucrop.UCrop;


/**
 * Created by louisgeek on 2016/12/22.
 */

public class ClassicSelectPhotoHelper {
    private static StatusBarColorFixBottomSheetDialog mBottomSheetDialog;
    private static boolean mNeedCrop;
    public static void initBottomSheetDialog(FragmentActivity fragmentActivity) {
        initBottomSheetDialog(fragmentActivity,true);
    }
    public static void initBottomSheetDialog(final FragmentActivity fragmentActivity, boolean needCrop) {
        mNeedCrop = needCrop;
        mBottomSheetDialog = new StatusBarColorFixBottomSheetDialog(fragmentActivity);

        View view = LayoutInflater.from(fragmentActivity).inflate(R.layout.layout_classic_bottom_sheet, null, false);
        Button id_btn_camera = (Button) view.findViewById(R.id.id_btn_camera);
        id_btn_camera.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                getPhotoFromCamera(fragmentActivity);
            }
        });
        Button id_btn_photo = (Button) view.findViewById(R.id.id_btn_photo);
        id_btn_photo.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                getPhotoFromGallery(fragmentActivity);

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

    private static void getPhotoFromGallery(final FragmentActivity fragmentActivity) {
        /**
         * 检测SD卡读取权限
         */
        PermissionsHelper.initDangerousPermissionOperation(new PermissionsHelper.DangerousPermissionOperation() {
            @Override
            public void doDangerousOperation(FragmentActivity fragmentActivity, String... permissions) {
                getPhotoFromGalleryContinue(fragmentActivity);
            }

            @Override
            public void permissionProhibition() {
                Toast.makeText(fragmentActivity, "没有SD卡读取权限!", Toast.LENGTH_SHORT).show();

            }
        });
        PermissionsHelper.checkPermissions(fragmentActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private static void getPhotoFromGalleryContinue(FragmentActivity fragmentActivity) {
        ClassicPhotoHelper.getPhotoFromGallery(fragmentActivity);
    }

    private static void getPhotoFromCamera(final FragmentActivity fragmentActivity) {
        /**
         * 检测相机权限
         */
        PermissionsHelper.initDangerousPermissionOperation(new PermissionsHelper.DangerousPermissionOperation() {
            @Override
            public void doDangerousOperation(FragmentActivity fragmentActivity, String... permissions) {
                getPhotoFromCameraContinue(fragmentActivity);
            }

            @Override
            public void permissionProhibition() {
                Toast.makeText(fragmentActivity, "没有相机使用权限!", Toast.LENGTH_SHORT).show();
               // GoToSysConfigHelper.goToNormalEnterWithManufacturer(fragmentActivity);
            }
        });
        PermissionsHelper.checkPermissions(fragmentActivity, Manifest.permission.CAMERA);
    }

    private static void getPhotoFromCameraContinue(FragmentActivity fragmentActivity) {
        ClassicPhotoHelper.getPhotoFromCamera(fragmentActivity);
    }

    private static void dismissBottomSheetDialog() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    public static void callAtOnActivityResult(Activity activity, int requestCode, int resultCode,
                                              Intent data, final OnBackImageListener onBackImageListener) {
        switch (requestCode) {
            case ClassicPhotoHelper.CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (mNeedCrop) {
                        ClassicPhotoHelper.getPhotoFromCameraBackAndGoToUCrop(activity);
                    } else {

                            ClassicPhotoHelper.getPhotoFromCameraBackCallAtOnActivityResult(activity,requestCode, resultCode,true, new ClassicPhotoHelper.OnBackImageCallback() {
                                @Override
                                public void onBackImage(String path) {
                                if (path==null&&path.equals("")){
                                    return;
                                }
                                    if (onBackImageListener != null) {
                                        onBackImageListener.onBackImagePath(path);
                                    }
                                }
                            });

                        }

                    dismissBottomSheetDialog();
                }
                break;
            case ClassicPhotoHelper.GALLERY_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (mNeedCrop) {
                        ClassicPhotoHelper.getPhotoFromGalleryBackAndGoToUCrop(activity, data);
                    } else {
                        if (onBackImageListener != null) {
                            onBackImageListener.onBackImagePath(ClassicPhotoHelper.getPhotoFromGalleryBack(activity,data));
                        }
                    }
                    dismissBottomSheetDialog();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    String imagePath = ClassicPhotoHelper.getFileAbsolutePathFormUriSupport(activity, resultUri);

                    if (onBackImageListener != null) {
                        onBackImageListener.onUCropBackImagePath(imagePath);
                    }
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                    if (onBackImageListener != null) {
                        onBackImageListener.onUCropBackError(cropError);
                    }
                }
                break;
        }
    }

    public abstract static class OnBackImageListener {
        public void onBackImagePath(String imagePath) {

        }

        public void onUCropBackImagePath(String imagePath) {

        }

        public void onUCropBackError(Throwable cropError) {

        }
    }

    public static void callAtOnRequestPermissionsResult(FragmentActivity fragmentActivity, int requestCode, final String[] permissions, int[] grantResults) {
        PermissionsHelper.callAtOnRequestPermissionsResult(fragmentActivity, requestCode, permissions, grantResults);
    }
}
