package com.classichu.photoselector.helper;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;


/**
 * Created by louisgeek on 2017/1/6.
 * <p>
 * 常用9组 Dangerous Permission
 * <p>
 * CALENDAR（日历）
 * READ_CALENDAR
 * WRITE_CALENDAR
 * <p>
 * CAMERA（相机）
 * CAMERA
 * <p>
 * CONTACTS（联系人）
 * READ_CONTACTS
 * WRITE_CONTACTS
 * GET_ACCOUNTS
 * <p>
 * LOCATION（位置）
 * ACCESS_FINE_LOCATION
 * ACCESS_COARSE_LOCATION
 * <p>
 * MICROPHONE（麦克风）
 * RECORD_AUDIO
 * <p>
 * PHONE（手机）
 * READ_PHONE_STATE
 * CALL_PHONE
 * READ_CALL_LOG
 * WRITE_CALL_LOG
 * ADD_VOICEMAIL
 * USE_SIP
 * PROCESS_OUTGOING_CALLS
 * <p>
 * SENSORS（传感器）
 * BODY_SENSORS
 * <p>
 * SMS（短信）
 * SEND_SMS
 * RECEIVE_SMS
 * READ_SMS
 * RECEIVE_WAP_PUSH
 * RECEIVE_MMS
 * <p>
 * STORAGE（存储卡）
 * READ_EXTERNAL_STORAGE
 * WRITE_EXTERNAL_STORAGE
 */

public class PermissionsHelper {

    private  static  final int REQUEST_CODE_PERMISSIONS=1024;
    /**
     * like Manifest.permission.CAMERA
     * ...
     * @param fragmentActivity
     * @param permissions
     */
    public static void checkPermissions(final FragmentActivity fragmentActivity, final String... permissions) {
       // CLog.d("checkPermissions");

        boolean mIsAllGranted = true;
        //
        for (int i = 0; i < permissions.length; i++) {
            final String permission = permissions[i];
            /**
             * 检测权限
             */
            //未授权
            if (ContextCompat.checkSelfPermission(fragmentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                //有一个未授权  就false
                mIsAllGranted = false;
                //会回调OnRequestPermissionsResult
                ActivityCompat.requestPermissions(fragmentActivity, new String[]{permission},REQUEST_CODE_PERMISSIONS);
            }
        }
        //操作
        if (mIsAllGranted&&mDangerousPermissionOperation!=null){

            mDangerousPermissionOperation.doDangerousOperation(fragmentActivity,permissions);
        }

    }

    public static void callAtOnRequestPermissionsResult(final FragmentActivity fragmentActivity, int requestCode, final String[] permissions, int[] grantResults) {
      // CLog.d("callAtOnRequestPermissionsResult ");
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                //permissions  grantResults 一一对应
                for (int i = 0; i < permissions.length; i++) {

                    boolean canShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, permissions[i]);
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (canShowRationale) {//表明用户没有彻底禁止弹出权限请求
                          /*todo  DialogTool.showAskDialog(fragmentActivity, "有权限才能干事啊，记得给予！", new OkCancelDialogFragment.OnBtnClickListener() {
                                @Override
                                public void onOkBtnClick(DialogInterface dialogInterface) {
                                    //
                                    ActivityCompat.requestPermissions(fragmentActivity, permissions, CommFinalData.REQUEST_CODE_PERMISSION);
                                }

                                @Override
                                public void onCancelBtnClick(DialogInterface dialogInterface) {

                                }
                            });*/
                        } else {//表明用户已经彻底禁止弹出权限请求
                          /*
                            ToastTool.showImageWarn(permissions[i] + "权限被禁止，进入权限设置界面");*/
                          if (mDangerousPermissionOperation!=null){
                              mDangerousPermissionOperation.permissionProhibition();
                          }
                        }
                        return;//
                    }
                }
                break;
        }
    }

    public static void initDangerousPermissionOperation(DangerousPermissionOperation dangerousPermissionOperation) {
        mDangerousPermissionOperation = dangerousPermissionOperation;
    }

    private static DangerousPermissionOperation mDangerousPermissionOperation;

    public interface DangerousPermissionOperation {
        void doDangerousOperation(FragmentActivity fragmentActivity, String... permissions);
        void permissionProhibition();
    }
}
