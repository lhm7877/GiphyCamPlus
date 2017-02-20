package com.hoomin.giphycamplus.base.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hoomin.giphycamplus.MyApplication;

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

    public ImageManager() {
        resultBitmapQ = new LinkedList<>();
    }

    //테스트용
    public static Bitmap mergeBitmapAndBitmap(Bitmap b1, Sticker sticker,int i) {
        Bitmap stickerFrame = sticker.gifDecoder.getFrame(i);
        Bitmap stickerBitmap = Bitmap.createScaledBitmap(stickerFrame, stickerFrame.getWidth() / 4, stickerFrame.getHeight() / 4, true);

        Bitmap mBitmap = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(mBitmap);

//        int adWDelta = (int)(b1.getWidth() - b2.getWidth())/2 ;
//        int adHDelta = (int)(b1.getHeight() - b2.getHeight())/2;

        canvas.drawBitmap(b1, 0, 0, null);
        canvas.drawBitmap(stickerBitmap,0, 0, null);

        return mBitmap;
    }

    public byte[] mergeBitmapAndSticker(File baseFile, Sticker sticker) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
//        Bitmap baseBitmap = Bitmap.createScaledBitmap(src, src.getWidth(), src.getHeight(), true);
        //비트맵이 사진 회전 속성에 따라 회전되어 저장되도록 수정
        Bitmap baseBitmap = BitmapFactory.decodeFile(baseFile.getAbsolutePath(), options);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(baseFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        baseBitmap = rotate(baseBitmap, exifDegree);

        for (int i = 0; i < sticker.getFrameCount(); i++) {
            Bitmap stickerFrame = sticker.gifDecoder.getFrame(i);
            Bitmap stickerBitmap = Bitmap.createScaledBitmap(stickerFrame, stickerFrame.getWidth() / 4, stickerFrame.getHeight() / 4, true);
//            resultBitmapQ.offer(mergeBitmapAndBitmap(baseBitmap, stickerBitmap));
            resultBitmapQ.offer(mergeBitmapAndBitmap(baseBitmap, sticker,i));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setDelay(100);
        for (int i = 0; i < sticker.getFrameCount(); i++) {
            Bitmap resultBitmap = resultBitmapQ.poll();
            encoder.addFrame(resultBitmap);
            resultBitmap.recycle();
        }
        encoder.finish();

        return bos.toByteArray();
    }

    //이미지 저장
    public boolean saveImage(byte[] bytes) {
        boolean isSuccess = false;
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
            new MediaScanning(MyApplication.getMyContext(), file);
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }


    public class mergeBitmapTask extends AsyncTask<File, Void, Boolean> {
        private Sticker mSticker;
        private ImageManager mImageManager;

        public mergeBitmapTask(Sticker sticker) {
            this.mSticker = sticker;
            mImageManager = new ImageManager();
        }

        @Override
        protected Boolean doInBackground(File... params) {

            Log.i("async", "doinback");
            byte[] resultImage = mImageManager.mergeBitmapAndSticker(params[0], mSticker);
            return mImageManager.saveImage(resultImage);
        }

        @Override
        protected void onPostExecute(Boolean isSucces) {
            super.onPostExecute(isSucces);
            if (isSucces) {
                Toast.makeText(MyApplication.getMyContext(), "저장성공", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshAndroidGallery(Uri fileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(fileUri);
            MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
        } else {
            MyApplication.getMyContext().sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    class MediaScanning implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mConnection;
        private File mTargetFile;

        public MediaScanning(Context mContext, File targetFile) {
            this.mTargetFile = targetFile;
            mConnection = new MediaScannerConnection(mContext, this);
            mConnection.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mConnection.scanFile(mTargetFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mConnection.disconnect();
        }
    }


    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


}
