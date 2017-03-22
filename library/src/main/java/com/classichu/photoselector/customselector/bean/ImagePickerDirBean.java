package com.classichu.photoselector.customselector.bean;

/**
 * Created by louisgeek on 2016/11/17.
 */

public class ImagePickerDirBean {
    private String dirPath;

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int index = dirPath.lastIndexOf("/");
        if (index < 0) {
            this.dirName = dirPath;
            return;
        }
        this.dirName = dirPath.substring(index);
    }

    public String getDirImageIconPath() {
        // KLog.d("getDirImageIconPath:"+dirImageIconPath);
        return dirImageIconPath;
    }

    public void setDirImageIconPath(String dirImageIconPath) {
        this.dirImageIconPath = dirImageIconPath;
    }

    public String getDirName() {
        return dirName;
    }

    private String dirName;
    private String dirImageIconPath;

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    private int ImageCount;

}
