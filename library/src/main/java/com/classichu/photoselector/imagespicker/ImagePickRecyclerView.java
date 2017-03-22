package com.classichu.photoselector.imagespicker;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.classichu.imageshow.bean.ImageShowBean;
import com.classichu.imageshow.helper.ImageShowDataHelper;
import com.classichu.photoselector.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by louisgeek on 2016/7/13.
 */
public class ImagePickRecyclerView extends RecyclerView {

    private static final String TAG = "ImagePickRecyclerView";
    List<ImagePickBean> mImagePickBeanList = new ArrayList<>();
    ImagePickRecyclerViewAdapter imagePickRecyclerViewAdapter;

    int mIp_maxPickCountFromXml;

    public static final int mOneColumnImageCount = 4;

    //String imageAllIdStrFromWeb="";
    public ImagePickRecyclerView(Context context) {
        this(context, null);
    }

    public ImagePickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImagePickRecyclerView);
        mIp_maxPickCountFromXml = typedArray.getInt(R.styleable.ImagePickRecyclerView_classic_maxPickCount, 0);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        imagePickRecyclerViewAdapter = new ImagePickRecyclerViewAdapter(mImagePickBeanList, mOneColumnImageCount);
        imagePickRecyclerViewAdapter.setOnAddClickInnerListener(new ImagePickRecyclerViewAdapter.OnAddClickInnerListener() {

            @Override
            public void onAddInnerClick(View view, int pickImageMaxCount, int hasImageCount) {
                if (onAddClickListener != null) {
                    onAddClickListener.onAddClick(view, pickImageMaxCount, hasImageCount);
                }
            }
        });
        imagePickRecyclerViewAdapter.setOnDeleteInnerClickListener(new ImagePickRecyclerViewAdapter.OnDeleteInnerClickListener() {
            @Override
            public void onDeleteInnerClick(View view, int pos) {
                mImagePickBeanList.remove(pos);
                Log.d("adapter", "onDeleteInnerClick: size:" + mImagePickBeanList.size() + ",pos:" + pos);
                imagePickRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        imagePickRecyclerViewAdapter.setOnItemClickListener(new ImagePickRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
                String pathNow = mImagePickBeanList.get(position).getImagePathOrUrl();
                int nowSelectedPos = 0;
                List<ImageShowBean> imageShowBeanList = new ArrayList<>();
                for (int i = 0; i < mImagePickBeanList.size(); i++) {
                    ImageShowBean isb = new ImageShowBean();
                    String imagePath = mImagePickBeanList.get(i).getImagePathOrUrl();
                    isb.setImageUrl(imagePath);
                    isb.setTitle("图片" + (i + 1));
                    if (pathNow.equals(imagePath)) {
                        nowSelectedPos = i;
                    }
                    imageShowBeanList.add(isb);
                }
                ImageShowDataHelper.setDataAndToImageShow(view.getContext(), imageShowBeanList, nowSelectedPos,false);


            }
        });
        this.setLayoutManager(new GridLayoutManager(context, mOneColumnImageCount));
        this.setAdapter(imagePickRecyclerViewAdapter);

        if (mIp_maxPickCountFromXml > 0) {
            this.setMaxImagePickCount(mIp_maxPickCountFromXml);
        }

    }

    private void setupDataList(List<ImagePickBean> imagePickBeanList) {
        for (int i = 0; i < imagePickBeanList.size(); i++) {
            ImagePickBean imagePickBean = imagePickBeanList.get(i);
            Log.d(TAG, "setupDataList: ==========================" + imagePickBean.getImageID());
            Log.d(TAG, "setupDataList: imagePickBeanList:" + imagePickBean.getImageName());
            Log.d(TAG, "setupDataList: imagePickBeanList:" + imagePickBean.getImagePathOrUrl());
            Log.d(TAG, "setupDataList: imagePickBeanList:" + imagePickBean.getImageWebIdStr());
            Log.d(TAG, "setupDataList: ==========================" + imagePickBean.getImagePickedTimeAndOrderTag());
        }
        mImagePickBeanList.clear();
        mImagePickBeanList.addAll(imagePickBeanList);
        imagePickRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void setupDataListStr(String listimageWebIDStrs, String imageUrlPathStrs) {
        Log.d("XXX", "setupDataListStr: upxxx:" + listimageWebIDStrs);
        Log.d("XXX", "setupDataListStr: upxxx:" + imageUrlPathStrs);
        List<ImagePickBean> imagePickBeanList = new ArrayList<>();
        if (listimageWebIDStrs != null && !listimageWebIDStrs.equals("") && !listimageWebIDStrs.equals("null")) {
            String[] listimageWebStrsArray = listimageWebIDStrs.split(",");
            String[] imageUrlPathStrsArray = imageUrlPathStrs.split("\\|");//|
            if (listimageWebStrsArray != null && listimageWebStrsArray.length > 0) {
                listimageWebStrsArray = removeArrayEmptyTextBackNewArray(listimageWebStrsArray);
            }
            if (imageUrlPathStrsArray != null && imageUrlPathStrsArray.length > 0) {
                imageUrlPathStrsArray = removeArrayEmptyTextBackNewArray(imageUrlPathStrsArray);
            }

            if (listimageWebStrsArray != null && listimageWebStrsArray.length > 0) {
                imagePickBeanList.clear();
                for (int i = 0; i < listimageWebStrsArray.length; i++) {
                    ImagePickBean imagePickBean = new ImagePickBean();
                    imagePickBean.setImageWebIdStr(listimageWebStrsArray[i]);
                    imagePickBean.setImagePathOrUrl(imageUrlPathStrsArray[i]);
                    imagePickBeanList.add(imagePickBean);
                }
            }
        }
        setupDataList(imagePickBeanList);
    }

    private String[] removeArrayEmptyTextBackNewArray(String[] strArray) {
        List<String> strList = Arrays.asList(strArray);
        List<String> strListNew = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i) != null && !strList.get(i).equals("")) {
                strListNew.add(strList.get(i));
            }
        }
        String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);
        return strNewArray;
    }

    public void setMaxImagePickCount(int maxImagePickCount) {
        imagePickRecyclerViewAdapter.setPickImageMaxCount(maxImagePickCount);
        // imagePickRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void setImageCanDelete(boolean isImageCanDelete) {
        imagePickRecyclerViewAdapter.setIsCanDelete(isImageCanDelete);
    }

    public void addDataList(List<ImagePickBean> imagePickBeanList) {
        mImagePickBeanList.addAll(imagePickBeanList);
        imagePickRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void updateDataList(List<ImagePickBean> imagePickBeanList) {
        Log.d("adapter", "updateDataList: imagePickBeanList:" + imagePickBeanList);
        Log.d("adapter", "updateDataList: mImagePickBeanList:" + mImagePickBeanList);
        mImagePickBeanList.clear();
        mImagePickBeanList.addAll(imagePickBeanList);
        imagePickRecyclerViewAdapter.notifyDataSetChanged();
    }

    public interface OnAddClickListener {
        void onAddClick(View view, int maxPickImageCount, int hasImageCount);
    }

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    OnAddClickListener onAddClickListener;

    public String getFileNameFormPath(String pathandname, boolean isHasExtension) {

        int start = pathandname.lastIndexOf("/");
        int end;
        if (isHasExtension) {
            end = pathandname.length();
        } else {
            end = pathandname.lastIndexOf(".");
        }
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }

    public void addClickBackToParseImagePickDataAndAppend(Intent dataIntent) {
        List<ImagePickBean> imagePickBeanList = new ArrayList<>();
        List<String> paths = dataIntent.getStringArrayListExtra("images");

        for (int i = 0; i < paths.size(); i++) {
            ImagePickBean imagePickBean = new ImagePickBean();
            imagePickBean.setImageName(this.getFileNameFormPath(paths.get(i), true));
            imagePickBean.setImagePathOrUrl(paths.get(i));
            // FIXME: 2017/3/21 imagePickBean.setImageTime(MyUtils.formatPhotoDate(paths.get(i),"yyyy-MM-dd HH:mm:ss"));
            imagePickBean.setImagePickedTimeAndOrderTag("the_" + i + 1 + "_at_" + System.currentTimeMillis());
            imagePickBeanList.add(imagePickBean);
        }

        this.addDataList(imagePickBeanList);
        // return  imagePickBeanList;
    }
 /*   public List<String>   getNeedUploadImageJsonList(){
        List<String> needUploadImageJsonList=new  ArrayList<>();
        if (mImagePickBeanList!=null&&mImagePickBeanList.size()>0){
            for (int i = 0; i < mImagePickBeanList.size(); i++) {
                ImagePickBean imagePickBean=mImagePickBeanList.get(i);
                String imageWebIDStr=imagePickBean.getImageWebIdStr();
                if (imageWebIDStr==null||imageWebIDStr.equals("")){
                    //有空的就是要上传的
                    String dataJson= "{\"base64ImgsJsonStr\":"+ ImageUtil.getBase64ImgsJsonStr(imagePickBean.getImagePath(),imagePickBean.getImageTime())+"}";
                    needUploadImageJsonList.add(dataJson);
                }
            }
        }
        return  needUploadImageJsonList;
    }*/
/*
    public List<ImagePickBean>   getNeedUploadImageList(){
        List<ImagePickBean> imagePickBeanList=new ArrayList<>();
        if (mImagePickBeanList!=null&&mImagePickBeanList.size()>0){
            for (int i = 0; i < mImagePickBeanList.size(); i++) {
                ImagePickBean imagePickBean=mImagePickBeanList.get(i);
                String imageWebIDStr=imagePickBean.getImageWebIdStr();
                if (imageWebIDStr==null||imageWebIDStr.equals("")){
                    //有空的就是要上传的
                     imagePickBeanList.add(imagePickBean);
                }
            }
        }
        return  imagePickBeanList;
    }
*/

    public int getNeedUploadImageCount() {
        //遍历
        int count = 0;
        for (int i = 0; i < mImagePickBeanList.size(); i++) {
            ImagePickBean imagePickBean = mImagePickBeanList.get(i);
            String imageWebIdStr = imagePickBean.getImageWebIdStr();
            if (imageWebIdStr == null || imageWebIdStr.equals("")) {
                count++;
            }
        }
        return count;
    }

    public List<ImagePickBean> getImagePickBeanListData() {
        //只得数据
        List<ImagePickBean> imagePickBeanList = new ArrayList<>(mImagePickBeanList);
        return imagePickBeanList;
    }

    public String getImageWebIdStrAll() {
        String imageWebIdStrAll = "";
        StringBuilder sb = new StringBuilder();
        if (mImagePickBeanList != null && mImagePickBeanList.size() > 0) {
            for (int i = 0; i < mImagePickBeanList.size(); i++) {
                String webIdStr = mImagePickBeanList.get(i).getImageWebIdStr();
                if (webIdStr != null && !webIdStr.equals("") && !webIdStr.toLowerCase().equals("null")) {
                    sb.append(webIdStr);
                    sb.append(",");
                }
            }
        }
        String str = sb.toString();
        Log.d("recyclerview", "getImageWebIdStrAll: 开始 str:" + str);
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);//去掉最后一个","
        }
        Log.d("recyclerview", "getImageWebIdStrAll: 结束 str:" + str);
        if (str != null) {
            imageWebIdStrAll = str;
        }
        return imageWebIdStrAll;
    }
   /* public String getImagePathStrAll() {
        StringBuilder sb=new StringBuilder();
        if (mImagePickBeanList!=null&&mImagePickBeanList.size()>0) {
            for (int i = 0; i < mImagePickBeanList.size(); i++) {
                String imagePath = mImagePickBeanList.get(i).getImagePath();
                sb.append(imagePath);
                sb.append("|");
            }
        }
        return sb.toString();
    }*/

   /* public void setImageAllIdStrFromWeb(String imageAllIdStrFromWeb) {
        this.imageAllIdStrFromWeb = imageAllIdStrFromWeb;
    }*/
}
