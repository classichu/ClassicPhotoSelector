package com.classichu.photoselector.customselector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.classichu.imageshow.bean.ImageShowBean;
import com.classichu.imageshow.helper.ImageShowDataHelper;
import com.classichu.photoselector.R;
import com.classichu.photoselector.customselector.adapter.ImagePickerDirListAdapter;
import com.classichu.photoselector.customselector.adapter.ImagePickerListAdapter;
import com.classichu.photoselector.customselector.bean.ImagePickerBean;
import com.classichu.photoselector.customselector.bean.ImagePickerDataWrapper;
import com.classichu.photoselector.customselector.bean.ImagePickerDirBean;
import com.classichu.photoselector.customselector.bean.ImagePickerDirDataWrapper;
import com.classichu.photoselector.customselector.helper.ImagePickerHelper;
import com.classichu.photoselector.tool.ViewTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPhotoSelectorActivity extends AppCompatActivity {

    /**
     * 图片适配器
     */
    private ImagePickerListAdapter mImagePickerListAdapter;

    private int mRecyclerviewDirHeight;

    /**
     * 当前文件夹下的的图片  adapter主用数据源
     */
    private List<ImagePickerBean> mNowDirImageList = new ArrayList<>();
    /**
     * 所有的图片
     */
    private List<ImagePickerBean> mAllImageList = new ArrayList<>();
    /**
     * 所有的文件夹
     */
    private List<ImagePickerDirBean> mImageDirBeanList = new ArrayList<>();
    /**
     * 虚拟的存放所有图片的文件夹
     */
    private final String ALL_IMAGES_DIR_PATH_KEY = "所有图片";
    /**
     * 当前
     */
    private String mNowDirPathKey = ALL_IMAGES_DIR_PATH_KEY;//默认是所有图片

    /**
     * 已选择的图片
     */
    ///private List<String> mSelectedPhotoList = new ArrayList<>();

    /**
     * raw  data
     */
    private Map<String, List<String>> mGroupMap = new HashMap<>();

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewDir;
    private ImagePickerDirListAdapter mImagePickerDirListAdapter;

    private int mMaxSelectCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photoselector);

        Bundle bundle = null;
        if (this.getIntent() != null) {
            bundle = this.getIntent().getExtras();
        }
        if (bundle != null) {
           mIsToolbarTitleCenter = bundle.getBoolean("isToolbarTitleCenter");
            mMaxSelectCount = bundle.getInt("maxSelectCount");
        }


        initToolbar();

        setToolbarTitle("图片选择");

        iniRecyclerView();
        iniRecyclerViewDir();
        gainData();

    }

    private void iniRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        /**
         * 覆盖baseAty的LinearLayoutManager
         */
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        /**
         * 因为NestedScrollView
         * 初始化时候防止测量   回调ALL 卡死和OOM
         */
        //### mRecyclerView.getLayoutManager().setAutoMeasureEnabled(false);
        /**
         *
         */

        mImagePickerListAdapter = new ImagePickerListAdapter(mNowDirImageList,mMaxSelectCount);
        mImagePickerListAdapter.setOnItemClickListener(new ImagePickerListAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(int selectedCount) {
                super.onItemSelected(selectedCount);

                //更新标题栏
                setToolbarTitle(selectedCount + "/"+mMaxSelectCount);
            }

            @Override
            public void onItemClick(View view, int position, ImagePickerBean imagePickerBean) {
                super.onItemClick(view, position, imagePickerBean);

                String pathNow = imagePickerBean.getPath();
                int nowSelectedPos = 0;
                List<ImageShowBean> imageShowBeanList = new ArrayList<>();
                for (int i = 0; i < mNowDirImageList.size(); i++) {
                    ImageShowBean isb = new ImageShowBean();
                    String imagePath = mNowDirImageList.get(i).getPath();
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

        mRecyclerView.setAdapter(mImagePickerListAdapter);
    }

    private void gainDataBack(ImagePickerDirDataWrapper imagePickerDataWrapper) {
        mGroupMap = imagePickerDataWrapper.getGroupMap();
        /**
         * 刷新文件夹数据
         */
        mImageDirBeanList.clear();
        mAllImageList.clear();

        ImagePickerDirBean showAllImageItemBean = null;//虚拟一个存放所有图片的文件夹
        for (String dirPathKey : mGroupMap.keySet()) {

            /**
             * 存放所有图片
             */
            List<String> nowDirImageList = mGroupMap.get(dirPathKey);
            if (nowDirImageList != null && nowDirImageList.size() > 0) {
                for (int i = 0; i < nowDirImageList.size(); i++) {
                    String path = dirPathKey + File.separator + nowDirImageList.get(i);
                    ImagePickerBean imagePickerBean = new ImagePickerBean();
                    imagePickerBean.setPath(path);
                    mAllImageList.add(imagePickerBean);
                }
            }
            /**
             * 单独存放图片
             */
            ImagePickerDirBean imageDirBean = new ImagePickerDirBean();
            imageDirBean.setDirPath(dirPathKey);
            // KLog.d("dir path" + dirPathKey);
            String first = nowDirImageList.get(0);
            // KLog.d("first Image" + first);
            imageDirBean.setDirImageIconPath(dirPathKey + File.separator + first);
            imageDirBean.setImageCount(nowDirImageList.size());
            mImageDirBeanList.add(imageDirBean);

            if (showAllImageItemBean == null) {
                //所有图片的文件夹
                showAllImageItemBean = new ImagePickerDirBean();
                showAllImageItemBean.setDirImageIconPath(dirPathKey + File.separator + first);
            }
        }
        // KLog.d("ASD 222");

        /**
         * 添加到mImageDirBeanList的最前面
         */
        showAllImageItemBean.setDirPath(ALL_IMAGES_DIR_PATH_KEY);
        showAllImageItemBean.setImageCount(mAllImageList.size());
        mImageDirBeanList.add(0, showAllImageItemBean);

        /**
         * 刷新文件夹List
         */
        mImagePickerDirListAdapter.notifyDataSetChanged();

        /**
         * 数据初始化完后 测量高度   当然 mRecyclerViewDir 是 wrap-content
         */
        mRecyclerviewDirHeight = ViewTool.getMeasuredHeightMy(mRecyclerViewDir);

        /**
         * 偏移 到看不见的顶部
         */
        mRecyclerViewDir.setTranslationY(-mRecyclerviewDirHeight);


        /**
         * 刷新图片数据
         */

        refreshImageData();
    }

    private void gainData() {
        ImagePickerHelper.scanLocalImage(this, new ImagePickerHelper.ScanLocalImagesCallBack() {
            @Override
            public void onSuccess(Map<String, List<String>> groupMap) {
                ImagePickerDirDataWrapper imagePickerDataWrapper = new ImagePickerDirDataWrapper();
                imagePickerDataWrapper.setGroupMap(groupMap);
                gainDataBack(imagePickerDataWrapper);
            }

            @Override
            public void onError(String message) {
                // baseCallBack.onError(message);
            }
        });
    }

    private void iniRecyclerViewDir() {
        mRecyclerViewDir = (RecyclerView) findViewById(R.id.id_recycler_view_dir);
        /**
         *
         */
        mRecyclerViewDir.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewDir.setHasFixedSize(true);
        mRecyclerViewDir.setItemAnimator(new DefaultItemAnimator());
        //###mRecyclerViewDir.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        //初始化
        // mRecyclerViewDir.setNestedScrollingEnabled(false);

        mImagePickerDirListAdapter = new ImagePickerDirListAdapter(mImageDirBeanList);

        mImagePickerDirListAdapter.setOnItemClickListener(new ImagePickerDirListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position, ImagePickerDirBean imagePickerDirBean) {


                if (mImagePickerDirListAdapter.getLastClickDirItemChildView() != null) {
                    mImagePickerDirListAdapter.getLastClickDirItemChildView().setVisibility(View.GONE);
                }
                View imv = view.findViewById(R.id.id_tv_item_image_more);
                imv.setVisibility(View.VISIBLE);
                mImagePickerDirListAdapter.setLastClickDirItemChildView(imv);

                /**
                 *
                 */
                imageDirOperator();
                if (mNowDirPathKey.equals(imagePickerDirBean.getDirPath())) {
                    return;
                }
                mNowDirPathKey = imagePickerDirBean.getDirPath();
                /**
                 *
                 */
                refreshImageData();


            }
        });


        mRecyclerViewDir.setAdapter(mImagePickerDirListAdapter);

    }


    private void refreshImageData() {

        mNowDirImageList.clear();
        if (mNowDirPathKey.equals(ALL_IMAGES_DIR_PATH_KEY)) {
            //
            mNowDirImageList.addAll(mAllImageList);
        } else {
            List<String> stringList = mGroupMap.get(mNowDirPathKey);
            if (stringList != null && stringList.size() > 0) {
                for (int i = 0; i < stringList.size(); i++) {
                    String pathTemp = mNowDirPathKey + File.separator + stringList.get(i);
                    ImagePickerBean imagePickerBean = new ImagePickerBean();
                    imagePickerBean.setPath(pathTemp);
                    mNowDirImageList.add(imagePickerBean);
                }
            }
        }
        //KLog.d("ASD 333");
        mImagePickerListAdapter.notifyDataSetChanged();

    }

    private TextView mToolbarTitleView;
    private Toolbar mToolbar;
    private boolean mIsToolbarTitleCenter=true;

    protected void setToolbarTitle(String string) {
        if (mIsToolbarTitleCenter) {
            if (mToolbarTitleView != null) {
                mToolbarTitleView.setText(string);
                mToolbar.setTitle("");
                mToolbarTitleView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mToolbarTitleView != null) {
                mToolbarTitleView.setText("");
                mToolbar.setTitle(string);
                mToolbarTitleView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 是否正在显示
     */
    private boolean isAnimationIng;
    private boolean isDirShow;

    private void hideImageDir() {
        isAnimationIng = true;
        //-mRecyclerviewDirHeight
        mRecyclerViewDir.animate().translationY(-mRecyclerviewDirHeight).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationIng = false;
                isDirShow = false;
            }
        });
    }

    private void showImageDir() {

        isAnimationIng = true;
        /**
         * 坐标0
         */
        mRecyclerViewDir.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationIng = false;
                isDirShow = true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.id_menu_dir) {
            imageDirOperator();
        } else if (i == R.id.id_menu_finish) {
            imagePickFinishOperator();
        }

        return super.onOptionsItemSelected(item);
    }

    private void imageDirOperator() {
        if (isAnimationIng) {
            return;
        }
        if (isDirShow) {
            hideImageDir();
        } else {
            showImageDir();
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        if (mToolbar == null) {
            return;
        }
        mToolbarTitleView = (TextView) mToolbar.findViewById(R.id.id_toolbar_title);

        /**
         * setToolbarTitle
         */
        this.setToolbarTitle(mToolbar.getTitle() != null ? mToolbar.getTitle().toString() : "");

        mToolbar.setVisibility(View.VISIBLE);
        //替换ActionBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //必须设置在setSupportActionBar(mToolbar);后才有效
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //结束当前aty
                finish();
            }
        });
    }


    private void imagePickFinishOperator() {
        List<String> selectedImageList = mImagePickerListAdapter.getSelectedImageList();
        ImagePickerDataWrapper imagePickerDataWrapper = new ImagePickerDataWrapper();
        imagePickerDataWrapper.setSelectedImageList(selectedImageList);
        Intent intent = new Intent();
        intent.putExtra("imagePickerDataWrapper", imagePickerDataWrapper);
        //
        setResult(RESULT_OK, intent);
        finish();

    }
}
