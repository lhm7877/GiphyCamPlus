package com.hoomin.giphycamplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.util.ImageManager;
import com.hoomin.giphycamplus.util.Sticker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    @BindView(R.id.iv_split_0)
    protected ImageView iv_split_0;
    @BindView(R.id.iv_split_1)
    protected ImageView iv_split_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Sticker sticker = new Sticker(this,"giphy.gif");

        Bitmap baseBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.boostcamp);

        new mergeBitmapTask(this,sticker).execute(baseBitmap);
    }

    private class mergeBitmapTask extends AsyncTask<Bitmap, Void, byte[]>{
        private Sticker mSticker;
        private Context mContext;
        private ImageManager mImageManager;
        mergeBitmapTask(Context context,Sticker sticker){
            this.mContext = context;
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
            Glide.with(mContext)
                    .load(bytes)
                    .asGif()
                    .into(iv_split_0);
        }
    }
}
