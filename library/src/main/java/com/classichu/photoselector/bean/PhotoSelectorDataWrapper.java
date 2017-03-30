package com.classichu.photoselector.bean;

import com.classichu.photoselector.imagespicker.ImagePickBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by louisgeek on 2017/3/28.
 */

public class PhotoSelectorDataWrapper implements Serializable{
    private String imagePickKey;//

    public List<ImagePickBean> getImagePickBeanList() {
        return imagePickBeanList;
    }

    public void setImagePickBeanList(List<ImagePickBean> imagePickBeanList) {
        this.imagePickBeanList = imagePickBeanList;
    }

    private List<ImagePickBean> imagePickBeanList;
    public String getImagePickKey() {
        return imagePickKey;
    }

    public void setImagePickKey(String imagePickKey) {
        this.imagePickKey = imagePickKey;
    }

    public int getMaxPickCount() {
        return maxPickCount;
    }

    public void setMaxPickCount(int maxPickCount) {
        this.maxPickCount = maxPickCount;
    }

    private int maxPickCount;
    //


}
