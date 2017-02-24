package com.hoomin.giphycamplus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.result.view.ResultActivity;
import com.hoomin.giphycamplus.base.util.PermissionCheck;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.realm.Realm;

import static com.hoomin.giphycamplus.base.util.ImageManager.saveBitmapToJpeg;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.activity_main)
    protected FrameLayout activity_main;

    @BindView(R.id.sfv_main)
    protected SurfaceView sfv_main;

    private MyCamera myCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.d("debug", "Screen inches : " + dm.widthPixels + " " + dm.heightPixels);

        Log.i("LifeCycle", "onCreate");
        init();
    }

    private void init() {
        PermissionCheck.checkPermission(this);
//        SurfaceHolder holder = surfaceView.getHolder();
//        holder.addCallback(surfaceView);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        myCamera = new MyCamera(this, sfv_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LifeCycle", "onResume");
        if(myCamera.camera==null){
            myCamera = new MyCamera(this, sfv_main);
            sfv_main.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        Log.i("LifeCycle", "onPause");
        super.onPause();
        if(myCamera.camera!=null){
            myCamera.camera.release();
            myCamera.surfaceHolder.removeCallback(myCamera.surfaceListener);
            myCamera.camera=null;
            sfv_main.setVisibility(View.INVISIBLE);
        }
//        myCamera.camera.stopPreview();
//        myCamera.camera.release();

    }

    @OnClick(R.id.btn_camera)
    void clickCamera() {
        myCamera.takePicture();
    }


    @OnClick(R.id.btn_gallery)
    void clickResult() {
        doTakeAlbumAction();

    }

    private void startCropImageActivity(Uri imageUri) {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowRotation(false)
                .setAspectRatio(dm.widthPixels, dm.heightPixels)
                .setFixAspectRatio(true)
                .start(this);
    }

    //테스트 저장 버튼
    @OnClick(R.id.btn_save)
    void clickSave() {
        Toast.makeText(MainActivity.this, "저장 클릭", Toast.LENGTH_SHORT).show();
        int permissionStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
            PermissionCheck.checkPermission(this);
        } else {
            Toast.makeText(this, "저장", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermissions(String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(MyApplication.getMyContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    private void requestPermission(){
//        int permissionStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
//            PermissionCheck.checkPermission(this);
//        } else {
//            Toast.makeText(this, "권한있음", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    startCropImageActivity(uri);
//                    File imageFile = new File(getRealPathFromURI(uri));
//                    Intent intent = new Intent(this, ResultActivity.class);
//                    intent.putExtra("activity",1);
//                    intent.putExtra("baseImage", imageFile);
//                    startActivity(intent);
                }
                break;
            }
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE: {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    String url = getRealPathFromURI(result.getUri());
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("baseImage", url);
                    startActivity(intent);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
            }
        }
    }


//    //권한 요청 결과
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PermissionCheck.MY_PERMISSION_REQUEST_STORAGE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
////                    saveImage();
//                    // permission was granted, yay! do the
//                    // calendar task you need to do.
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                break;
//
//        }
//    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v("jyp", "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }


}
