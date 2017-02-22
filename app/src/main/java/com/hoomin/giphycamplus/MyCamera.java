package com.hoomin.giphycamplus;

/**
 * Created by Hooo on 2017-02-22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hoomin.giphycamplus.result.view.ResultActivity;

import java.io.IOException;

import butterknife.OnClick;

import static com.hoomin.giphycamplus.base.util.ImageManager.saveBitmapToJpeg;

public class MyCamera{

    protected static final String TAG = null;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean inProgress;
    private Context mContext;

    // 2. 그다음 시작하는게 onCreate. Activity 생성시 제일 먼저 실행된다.
    public MyCamera(Context context,SurfaceView surfaceView) {
        this.mContext = context;
        camera = Camera.open();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
    }

    public void takePicture() {
        if(camera !=null && inProgress == false)
        {
            camera.takePicture(null, null, takePicture);
            inProgress = true;
        }
    }

    private Camera.PictureCallback takePicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            String url = saveBitmapToJpeg(MyApplication.getMyContext(),bitmap);
            Intent intent = new Intent(mContext,ResultActivity.class);
            intent.putExtra("activity",0);
            intent.putExtra("capturedBaseImage",url);
            camera.startPreview();
            inProgress = false;
            mContext.startActivity(intent);
        }
    };

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera.release();
            camera = null;
            Log.i(TAG, "카메라 기능 해제");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Camera.Parameters parameters = camera.getParameters();
//            if (mContext.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
//            } else {
//                parameters.set("orientation", "landscape");
//                camera.setDisplayOrientation(0);
//                parameters.setRotation(0);
//            }
            camera.setParameters(parameters);


            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
//            camera = Camera.open();
//            Log.i(TAG, "카메라 미리보기 활성");
//
//            try {
//                camera.setPreviewDisplay(holder);
//            }catch(Exception e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
            // TODO Auto-generated method stub
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.startPreview();
            Log.i(TAG,"카메라 미리보기 활성");

        }
    };
}