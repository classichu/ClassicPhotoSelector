package com.classichu.photoselector.tool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

/**
 * Created by louisgeek on 2016/9/29.
 */
public class AppTool {
    /**
     * 获取应用程序的IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telecomManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telecomManager.getDeviceId();
        return imei;
    }

    /**
     * 获取设备的系统版本号
     */
    public static int getDeviceSDK() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        return sdk;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceName() {
        String model = android.os.Build.MODEL;
        return model;
    }
    /**
     * 获取设备的厂商
     */
    public static String getDeviceManufacturer() {
        String Manufacturer = android.os.Build.MANUFACTURER;
        String BOARD = android.os.Build.BOARD;
        String DEVICE = android.os.Build.DEVICE;
        Log.d("KKK", "getDeviceManufacturer: "+Manufacturer);
        Log.d("KKK", "getDeviceManufacturer: "+BOARD);
        Log.d("KKK", "getDeviceManufacturer: "+DEVICE);
        return Manufacturer;
    }
    /**
     * 获取自己应用程序的名称
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;

        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            applicationInfo = null;
        }

        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 获取自己应用程序的图标
     */
    public static Bitmap getIconBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名
     *
     * @return 当前应用的版本名
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本读取错误";
        }
    }

    /**
     * 获取应用程序包名
     */
    public static String getPackageName(Context context) {
        String pkgName = context.getPackageName();
        return pkgName;
    }

    /**
     * 判断app是否在前台还是在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
				BACKGROUND=400 EMPTY=500 FOREGROUND=100
				GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
                Log.i(context.getPackageName(), "louisz==此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "louisz==处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "louisz==处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static void installApk(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context) {
        Uri uri = Uri.parse("package:com.xxx.xxx");
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }
}
