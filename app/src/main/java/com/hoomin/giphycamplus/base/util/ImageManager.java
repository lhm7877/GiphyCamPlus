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
import android.widget.ImageView;
import android.widget.Toast;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

import static com.hoomin.giphycamplus.R.drawable.sticker;

/**
 * Created by Hooo on 2017-02-11.
 */

public class ImageManager {
    Queue<Bitmap> resultBitmapQ;

    /*public static InputStream getInputStreamfromGif(Context context, GiphyImageDTO imageDTO) {
//        return imageDTO.getInputStream();
        return null;
    }*/

    public ImageManager() {
        resultBitmapQ = new LinkedList<>();
    }

    //테스트용
    public static Bitmap mergeBitmapAndBitmap(Bitmap b1, ArrayList<Sticker> stickers, int i) {
        Log.i("ratio", String.valueOf(b1.getWidth()));


        Bitmap mBitmap = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(mBitmap);

        final int scale = (int) MyApplication.getMyContext().getResources().getDisplayMetrics().density;

        Bitmap[] scaledBitmap = new Bitmap[stickers.size()];
        for(Sticker sticker : stickers){
            float ratio = (float) (b1.getHeight() / 200.0);
            float ratio2 = (float) (b1.getWidth() / sticker.getGifDecoder().getFrame(i).getWidth());
            Bitmap stickerFrame = sticker.getGifDecoder().getFrame(i);
            scaledBitmap[stickers.indexOf(sticker)] =
                    Bitmap.createBitmap(stickerFrame, 0, 0, (int) (stickerFrame.getWidth()/ratio2), (int) (b1.getHeight()/ratio));
            Log.i("frameSize","가로 : " + stickerFrame.getWidth() + "   " + "세로 : " + stickerFrame.getHeight());
        }



//        Log.i("ratio", ratio+" "+scaledBitmap[0].getHeight());

//        int adWDelta = (int)(b1.getWidth() - b2.getWidth())/2 ;
//        int adHDelta = (int)(b1.getHeight() - b2.getHeight())/2;

        canvas.drawBitmap(b1, 0, 0, null);
        for(int j =0; j<stickers.size(); j++){
            canvas.drawBitmap(scaledBitmap[j],stickers.get(j).getImageView().getX(),stickers.get(j).getImageView().getY(),null);
        }
//        canvas.drawBitmap(stickerBitmap,sticker.getImageView().getX(), sticker.getImageView().getY(), null);

        return mBitmap;
    }

    public byte[] mergeBitmapAndSticker(File baseFile, ArrayList<Sticker> stickers) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
//        Bitmap baseBitmap = Bitmap.createScaledBitmap(src, src.getWidth(), src.getHeight(), true);
        //비트맵이 사진 회전 속성에 따라 회전되어 저장되도록 수정
        Bitmap baseBitmap = BitmapFactory.decodeFile(baseFile.getAbsolutePath(), options);

        baseBitmap = rotateBitmap(baseBitmap,baseFile);

        //가장 킨 프레임 카운트에 맞춤
        int maxFrameCount = 0;
        for(Sticker sticker : stickers){
            if(sticker.getFrameCount()>maxFrameCount){
                maxFrameCount = sticker.getFrameCount();
            }

        }

        for (int i = 0; i < maxFrameCount; i++) {
            resultBitmapQ.offer(mergeBitmapAndBitmap(baseBitmap, stickers,i));
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setDelay(100);
        while(!resultBitmapQ.isEmpty()){
            Bitmap resultBitmap = resultBitmapQ.poll();
            encoder.addFrame(resultBitmap);
            resultBitmap.recycle();
        }
        encoder.finish();

        Log.i("resultImage2", String.valueOf(bos.toByteArray()));
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
        Calendar c = Calendar.getInstance();
        String date = String.valueOf((c.get(Calendar.MONTH))
                        + (c.get(Calendar.DAY_OF_MONTH))
                        + (c.get(Calendar.YEAR))
                        + (c.get(Calendar.HOUR_OF_DAY))
                        + (c.get(Calendar.MINUTE))
                        + (c.get(Calendar.SECOND)));

        File file = new File(giphyPath, date+".gif");
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
        private ArrayList<Sticker> mStickers;
        private ImageManager mImageManager;

        public mergeBitmapTask(ArrayList<Sticker> sticker) {
            this.mStickers = sticker;
            mImageManager = new ImageManager();
        }

        @Override
        protected Boolean doInBackground(File... params) {
            byte[] resultImage = mImageManager.mergeBitmapAndSticker(params[0], mStickers);
            Log.i("resultImage", String.valueOf(resultImage));
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

//    public void refreshAndroidGallery(Uri fileUri) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Intent mediaScanIntent = new Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            mediaScanIntent.setData(fileUri);
//            MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
//        } else {
//            MyApplication.getMyContext().sendBroadcast(new Intent(
//                    Intent.ACTION_MEDIA_MOUNTED,
//                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//        }
//    }

    static class MediaScanning implements MediaScannerConnection.MediaScannerConnectionClient {
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


    public static Bitmap rotate(Bitmap bitmap, int degrees) {
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

    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotateBitmap(Bitmap baseBitmap, File baseFile){
        Log.i("rotate","rotateBitmap()");
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(baseFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Log.i("rotate", String.valueOf(exifDegree));
        return rotate(baseBitmap, exifDegree);
    }
    public static String saveBitmapToJpeg(Context context, Bitmap bitmap){
//        File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로
        File diFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/GiphyCamPlus");
        String giphyPath = diFile.getPath();
        File storage = new File(giphyPath);
        String fileName = "tempImage.jpg";  // 파일이름은 마음대로!
        File tempFile = new File(storage,fileName);
        Log.i("rotate",tempFile.getAbsolutePath());
        try{
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            out.close(); // 마무리로 닫아줍니다.
            new MediaScanning(MyApplication.getMyContext(), tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴해주면 끝!
    }


}
