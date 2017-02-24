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
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hoomin.giphycamplus.result.view.ResultActivity;

import java.io.IOException;

import butterknife.OnClick;

import static com.hoomin.giphycamplus.base.util.ImageManager.saveBitmapToJpeg;

public class MyCamera {

    protected static final String TAG = null;
    public SurfaceHolder surfaceHolder;
    public Camera camera;
    private boolean inProgress;
    private Context mContext;

    // 2. 그다음 시작하는게 onCreate. Activity 생성시 제일 먼저 실행된다.
    public MyCamera(Context context, SurfaceView surfaceView) {
        this.mContext = context;

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
    }

    public void takePicture() {
        if (camera != null && inProgress == false) {

            camera.takePicture(null, null, takePicture);
            inProgress = true;
        }
    }

    private Camera.PictureCallback takePicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopPreview();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
            String url = saveBitmapToJpeg(MyApplication.getMyContext(), bitmap);
            Intent intent = new Intent(mContext, ResultActivity.class);
            Log.i("baseImage",url);
            intent.putExtra("baseImage", url);
//            camera.startPreview();
            inProgress = false;
            mContext.startActivity(intent);
        }
    };

    public SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.i("lifecycle","surfaceDestroyed");
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//            Log.i("onCreate", "카메라 기능 해제");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            if (camera != null) {
//                camera.release();
//                camera = null;
//            }
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
//            if (mContext.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
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
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i("lifecycle","surfaceChanged");
            /*// TODO Auto-generated method stub
            // 프리뷰를 회전시키거나 변경시 처리를 여기서 해준다.
            // 프리뷰 변경시에는 먼저 프리뷰를 멈춘다음 변경해야한다.
            if (holder.getSurface() == null){
                // 프리뷰가 존재하지 않을때
                return;
            }

            // 우선 멈춘다
            try {
                camera.stopPreview();
            } catch (Exception e){
                // 프리뷰가 존재조차 하지 않는 경우다
            }

            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.startPreview();
            Log.i(TAG,"카메라 미리보기 활성");
*/
        }
    };
}