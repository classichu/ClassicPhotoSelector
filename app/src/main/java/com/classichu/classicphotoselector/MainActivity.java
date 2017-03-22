package com.classichu.classicphotoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.classichu.photoselector.customselector.ClassicPhotoSelectorActivity;
import com.classichu.photoselector.helper.ClassicSelectPhotoHelper;
import com.classichu.photoselector.listener.OnNotFastClickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.dsadas).setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                startActivity(new Intent(MainActivity.this, ClassicPhotoSelectorActivity.class));
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

                Toast.makeText(MainActivity.this, "onBackImagePath imagePath:" + imagePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUCropBackImagePath(String imagePath) {
                super.onUCropBackImagePath(imagePath);

                Toast.makeText(MainActivity.this, "onUCropBackImagePath imagePath:" + imagePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUCropBackError(Throwable cropError) {
                super.onUCropBackError(cropError);
                Toast.makeText(MainActivity.this, "onUCropBackError", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ClassicSelectPhotoHelper.callAtOnRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
