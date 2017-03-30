package com.classichu.photoselector.helper;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.classichu.photoselector.R;
import com.classichu.photoselector.tool.SdcardTool;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by louisgeek on 2016/12/19.
 * 系统的默认的机制，Intent触发Camera程序，拍好照片后，将会返回数据，但是考虑到内存问题，Camera不会将全尺寸的图像返回给调用的Activity，一般情况下，有可能返回的是缩略图，比如120*160px。这看起来就像是一个缩略图一样了，这样的效果是让我们非常不满意的，因为我们想要上传的明明是一个高清的图片，但是这样我们上传的竟然是一个特别模糊的图片啦，所以我们不能采用这种办法了。
 * <p>
 * 我们的思路：调用系统相机，拍照完将拍完的照片存在sd卡的某一个地方，然后我们调用自己的剪裁工具，不调用系统的剪裁工具了，这样我们剪裁完的图片也就是一张高清的图片了，这样就完美的解决了所遇到的问题。
 * <p>
 * 文／关玮琳linSir（简书作者）
 * 原文链接：http://www.jianshu.com/p/ba0c654935f1#
 * 著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
 */

public class ClassicPhotoHelper {

    public static final int GALLERY_REQUEST = 100;

    public static final int CAMERA_REQUEST = 200;

    private static final String mCameraSaveImageTempPath =
            Environment.getExternalStorageDirectory() + File.separator + "tempImage";


    /**
     * 启动手机相册
     * 如果不指定uri,那么系统会使用默认uri,并放在intent的data里,在onActivityResult里返回.但这只是原生系统的,
     * 各厂商改rom,这里经常会返回intent为null或者intent里的data为null.
     */
    public static void getPhotoFromGallery(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// Intent.ACTION_PICK
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //设置了MediaStore.EXTRA_OUTPUT，所以data为null，数据直接就在uri中
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImageTempPath)));
        activity.startActivityForResult(intent, GALLERY_REQUEST);
    }

    /**
     * 启动手机相机
     * 如果不指定uri,那么系统会使用默认uri,并放在intent的data里,在onActivityResult里返回.但这只是原生系统的,
     * 各厂商改rom,这里经常会返回intent为null或者intent里的data为null.
     */
    public static void getPhotoFromCamera(Activity activity) {
        if (SdcardTool.hasSDCardMounted()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  getPhotoFromCamera//设置了MediaStore.EXTRA_OUTPUT，所以data为null，数据直接就在uri中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraSaveImageTempPath)));
            activity.startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Log.i("aaa", "SD not exist");
        }
    }

    /**
     * @param activity
     * @param dataIntent
     */
    public static String getPhotoFromGalleryBackAndGoToUCrop(Activity activity, Intent dataIntent) {
        String imageFilePath = getFileAbsolutePathFormUriSupport(activity, dataIntent.getData());
       // KLog.d("imageFilePath:" + imageFilePath);
        //float aspectRatioX, float aspectRatioY  宽高比，比如16：9
        goToUCrop(activity, imageFilePath, 300, 300);
        return imageFilePath;
    }

    public static String getPhotoFromGalleryBack(Activity activity, Intent dataIntent) {
        String imageFilePath = getFileAbsolutePathFormUriSupport(activity, dataIntent.getData());
        return imageFilePath;
    }

    /**
     * //  getPhotoFromCamera //设置了MediaStore.EXTRA_OUTPUT，所以data为null，数据直接就在uri中
     *
     * @param activity
     */
    public static void getPhotoFromCameraBackAndGoToUCrop(Activity activity) {
      /*   //修正方向  URI取有问题 暂时不用
       int degree = ImageTool.getBitmapDegree(imagePath);

        if(0 != degree){
            bitmapxx=ImageTool.rotateBitmapByDegree(bitmapxx, degree);
        }*/
        //float aspectRatioX, float aspectRatioY  宽高比，比如16：9
        goToUCrop(activity, mCameraSaveImageTempPath, 300, 300);
    }
    public static void getPhotoFromCameraBackCallAtOnActivityResult(Context context,int requestCode, int resultCode,boolean saveToGallery,OnBackImageCallback onBackImageCallback) {
        if (requestCode==ClassicPhotoHelper.CAMERA_REQUEST&&resultCode==RESULT_OK){
                if (onBackImageCallback!=null){
                    onBackImageCallback.onBackImage(mCameraSaveImageTempPath);
                }
                if (!TextUtils.isEmpty(mCameraSaveImageTempPath)&&saveToGallery){
                    saveImageToGallery(context,mCameraSaveImageTempPath);
                }
        }
    }
    public  interface  OnBackImageCallback{
        void onBackImage(String path);
    }

    /**
     * "hhhqqq/zfq"
     * @param saveChildPath
     * @param bmp
     */
    public static void saveImageToSdcard(String saveChildPath, Bitmap bmp) {
        // 保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), saveChildPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveImageToGallery(Context context,String path) {
        File file=new File(path);
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
      context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
     /*   context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));*/
    }

    /**
     * UCrop.of(sourceUri, destinationUri)
     * .withAspectRatio(16, 9)
     * .withMaxResultSize(maxWidth, maxHeight)
     * .start(context);
     * <p>
     * float aspectRatioX, float aspectRatioY
     * 宽高比，比如16：9
     *
     * @param activity
     * @param sourceFilePath
     * @param aspectRatioX
     * @param aspectRatioY
     * @return
     */
    public static String goToUCrop(Activity activity, String sourceFilePath, float aspectRatioX, float aspectRatioY) {

        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        /**
         *
         */
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        /**
         *
         */
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start(activity, UCrop.REQUEST_CROP);
        return cameraScalePath;
    }


    /**
     * ======================================================================================
     * ======================================================================================
     * ======================================================================================
     * ======================================================================================
     * <p>
     * http://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri
     * 获取相册 返回  得到绝对路径 兼容方法
     * <p>
     * 一般 uri返回的是file:///...
     * android 4.4 返回的是content:///...
     */

    public static String getFileAbsolutePathFormUriSupport(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
