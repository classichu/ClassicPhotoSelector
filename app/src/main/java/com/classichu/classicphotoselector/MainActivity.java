package com.classichu.classicphotoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.classichu.photoselector.customselector.CustomPhotoSelectorActivity;
import com.classichu.photoselector.customselector.bean.ImagePickerDataWrapper;
import com.classichu.photoselector.helper.ClassicSelectPhotoHelper;
import com.classichu.photoselector.imagespicker.ImagePickRecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView id_xsada;
    ImagePickRecyclerView iprv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        id_xsada = (ImageView) findViewById(R.id.id_xsada);
         iprv = (ImagePickRecyclerView) findViewById(R.id.id_xxad_xasa);
        iprv.setOnAddClickListener(new ImagePickRecyclerView.OnAddClickListener() {
            @Override
            public void onAddClick(View view, int maxPickImageCount, int hasImageCount) {
               //## ClassicSelectPhotoHelper.initBottomSheetDialog(MainActivity.this,false);

                Intent intent=  new Intent(MainActivity.this, CustomPhotoSelectorActivity.class);
                Bundle bundle=new Bundle();
                bundle.putBoolean("isToolbarTitleCenter",true);
                intent.putExtras(bundle);
                startActivityForResult(intent,44544);

            }


        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        ClassicSelectPhotoHelper.callAtOnActivityResult(this, requestCode, resultCode, data, new ClassicSelectPhotoHelper.OnBackImageListener() {
            @Override
            public void onBackImagePath(String imagePath) {
                super.onBackImagePath(imagePath);

                 Toast.makeText(MainActivity.this, "onBackImagePath imagePath:"+imagePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUCropBackImagePath(String imagePath) {
                super.onUCropBackImagePath(imagePath);

                 Toast.makeText(MainActivity.this, "onUCropBackImagePath imagePath:"+imagePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUCropBackError(Throwable cropError) {
                super.onUCropBackError(cropError);
                Toast.makeText(MainActivity.this, "onUCropBackError", Toast.LENGTH_SHORT).show();
            }
        });

        //
        if (requestCode==44544){
            if (resultCode==RESULT_OK){
                ImagePickerDataWrapper imagePickerDataWrapper= (ImagePickerDataWrapper) data.getSerializableExtra("imagePickerDataWrapper");
                List<String> selectedList= imagePickerDataWrapper.getSelectedImageList();
                StringBuilder sbWebId=new StringBuilder();
                StringBuilder sbPath=new StringBuilder();
                for (String s: selectedList) {
                    sbWebId.append(s);
                    sbWebId.append(",");

                    sbPath.append(s);
                    sbPath.append("|");
                }
               // Toast.makeText(this, "已选择："+sb.toString(), Toast.LENGTH_SHORT).show();
                String sbWebIdxx= sbWebId.toString().substring(0,sbWebId.toString().length()-1);

               String sbPathxx= sbPath.toString().substring(0,sbPath.toString().length()-1);

                iprv.setupDataListStr(sbWebIdxx,sbPathxx);

            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ClassicSelectPhotoHelper.callAtOnRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
