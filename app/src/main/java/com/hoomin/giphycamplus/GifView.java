package com.hoomin.giphycamplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.hoomin.giphycamplus.util.GifDecoder;

import java.io.InputStream;

/**
 * Created by Hooo on 2017-02-10.
 */

class GifView extends ImageView implements View.OnClickListener {

    private boolean isPlayingGif = false;
    private GifDecoder gifDecoder = new GifDecoder();
    private Bitmap frame = null;
    final Handler handler = new Handler();

    final Runnable updateDisplayedImage = new Runnable() {
        public void run() {
            if(frame != null && !frame.isRecycled())
                setImageBitmap(frame);
        }
    };

    public GifView(Context context, InputStream gifStream) {
        super(context);
        playGif(gifStream);
        setOnClickListener(this);
    }

    private void playGif(InputStream gifStream) {;
        gifDecoder.read(gifStream);
        isPlayingGif = true;

        new Thread(new Runnable() {
            public void run() {
                int numOfFrame = gifDecoder.getFrameCount();

                while(true) {
                    for(int i = 0; i < numOfFrame; i++) {
                        // get current frame and update displayed image
                        frame = gifDecoder.getFrame(i);

                        // the runnable will be run on the thread
                        // to which this handler is attached (main thread)
                        handler.post(updateDisplayedImage);

                        // break time up to the next frame
                        int breakTime = gifDecoder.getDelay(i);
                        try {
                            Thread.sleep(breakTime);
                            while(!isPlayingGif)
                                Thread.sleep(50);
                        } catch (InterruptedException e) { }
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        isPlayingGif = !isPlayingGif;
    }

}