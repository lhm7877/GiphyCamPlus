package com.hoomin.giphycamplus.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.Main2Activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Hooo on 2017-02-11.
 */

public class ImageManager {
    Queue<Bitmap> resultBitmapQ;
    public static InputStream getInputStreamfromGif(Context context, String path) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(path);
        } catch (IOException e) {
        }
        return stream;
    }

    public ImageManager(){
        resultBitmapQ = new LinkedList<>();
    }

    //테스트용
    public static Bitmap mergeBitmapAndBitmap(Bitmap b1, Bitmap b2) {
        Bitmap mBitmap = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(mBitmap);

//        int adWDelta = (int)(b1.getWidth() - b2.getWidth())/2 ;
//        int adHDelta = (int)(b1.getHeight() - b2.getHeight())/2;

        canvas.drawBitmap(b1, 0, 0, null);
        canvas.drawBitmap(b2, 0, 0, null);

        return mBitmap;
    }

    public byte[] mergeBitmapAndSticker(Bitmap b1, Sticker sticker) {
//        Bitmap mBitmap = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(mBitmap);

//        int adWDelta = (int)(b1.getWidth() - b2.getWidth())/2 ;
//        int adHDelta = (int)(b1.getHeight() - b2.getHeight())/2;

//        b1.

        for(int i =0; i<sticker.getFrameCount(); i++){
            resultBitmapQ.offer(mergeBitmapAndBitmap(b1,sticker.gifDecoder.getFrame(i)));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setDelay(100);
        for(int i=0; i<sticker.getFrameCount(); i++){
            Bitmap resultBitmap = resultBitmapQ.poll();
            encoder.addFrame(resultBitmap);
            resultBitmap.recycle();
        }
        encoder.finish();

        return bos.toByteArray();
    }

    //이미지 저장
    public void saveImage(byte[] bytes) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.giphy);
        File diFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GiphyCamPlus");
        String giphyPath = diFile.getPath();
        if (!diFile.exists()) {
            diFile.mkdirs();
        }
        File file = new File(giphyPath, "Test4.gif");
        Log.i("path", file.getPath());

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage2(Context context,Sticker sticker,Bitmap baseBitmap){

        new mergeBitmapTask(sticker).execute(baseBitmap);
    }

    public class mergeBitmapTask extends AsyncTask<Bitmap, Void, byte[]> {
        private Sticker mSticker;
        private Context mContext;
        private ImageManager mImageManager;
        public mergeBitmapTask(Sticker sticker){
            this.mSticker = sticker;
            mImageManager = new ImageManager();
        }
        @Override
        protected byte[] doInBackground(Bitmap... params) {
            Log.i("async","doinback");
            return mImageManager.mergeBitmapAndSticker(params[0],mSticker);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mImageManager.saveImage(bytes);
        }
    }

}
