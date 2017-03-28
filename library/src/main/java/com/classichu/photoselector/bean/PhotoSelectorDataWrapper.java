package com.classichu.photoselector.bean;

import com.classichu.photoselector.imagespicker.ImagePickBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by louisgeek on 2017/3/28.
 */

public class PhotoSelectorDataWrapper implements Serializable{
    private String resultid;//选株点击详情id
    private String key;
    private int maxCount;
    //调查项id
    private List<ImagePickBean> imagePickBeanList;

    public void setResultid(String resultid) {
        this.resultid = resultid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setImagePickBeanList(List<ImagePickBean> imagePickBeanList) {
        this.imagePickBeanList = imagePickBeanList;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public List<ImagePickBean> getImagePickBeanList() {
        return imagePickBeanList;
    }


    public PhotoSelectorDataWrapper() {
    }

    public String getResultid() {
        return resultid;
    }

    public String getKey() {
        return key;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getItemid() {
        return itemid;
    }

    private String itemid;//详情 code



}
