/*
package com.classichu.photoselector.imagespicker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.louisgeek.httplib.OkHttpClientSingleton;
import com.louisgeek.httplib.bean.BaseListBean;
import com.louisgeek.httplib.bean.UploadBase64ImgsWithWeatherBean;
import com.louisgeek.httplib.callback.SimpleGsonOkHttpCallback;
import com.louisgeek.httplib.util.ImageUtil;
import com.sunstar.cloudseeds.fragment.MyDialogFragmentGrid;
import com.sunstar.cloudseeds.fragment.MyDialogFragmentProgress;
import com.sunstar.cloudseeds.httpRequest.Url;
import com.sunstar.cloudseeds.model.UploadImageBackModel;
import com.sunstar.cloudseeds.tools.InfoHolderSingleton;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by louisgeek on 2016/7/14.
 *//*

public abstract class ImagePickUploadQueueTool {

    private static final String TAG = "ImagePickUploadTool";
    private Context mContext;
    private UploadBase64ImgsWithWeatherBean mUploadBase64ImgsWithWeatherBean;
    private int mUserID;
    private boolean mIsHasWeather;
    */
/**
     * 传入的 UploadBase64ImgsWithWeatherBean 的子List，内部自动填充   所以只需要传入Bean其本身的数据
     *
     * @param context
     * @param queueNameNotTheSame
     * @param thePreviousDataString
     * @param imagePickRecyclerView
     * @param v4FragmentManager
     * @param progressDialogName
     * @param isHasWeather
     * @param uploadBase64ImgsWithWeatherBean
     *//*

    public ImagePickUploadQueueTool(Context context,
                                    String queueNameNotTheSame,
                                    String thePreviousDataString,
                                    ImagePickRecyclerView imagePickRecyclerView,
                                    FragmentManager v4FragmentManager,
                                    String progressDialogName,
                                    boolean isHasWeather,
                                    UploadBase64ImgsWithWeatherBean uploadBase64ImgsWithWeatherBean
    ) {
        mContext = context;
        mImagePickRecyclerView = imagePickRecyclerView;
        V4FragmentManager = v4FragmentManager;
        mQueueNameNoSame = queueNameNotTheSame;
        mThePreviousDataString = thePreviousDataString;
        mProgressDialogName = progressDialogName;
        mIsHasWeather = isHasWeather;
        mUploadBase64ImgsWithWeatherBean = uploadBase64ImgsWithWeatherBean;
    }

    public ImagePickUploadQueueTool(Context context, String queueNameNotTheSame,
                                    String thePreviousDataString,
                                    ImagePickRecyclerView imagePickRecyclerView,
                                    FragmentManager v4FragmentManager, String progressDialogName,
                                    int userID,
                                    boolean isHasWeather
    ) {
        mContext = context;
        mImagePickRecyclerView = imagePickRecyclerView;
        V4FragmentManager = v4FragmentManager;
        mQueueNameNoSame = queueNameNotTheSame;
        mThePreviousDataString = thePreviousDataString;
        mProgressDialogName = progressDialogName;
        mIsHasWeather = isHasWeather;
        mUserID = userID;
    }

    // private Activity mActivity;
    private ImagePickRecyclerView mImagePickRecyclerView;
    private MyDialogFragmentProgress mMyDialogFragmentProgress;
    private android.support.v4.app.FragmentManager V4FragmentManager;
    private String mQueueNameNoSame;
    private String mProgressDialogName;

    private String mThePreviousDataString;
    */
/**
     *   String paramsStr="baseid="+jiDiInfoModel.getBaseid()+"&identitycardimages="+webIDS+"&businesslicenceimages=286";
     *//*



    */
/**
     * 【上传图片】1.开始上传
     *//*

    public void uploadImageQueue_Start() {
        //1.存起来
        List<ImagePickBean> imagePickBeanList = mImagePickRecyclerView.getImagePickBeanListData();
        InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LIST_ + mQueueNameNoSame,
                imagePickBeanList);
        InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_NEW_WEB_IDS_ + mQueueNameNoSame, "");
        //测试用
        for (int i = 0; i < imagePickBeanList.size(); i++) {
            ImagePickBean imagePickBean = imagePickBeanList.get(i);
            Log.d(TAG, mQueueNameNoSame + "上传开始: ==========================" + imagePickBean.getImageID());
            Log.d(TAG, mQueueNameNoSame + "上传开始: imagePickBeanList:" + imagePickBean.getImageName());
            Log.d(TAG, mQueueNameNoSame + "上传开始: imagePickBeanList:" + imagePickBean.getImagePathOrUrl());
            Log.d(TAG, mQueueNameNoSame + "上传开始: imagePickBeanList:" + imagePickBean.getImageWebIdStr());
            Log.d(TAG, mQueueNameNoSame + "上传开始: ==========================" + imagePickBean.getImagePickedTimeAndOrderTag());
        }
        int needUpdateCount = mImagePickRecyclerView.getNeedUploadImageCount();
        if (needUpdateCount != 0) {
            InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_COUNT_ + mQueueNameNoSame,
                    needUpdateCount);
            InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LEFT_COUNT_ + mQueueNameNoSame,
                    needUpdateCount);
            if (mMyDialogFragmentProgress != null) {
                mMyDialogFragmentProgress.dismiss();
            }
            mMyDialogFragmentProgress = MyDialogFragmentProgress.newInstance("", mProgressDialogName);
            mMyDialogFragmentProgress.show(V4FragmentManager, "mMyDialogFragmentProgress");

            uploadImageQueue_ExecueQueue();//2.执行上传队列
        } else {
            //  ToastUtil.show(mContext,"没有需要上传的图片",1);
            Log.d(TAG, mQueueNameNoSame + "没有需要上传的图片");
           */
/* String webIDS="";
            for (int i = 0; i < imagePickBeanList.size(); i++) {
                String tempIDStr=imagePickBeanList.get(i).getImageWebIdStr();
                if (tempIDStr!=null&&!tempIDStr.equals("")){
                    webIDS=webIDS+tempIDStr+",";
                }
            }*//*

            // InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_NEW_WEB_IDS_+mQueueNameNoSame,webIDS);

            uploadImageQueue_UpdatePicIntoInformation();
        }
    }


    */
/**
     * 【上传图片】2.执行上传
     *//*

    private void uploadImageQueue_ExecueQueue() {
        Log.d(TAG, "execueImageQueue xxx");
        //
        //取出来
        List<ImagePickBean> needImagePickBeanList = getLeftImageListFromInstance();

        if (needImagePickBeanList != null && needImagePickBeanList.size() > 0) {
            for (int i = 0; i < needImagePickBeanList.size(); i++) {
                ImagePickBean imagePickBean = needImagePickBeanList.get(i);
                //执行
                uploadImageQueue_UploadLeftImages(imagePickBean.getImageName(), imagePickBean.getImagePathOrUrl(), imagePickBean.getImageTime(), imagePickBean.getImagePickedTimeAndOrderTag());
                Log.d(TAG, "execueImageQueue: uploadLeftImages:" + imagePickBean.getImageName());
                break;//一个就结束
            }
        }
    }

    */
/**
     * 【上传图片】3.上传图片
     *
     * @param imageName
     * @param imagePath
     * @param imageTime
     * @param imagePickedTimeAndOrderTag
     *//*

    private void uploadImageQueue_UploadLeftImages(final String imageName, final String imagePath, final String imageTime, final String imagePickedTimeAndOrderTag) {
        Log.d(TAG, "uploadLeftImages: imageName:" + imageName);
        Log.d(TAG, "uploadLeftImages: imagePath:" + imagePath);
        Log.d(TAG, "uploadLeftImages: imageTime:" + imageTime);
        final SimpleGsonOkHttpCallback<BaseListBean<UploadImageBackModel>> simpleGsonOkHttpCallback = new SimpleGsonOkHttpCallback<BaseListBean<UploadImageBackModel>>() {

            @Override
            public BaseListBean<UploadImageBackModel> OnSuccess(String result, int statusCode) {
                Log.d(TAG, "OnSuccess: result:" + result);
                TypeToken<BaseListBean<UploadImageBackModel>> typeToken = new TypeToken<BaseListBean<UploadImageBackModel>>() {
                };
                return BaseListBean.fromJsonFive(result, typeToken);
            }

            @Override
            public void OnSuccessNotifyUI(BaseListBean<UploadImageBackModel> uploadImageBackModelBaseListBean) {
                super.OnSuccessNotifyUI(uploadImageBackModelBaseListBean);
                String idStr = uploadImageBackModelBaseListBean.getInfo().get(0).getAttachmentid() + "";
                //   String Attname=uploadImageBackModelBaseListBean.getInfo().get(0).getAttname();
                //  String path=uploadImageBackModelBaseListBean.getInfo().get(0).getPath()
                // String microimagefilename=uploadImageBackModelBaseListBean.getInfo().get(0).getMicroimagefilename();
                Log.d(TAG, "OnSuccessNotifyUI: id:" + idStr);
                //id_iprv_frsfzImagePickRecyclerView.updateDataList();
                uploadImageQueue_FinishToUpdateData(idStr, imageName, imagePath, imagePickedTimeAndOrderTag);

            }

            @Override
            public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                super.OnErrorNotifyUI(errorMsg, statusCode);

                //取到所有上传失败的图
                List<ImagePickBean> leftImageListFromInstance = getLeftImageListFromInstance();
                List<String> stringList=new ArrayList<>();
                for (int i = 0; i < leftImageListFromInstance.size(); i++) {
                    String leftImagePathStr = leftImageListFromInstance.get(i).getImagePathOrUrl();
                    stringList.add(leftImagePathStr);
                }

                MyDialogFragmentGrid myDialogFragmentGrid=MyDialogFragmentGrid.newInstance("以下图片上传失败","是否重新上传?");
                myDialogFragmentGrid.show(V4FragmentManager,"myDialogFragmentGrid");
                InfoHolderSingleton.getInstance().putMapObj("dialog_mStringList",stringList);
                myDialogFragmentGrid.setOnBtnClickListener(new MyDialogFragmentGrid.OnBtnClickListener() {
                    @Override
                    public void onBtnClickOk(DialogInterface dialog, int which) {
                        uploadImageQueue_Start();
                    }

                    @Override
                    public void onBtnClickCancel(DialogInterface dialog, int which) {
                        //nothing
                        if (mMyDialogFragmentProgress != null) {
                            mMyDialogFragmentProgress.dismiss();
                        }
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                uploadImageQueue_UpdatePicIntoInformation();//有部分失败，继续传
                            }
                        });
                    }
                });
            }
        };



                if (mIsHasWeather) {
                    String hasWeatherDataJson = "{\"plantimgsjsonstr\":" + ImageUtil.getBase64WithWeatherImgsJsonStr(imagePath, imageTime, mUploadBase64ImgsWithWeatherBean) + "}";
                    Log.d(TAG, "uploadxxx hasWeatherDataJson:" + hasWeatherDataJson);
                    OkHttpClientSingleton.getInstance()
                            .doPostAsyncJson(Url.IMAGE_UPDATE_WITH_WEATHER, hasWeatherDataJson, simpleGsonOkHttpCallback);
                } else {
                    String dataJson = "{\"base64ImgsJsonStr\":" + ImageUtil.getBase64ImgsJsonStr(imagePath, imageTime, mUserID) + "}";
                    Log.d(TAG, "uploadxxx dataJson:" + dataJson);
                    OkHttpClientSingleton.getInstance()
                            .doPostAsyncJson(Url.UPLOAD_IMAGES, dataJson, simpleGsonOkHttpCallback);
                }
    }


    private void uploadImageQueue_FinishToUpdateData(String imagesIdStr, String imageName, String imageUrl, String imagePickedTimeAndOrderTag) {
        int allCount = (int) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_COUNT_ + mQueueNameNoSame);
        int leftCount = (int) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LEFT_COUNT_ + mQueueNameNoSame);
        int lefCountNew = leftCount - 1;
        //取出 webIDS
        String webIDS = (String) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_NEW_WEB_IDS_ + mQueueNameNoSame);
        String webIDSNew = webIDS + imagesIdStr + ",";
        //刷新  新的webIDS
        InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_NEW_WEB_IDS_ + mQueueNameNoSame, webIDSNew);
        Log.d(TAG, mQueueNameNoSame + "finishToUpdateData: 当前需要上传总数:" + allCount + "，剩余:" + lefCountNew);
        if (allCount != 0) {
            int progress = (int) ((allCount - lefCountNew) * 100.0 / allCount);
            Log.d(TAG, "finishToUpdateData: progress：" + progress);
            mMyDialogFragmentProgress.updateProgress(allCount, progress);

        }
        InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LEFT_COUNT_ + mQueueNameNoSame, lefCountNew);

        List<ImagePickBean> imagePickBeanList = null;
        if (imageName != null && !imageName.equals("")) {
            imagePickBeanList = (List<ImagePickBean>) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LIST_ + mQueueNameNoSame);

            //更新  instance 内
            for (int i = 0; i < imagePickBeanList.size(); i++) {
                ImagePickBean imagePickBean = imagePickBeanList.get(i);
                if (imageName.equals(imagePickBean.getImageName()) && imagePickedTimeAndOrderTag.equals(imagePickBean.getImagePickedTimeAndOrderTag())) {
                    imagePickBeanList.get(i).setImageWebIdStr(imagesIdStr);
                    imagePickBeanList.get(i).setImagePathOrUrl(imageUrl);

                    break;
                }
            }
            //刷新  //更新  instance 内
            InfoHolderSingleton.getInstance().putMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LIST_ + mQueueNameNoSame, imagePickBeanList);
            //更新  RecyclerView 内
            //mImagePickRecyclerView.updateDataList(imagePickBeanList);

        }
        if (lefCountNew <= 0) {
            // ToastUtil.show(mContext,"上传完成",1);
            Log.d(TAG, mQueueNameNoSame + "上传完成");
            if (mMyDialogFragmentProgress != null) {
                mMyDialogFragmentProgress.updateAddFinshText();
            }
            if (imagePickBeanList != null) {
                for (int i = 0; i < imagePickBeanList.size(); i++) {
                    ImagePickBean imagePickBean = imagePickBeanList.get(i);
                    Log.d(TAG, mQueueNameNoSame + "上传完成: ===============:" + imagePickBean.getImageID());
                    Log.d(TAG, mQueueNameNoSame + "上传完成: imagePickBeanList:" + imagePickBean.getImageName());
                    Log.d(TAG, mQueueNameNoSame + "上传完成: imagePickBeanList:" + imagePickBean.getImagePathOrUrl());
                    Log.d(TAG, mQueueNameNoSame + "上传完成: imagePickBeanList:" + imagePickBean.getImageWebIdStr());
                    Log.d(TAG, mQueueNameNoSame + "上传完成: ===============:" + imagePickBean.getImagePickedTimeAndOrderTag());
                }
                //更新  RecyclerView 内
                mImagePickRecyclerView.updateDataList(imagePickBeanList);

                //延迟2秒消失
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMyDialogFragmentProgress != null) {
                            mMyDialogFragmentProgress.dismiss();
                        }
                        //
                        uploadImageQueue_UpdatePicIntoInformation();
                    }
                }, 2000);


            }
        }
        //切记在UI线程中直接执行  网络请求 在子线程
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                uploadImageQueue_ExecueQueue();//继续执行
            }
        });
    }

    private void uploadImageQueue_UpdatePicIntoInformation() {
        String webIDS = (String) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_NEW_WEB_IDS_ + mQueueNameNoSame);
        Log.d("updatePic", "updatePic: 开始 webIDS:" + webIDS);
        if (webIDS.endsWith(",")) {
            webIDS = webIDS.substring(0, webIDS.length() - 1);//去掉最后一个","
        }
        Log.d("updatePic", "updatePic: 结束 webIDS:" + webIDS);
        uploadImageQueue_Complete(mThePreviousDataString, webIDS);
      */
/*  SimpleGsonOkHttpCallback<BaseListBean> simpleGsonOkHttpCallback=new SimpleGsonOkHttpCallback<BaseListBean>() {
            @Override
            public BaseListBean OnSuccess(String result, int statusCode) {
                TypeToken<BaseListBean> typeToken=new TypeToken<BaseListBean>(){};
                return BaseListBean.fromJsonFive(result,typeToken);
            }

            @Override
            public void OnSuccessNotifyUI(BaseListBean baseListBean) {
                super.OnSuccessNotifyUI(baseListBean);
                Log.d(TAG, "OnSuccessNotifyUI: "+baseListBean.getMessage());
                ToastUtil.show(mContext,"已上传的图片保存成功",1);

            }
            @Override
            public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                super.OnErrorNotifyUI(errorMsg, statusCode);
                ToastUtil.show(mContext,"已上传的图片保存失败:"+errorMsg,1);
            }
        };

        String urlParamsStr=urlParamsStrCutOne+"&identitycardimages="+webIDS+"&businesslicenceimages=286";
        OkHttpClientSingleton.getInstance().doPostAsync(Url.BASE_IMAGE_UPDATE_AFTER_UPLOAD,urlParamsStr,simpleGsonOkHttpCallback);*//*

    }

  /*  protected abstract void uploadImageQueue_Complete(String thePreviousData, String webIDS);

    //==============================Tools ========================================
    @NonNull
    private List<ImagePickBean> getLeftImageListFromInstance() {
        List<ImagePickBean> imagePickBeanList = (List<ImagePickBean>) InfoHolderSingleton.getInstance().getMapObj(InfoHolderSingleton.KEY_IMAGES_QUEUE_LIST_ + mQueueNameNoSame);

        List<ImagePickBean> needImagePickBeanList = new ArrayList<>();
        if (imagePickBeanList != null && imagePickBeanList.size() > 0) {
            //遍历
            for (int i = 0; i < imagePickBeanList.size(); i++) {
                ImagePickBean imagePickBean = imagePickBeanList.get(i);
                String imageWebIdStr = imagePickBean.getImageWebIdStr();
                if (imageWebIdStr == null || imageWebIdStr.equals("")) {
                    needImagePickBeanList.add(imagePickBean);
                }
            }
        }
        return needImagePickBeanList;
    }
*/

//}

