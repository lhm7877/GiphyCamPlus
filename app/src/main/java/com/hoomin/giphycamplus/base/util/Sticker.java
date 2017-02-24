package com.hoomin.giphycamplus.base.util;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Hooo on 2017-02-11.
 */

public class Sticker {
    private GifDecoder gifDecoder;
    private GiphyImageDTO giphyImageDTO;
    private InputStream inputStream;
    //    private ImageView imageView;
    private PhotoView photoView;
    private LinearLayout.LayoutParams layoutParams;


    private Queue<Bitmap> gifFrames = new LinkedList<>();
    private int frameCount;

    public Sticker(GiphyImageDTO imageDTO) {
        this.giphyImageDTO = imageDTO;
        init();
    }

    private void init() {
        gifDecoder = new GifDecoder();
//        gifDecoder.read(ImageManager.getInputStreamfromGif(mContext,giphyImageDTO));

//        imageView = new ImageView(MyApplication.getMyContext());
//        photoView = new PhotoView(MyApplication.getMyContext());
//        photoView.setMinimumScale(0.1f);
//        photoView.setMaximumScale(2.0f);


        //ImageView의 크기 지정(drag시 크기 변화 방지)
//        final float scale = MyApplication.getMyContext().getResources().getDisplayMetrics().density;
//        int dpWidthInPx = (int) (200 * scale);
//        int dpHeightInPx = (int) (200 * scale);
        layoutParams = new LinearLayout.LayoutParams(200,200);
        photoView = new PhotoView(MyApplication.getMyContext());
        layoutParams.gravity = Gravity.CENTER;
        photoView.setLayoutParams(layoutParams);
//        imageView.setLayoutParams(layoutParams);
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

    //    public ImageView getImageView() {
//        return imageView;
//    }
//
//    public void setImageView(ImageView imageView) {
//        this.imageView = imageView;
//    }
    public PhotoView getPhotoView() {
        return photoView;
    }

    public void setPhotoView(PhotoView photoView) {
        this.photoView = photoView;
    }

    public GifDecoder getGifDecoder() {
        return gifDecoder;
    }

    public void setGifDecoder(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
    }

    public LinearLayout.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(LinearLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
}
