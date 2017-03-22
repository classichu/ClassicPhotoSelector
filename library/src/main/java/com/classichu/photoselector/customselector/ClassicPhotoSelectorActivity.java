package com.classichu.photoselector.customselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
}
