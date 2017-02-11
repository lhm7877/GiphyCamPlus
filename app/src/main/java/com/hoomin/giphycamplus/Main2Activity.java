package com.hoomin.giphycamplus;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.hoomin.giphycamplus.util.GifDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    @BindView(R.id.iv_split_0)
    protected ImageView iv_split_0;
    @BindView(R.id.iv_split_1)
    protected ImageView iv_split_1;
    @BindView(R.id.iv_split_2)
    protected ImageView iv_split_2;
    @BindView(R.id.iv_split_3)
    protected ImageView iv_split_3;
    @BindView(R.id.iv_split_4)
    protected ImageView iv_split_4;
    @BindView(R.id.iv_split_5)
    protected ImageView iv_split_5;
    @BindView(R.id.iv_split_6)
    protected ImageView iv_split_6;
    @BindView(R.id.iv_split_7)
    protected ImageView iv_split_7;
    @BindView(R.id.iv_split_8)
    protected ImageView iv_split_8;
    @BindView(R.id.iv_split_9)
    protected ImageView iv_split_9;
    @BindView(R.id.iv_split_10)
    protected ImageView iv_split_10;
    @BindView(R.id.iv_split_11)
    protected ImageView iv_split_11;
    @BindView(R.id.iv_split_12)
    protected ImageView iv_split_12;
    @BindView(R.id.iv_split_13)
    protected ImageView iv_split_13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(getInputStreamfromGif("giphy.gif"));
        int numOfFrame = gifDecoder.getFrameCount();

        Queue<Bitmap> gifFrames = new LinkedList<>();

        for (int i = 0; i < numOfFrame; i++) {
            gifFrames.offer(gifDecoder.getFrame(i));
        }


        ImageView[] imageViews = {
                iv_split_0,
                iv_split_1,
                iv_split_2,
                iv_split_3,
                iv_split_4,
                iv_split_5,
                iv_split_6,
                iv_split_7,
                iv_split_8,
                iv_split_9,
                iv_split_10,
                iv_split_11,
                iv_split_12,
                iv_split_13
        };

        for (int i = 0; i < numOfFrame; i++) {
            imageViews[i].setImageBitmap(gifFrames.poll());
        }
    }

    private InputStream getInputStreamfromGif(String path) {
        InputStream stream = null;
        try {
            stream = getAssets().open(path);
        } catch (IOException e) {
        }
        return stream;
    }
}
