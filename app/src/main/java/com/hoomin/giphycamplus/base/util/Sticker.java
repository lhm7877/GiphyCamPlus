package com.hoomin.giphycamplus.base.util;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Hooo on 2017-02-11.
 */

public class Sticker {
    private GifDecoder gifDecoder;
    private GiphyImageDTO giphyImageDTO;
    private InputStream inputStream;
    private ImageView imageView;


    private Queue<Bitmap> gifFrames = new LinkedList<>();
    private int frameCount;
    public Sticker(GiphyImageDTO imageDTO){
        this.giphyImageDTO = imageDTO;
        init();
    }
    private void init(){
        gifDecoder = new GifDecoder();
//        gifDecoder.read(ImageManager.getInputStreamfromGif(mContext,giphyImageDTO));

        imageView = new ImageView(MyApplication.getMyContext());

        //ImageView의 크기 지정(drag시 크기 변화 방지)
//        final float scale = MyApplication.getMyContext().getResources().getDisplayMetrics().density;
//        int dpWidthInPx = (int) (200 * scale);
//        int dpHeightInPx = (int) (200 * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        layoutParams.gravity= Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
    }

    public Queue<Bitmap> getGifFrames() {
        return gifFrames;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public GiphyImageDTO getGiphyImageDTO() {
        return giphyImageDTO;
    }

    public void setGiphyImageDTO(GiphyImageDTO giphyImageDTO) {
        this.giphyImageDTO = giphyImageDTO;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        gifDecoder.read(inputStream);
        frameCount = gifDecoder.getFrameCount();
        for (int i = 0; i < frameCount; i++) {
            gifFrames.offer(gifDecoder.getFrame(i));
        }
        this.inputStream = inputStream;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public GifDecoder getGifDecoder() {
        return gifDecoder;
    }

    public void setGifDecoder(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
    }
}
