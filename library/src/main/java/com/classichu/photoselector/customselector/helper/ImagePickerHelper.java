package com.classichu.photoselector.customselector.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.classichu.photoselector.tool.SdcardTool;
import com.classichu.photoselector.tool.ThreadTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class ImagePickerHelper {
    private static Map<String, List<String>> mGroupMap = new HashMap<>();
    private static boolean isSuccess = false;
    private static String message = "";

    /**
     * 扫描本地图片
     * @param context
     * @param scanLocalImagesCallBack
     */
    public static void scanLocalImage(final Context context, final ScanLocalImagesCallBack scanLocalImagesCallBack) {
        //KLog.d("SDF 扫描开始");
        if (!SdcardTool.hasSDCardMounted()) {
        //    KLog.e("无外部存储");
            isSuccess = false;
            message = "无外部存储";
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //ContentResolver
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                //只查询jpeg和png的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                if (cursor == null) {
                   // KLog.d("mCursor is null");
                    isSuccess = false;
                    message = "mCursor is null";
                    return;
                }
                mGroupMap.clear();
                while (cursor.moveToNext()) {
                    //获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (path==null){continue;}
                    File file = new File(path);
                    String name =file.getName();
                    //获取该图片的父路径名
                    String parentPath = file.getParentFile().getAbsolutePath();
                    if (parentPath==null){continue;}
                    //根据父路径将图片放入到Map中
                    if (!mGroupMap.containsKey(parentPath)) {
                        List<String> childList = new ArrayList<>();
                        childList.add(name);
                        mGroupMap.put(parentPath, childList);
                    } else {
                        mGroupMap.get(parentPath).add(name);
                    }
                }

                //通知扫描图片完成
                isSuccess = true;
                message = "图片扫描完成";
              //  KLog.d("SDF 扫描结束");
                /**
                 *close
                 */
                cursor.close();


                if (isSuccess) {
                    ThreadTool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scanLocalImagesCallBack.onSuccess(mGroupMap);
                        }
                    });

                } else {
                    ThreadTool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scanLocalImagesCallBack.onError(message);
                        }
                    });
                }

            }
        }).start();

    }

    public interface ScanLocalImagesCallBack {
        void onSuccess(Map<String, List<String>> groupMap);

        void onError(String message);
    }
}
