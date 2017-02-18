package com.hoomin.giphycamplus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.result.view.ResultActivity;
import com.hoomin.giphycamplus.base.util.PermissionCheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_main)
    protected ImageView iv_main;

    @BindView(R.id.iv_gif)
    protected ImageView iv_gif;

    @BindView(R.id.activity_main)
    protected FrameLayout activity_main;

    @BindView(R.id.btn_save)
    protected Button btn_save;

    @BindView(R.id.btn_result)
    protected Button btn_result;

    private String albumImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        init();
        Glide.with(this)
                .load(R.drawable.boostcamp)
                .into(iv_main);

    }

    private void init() {
        requestPermission();
    }

    @OnClick(R.id.btn_result)
    void clickResult() {
        doTakeAlbumAction();
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

    private void requestPermission(){
        int permissionStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStorage == PackageManager.PERMISSION_DENIED) {
            PermissionCheck.checkPermission(this);
        } else {
            Toast.makeText(this, "권한있음", Toast.LENGTH_SHORT).show();
        }
    }

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
                Uri uri = data.getData();
                File imageFile = new File(getRealPathFromURI(uri));
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("baseImage",imageFile);
                startActivity(intent);
            }
        }
    }


    //권한 요청 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionCheck.MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    saveImage();
                    // permission was granted, yay! do the
                    // calendar task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

        }
    }

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
}
