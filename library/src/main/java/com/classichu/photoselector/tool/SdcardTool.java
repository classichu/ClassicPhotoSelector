package com.classichu.photoselector.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class SdcardTool {

    /**
     * 判断sd卡可用
     * @return
     */
    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡路径
     *"/storage/emulated/0/"
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取系统存储路径
     * "/system"
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }
    /**
     * 得到/sdcard/Android/data/<application package>/cache 或者/data/data/<application package>/cache
     *
     * 当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。
     * 前者获取到的就是 /sdcard/Android/data/<application package>/cache 这个路径，
     * 而后者获取到的是 /data/data/<application package>/cache 这个路径
     * @param context
     * @return
     */
    public static String getDiskCacheDirPath(Context context) {
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
    /**
     * 得到/data/data/<application package>/cache
     * @param context
     * @return
     */
    public static String getCacheDirPath(Context context) {
        String cachePath = context.getCacheDir().getPath();
        return cachePath;
    }
}
