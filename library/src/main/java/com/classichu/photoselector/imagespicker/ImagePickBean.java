package com.classichu.photoselector.imagespicker;

import java.io.Serializable;

/**
 * Created by louisgeek on 2016/7/13.
 */
public class ImagePickBean implements Serializable{
    private int imageID;
    private String imageWebIdStr;
    private String imageName;
    private String imageTime;
    private String imagePathOrUrl;
    private String imageResID;
    private String imagePickedTimeAndOrderTag;//不同时间标记

    public String getImagePickedTimeAndOrderTag() {
        return imagePickedTimeAndOrderTag;
    }

    public void setImagePickedTimeAndOrderTag(String imagePickedTimeAndOrderTag) {
        this.imagePickedTimeAndOrderTag = imagePickedTimeAndOrderTag;
    }

    public String getImagePathOrUrl() {
        return imagePathOrUrl;
    }

    public void setImagePathOrUrl(String imagePathOrUrl) {
        this.imagePathOrUrl = imagePathOrUrl;
    }

    public String getImageWebIdStr() {
        return imageWebIdStr;
    }

    public void setImageWebIdStr(String imageWebIdStr) {
        this.imageWebIdStr = imageWebIdStr;
    }



    public String getImageTime() {
        return imageTime;
    }

    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }



    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }




    public String getImageResID() {
        return imageResID;
    }

    public void setImageResID(String imageResID) {
        this.imageResID = imageResID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
