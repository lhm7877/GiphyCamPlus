package com.hoomin.giphycamplus.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Hooo on 2017-02-11.
 */

public class Sticker {
    Context mContext;
    GifDecoder gifDecoder;

    private Queue<Bitmap> gifFrames = new LinkedList<>();
    private int frameCount;
    public Sticker(Context context, String path){
        this.mContext = context;
        init(path);
    }
    private void init(String path){
        gifDecoder = new GifDecoder();
        gifDecoder.read(ImageManager.getInputStreamfromGif(mContext,path));
        frameCount = gifDecoder.getFrameCount();
        for (int i = 0; i < frameCount; i++) {
            gifFrames.offer(gifDecoder.getFrame(i));
        }
    }

    public Queue<Bitmap> getGifFrames() {
        return gifFrames;
    }

    public int getFrameCount() {
        return frameCount;
    }
}
