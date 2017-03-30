package com.classichu.photoselector.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.classichu.photoselector.bean.PhotoSelectorDataWrapper;
import com.classichu.photoselector.customselector.ClassicPhotoSelectorActivity;
import com.classichu.photoselector.imagespicker.ImagePickBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by louisgeek on 2017/3/28.
 */

public class ClassicPhotoUploaderDataHelper {
    public static final int REQUEST_CODE_PHOTO_SELECTOR = 2329;

    public static void setDataAndToPhotoSelector(FragmentActivity fragmentActivity,List<ImagePickBean> imagePickBeanList,
                                                 int maxImagePickCount) {
        setDataAndToPhotoSelector(fragmentActivity,imagePickBeanList, maxImagePickCount, true);
    }

    public static void setDataAndToPhotoSelector(FragmentActivity fragmentActivity,
                                                 List<ImagePickBean> imagePickBeanList,
                                                 int maxImagePickCount, boolean isTitleCenter) {
        WeakReference<FragmentActivity> fragmentActivityWeakReference = new WeakReference<>(fragmentActivity);
        Intent intent = new Intent(fragmentActivityWeakReference.get(), ClassicPhotoSelectorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTitleCenter", isTitleCenter);
        PhotoSelectorDataWrapper wrapper=new PhotoSelectorDataWrapper();
        wrapper.setMaxPickCount(maxImagePickCount);
        wrapper.setImagePickBeanList(imagePickBeanList);
        wrapper.setImagePickKey("");
        bundle.putSerializable("photoSelectorDataWrapper", wrapper);
        intent.putExtras(bundle);
        fragmentActivityWeakReference.get().startActivityForResult(intent, REQUEST_CODE_PHOTO_SELECTOR);
    }

    public static void callAtOnActivityResult(int requestCode, int resultCode, Intent data,
                                              PhotoSelectorBackData photoSelectorBackData) {
        PhotoSelectorDataWrapper photoSelectorDataWrapper;
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO_SELECTOR) {
            photoSelectorDataWrapper = (PhotoSelectorDataWrapper)
                    data.getSerializableExtra("photoSelectorDataWrapper");
            if (photoSelectorBackData != null) {
                photoSelectorBackData.backData(photoSelectorDataWrapper.getImagePickBeanList());
            }

        }
    }

    public interface PhotoSelectorBackData {
        void backData(List<ImagePickBean> imagePickBeanList);
    }
}
