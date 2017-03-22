package com.classichu.photoselector.customselector.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by louisgeek on 2016/12/12.
 */

public class ImagePickerDataWrapper implements Serializable {
    public List<String> getSelectedImageList() {
        return selectedImageList;
    }

    public void setSelectedImageList(List<String> selectedImageList) {
        this.selectedImageList = selectedImageList;
    }

    private List<String> selectedImageList;


}
