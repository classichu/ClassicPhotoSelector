package com.classichu.photoselector.customselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.classichu.photoselector.R;
import com.classichu.photoselector.customselector.bean.ImagePickerDataWrapper;
import com.classichu.photoselector.imagespicker.ImagePickBean;
import com.classichu.photoselector.imagespicker.ImagePickRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassicPhotoSelectorActivity extends AppCompatActivity {
    private ImagePickRecyclerView iprv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_photo_selector);

        Bundle bundle = null;
        if (this.getIntent() != null) {
            bundle = this.getIntent().getExtras();
        }
        if (bundle != null) {
            mIsToolbarTitleCenter = bundle.getBoolean("isToolbarTitleCenter");
        }

        initToolbar();

        setToolbarTitle("图片选择");

        iprv = (ImagePickRecyclerView) findViewById(R.id.id_iprv);
        iprv.setOnAddClickListener(new ImagePickRecyclerView.OnAddClickListener() {
            @Override
            public void onAddClick(View view, int maxPickImageCount, int hasImageCount) {
                //## ClassicSelectPhotoHelper.initBottomSheetDialog(MainActivity.this,false);

                Intent intent = new Intent(ClassicPhotoSelectorActivity.this, CustomPhotoSelectorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isToolbarTitleCenter", true);
                bundle.putInt("maxSelectCount", maxPickImageCount-hasImageCount);
                intent.putExtras(bundle);
                startActivityForResult(intent, 44544);

            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if (requestCode == 44544) {
            if (resultCode == RESULT_OK) {
                ImagePickerDataWrapper imagePickerDataWrapper = (ImagePickerDataWrapper) data.getSerializableExtra("imagePickerDataWrapper");
                List<String> selectedList = imagePickerDataWrapper.getSelectedImageList();
                List<ImagePickBean> imagePickBeanList=new ArrayList<>();
                for (String s : selectedList) {
                    ImagePickBean imagePickBean=new ImagePickBean();
                    imagePickBean.setImagePathOrUrl(s);
                    imagePickBean.setImageWebIdStr(s);
                    imagePickBeanList.add(imagePickBean);
                }
                // Toast.makeText(this, "已选择："+sb.toString(), Toast.LENGTH_SHORT).show();
                iprv.addDataList(imagePickBeanList);

            }
        }
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
}
