package com.classichu.classicphotoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.classichu.photoselector.helper.ClassicSelectPhotoHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.id_xxx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassicSelectPhotoHelper.initBottomSheetDialog(MainActivity.this);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        ClassicSelectPhotoHelper.callAtOnActivityResult(this, requestCode, resultCode, data, new ClassicSelectPhotoHelper.OnUCropBackImagePathListener() {
            @Override
            public void onUCropBackImagePath(String imagePath) {
                 /*  Bitmap bitmap = ImageTool.decodeSampledBitmapFromPath(imagePath,
                            getResources().getDimensionPixelSize(R.dimen.base_userface_width),
                            getResources().getDimensionPixelSize(R.dimen.base_userface_height));*/
            /*    GlideHelper.loadImage(imagePath, new GlideHelper.OnLoadImageBackListener() {
                    @Override
                    public void onLoadImageBack(Bitmap bitmap) {
                        mRoundedImageView.setImageBitmap(bitmap);
                    }
                });*/
               /* ImageLoaderFactory.getManager().loadImage(imagePath, new ILoadImageCallback() {
                    @Override
                    public void onLoadImageBack(Bitmap bitmap) {
                        mRoundedImageView.setImageBitmap(bitmap);
                    }
                });*/

                Toast.makeText(MainActivity.this, "imagePath"+imagePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUCropBackError(Throwable cropError) {

            }
        });
    }
}
