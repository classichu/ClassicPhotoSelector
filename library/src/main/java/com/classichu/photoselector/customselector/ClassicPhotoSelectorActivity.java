package com.classichu.photoselector.customselector;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.classichu.photoselector.R;
import com.classichu.photoselector.bean.PhotoSelectorDataWrapper;
import com.classichu.photoselector.customselector.bean.ImagePickerDataWrapper;
import com.classichu.photoselector.fragment.ClassicDialogFragment;
import com.classichu.photoselector.imagespicker.ImagePickBean;
import com.classichu.photoselector.imagespicker.ImagePickRecyclerView;
import com.classichu.photoselector.listener.OnNotFastClickListener;

import java.util.ArrayList;
import java.util.List;

public class ClassicPhotoSelectorActivity extends AppCompatActivity {
    private ImagePickRecyclerView iprv;
    private int mMaxImagePickCount = 3;
    private String mImagePickKey="";

    private static final int REQUEST_CODE_SELECT_PHOTO = 44544;
    private PhotoSelectorDataWrapper mPhotoSelectorDataWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_photo_selector);

        Bundle bundle = null;
        if (this.getIntent() != null) {
            bundle = this.getIntent().getExtras();
        }
        if (bundle != null) {
            mIsToolbarTitleCenter = bundle.getBoolean("isTitleCenter");
            mPhotoSelectorDataWrapper = (PhotoSelectorDataWrapper)
                    bundle.getSerializable("photoSelectorDataWrapper");
            if (mPhotoSelectorDataWrapper!=null){
            mMaxImagePickCount =mPhotoSelectorDataWrapper.getMaxPickCount();
            mImagePickKey =mPhotoSelectorDataWrapper.getImagePickKey();}
        }

        initToolbar();

        setToolbarTitle("图片选择");


        Button id_btn_show_save = (Button) findViewById(R.id.id_btn_show_save);
        id_btn_show_save.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {

                saveImageOpear();
            }
        });

        iprv = (ImagePickRecyclerView) findViewById(R.id.id_iprv);
        iprv.setMaxImagePickCount(mMaxImagePickCount);
        //
        if (mPhotoSelectorDataWrapper!=null&&mPhotoSelectorDataWrapper.getImagePickBeanList() != null
                && mPhotoSelectorDataWrapper.getImagePickBeanList().size() > 0) {
           iprv.addDataList(mPhotoSelectorDataWrapper.getImagePickBeanList());
        }
        iprv.setOnAddClickListener(new ImagePickRecyclerView.OnAddClickListener() {
            @Override
            public void onAddClick(View view, int maxPickImageCount, int hasImageCount) {
                //## ClassicSelectPhotoHelper.initBottomSheetDialog(MainActivity.this,false);

                Intent intent = new Intent(ClassicPhotoSelectorActivity.this, CustomPhotoSelectorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isToolbarTitleCenter", true);
                bundle.putInt("maxSelectCount", maxPickImageCount - hasImageCount);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);

            }


        });

    }

    private void saveImageOpear() {
        List<ImagePickBean> imagePickBeanList = iprv.getImagePickBeanListData();
        PhotoSelectorDataWrapper photoSelectorDataWrapper = new PhotoSelectorDataWrapper();
        photoSelectorDataWrapper.setImagePickBeanList(imagePickBeanList);
        Intent intent = new Intent();
        intent.putExtra("photoSelectorDataWrapper", photoSelectorDataWrapper);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                ImagePickerDataWrapper imagePickerDataWrapper = (ImagePickerDataWrapper) data.getSerializableExtra("imagePickerDataWrapper");
                List<String> selectedList = imagePickerDataWrapper.getSelectedImageList();
                List<ImagePickBean> imagePickBeanList = new ArrayList<>();
                for (String s : selectedList) {
                    ImagePickBean imagePickBean = new ImagePickBean();
                    imagePickBean.setImagePathOrUrl(s);
                    //imagePickBean.setImageWebIdStr(s);
                    imagePickBeanList.add(imagePickBean);
                }
                // Toast.makeText(this, "已选择："+sb.toString(), Toast.LENGTH_SHORT).show();
                iprv.addDataList(imagePickBeanList);

            }
        }
    }

    private TextView mToolbarTitleView;
    private Toolbar mToolbar;
    private boolean mIsToolbarTitleCenter = true;

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
                ClassicDialogFragment.Builder builder
                        = new ClassicDialogFragment.Builder(ClassicPhotoSelectorActivity.this);
                        builder.setTitle("温馨提示")
                        .setMessage("图片未保存，是否需要保存？")
                        .setOkText("是")
                        .setCancelText("否")
                        .setOnBtnClickListener(new ClassicDialogFragment.OnBtnClickListener() {
                            @Override
                            public void onBtnClickOk(DialogInterface dialogInterface) {
                                super.onBtnClickOk(dialogInterface);
                                //
                                saveImageOpear();
                            }

                            @Override
                            public void onBtnClickCancel(DialogInterface dialogInterface) {
                                super.onBtnClickCancel(dialogInterface);
                                //
                                finish();
                            }
                        }).build().show(getSupportFragmentManager(),"DSADSAS");

            }
        });
    }
}
