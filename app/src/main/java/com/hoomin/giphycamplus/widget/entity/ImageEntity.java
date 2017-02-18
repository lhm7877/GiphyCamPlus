package com.hoomin.giphycamplus.widget.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.viewmodel.Layer;

import static android.R.attr.bitmap;


public class ImageEntity extends MotionEntity {

    @NonNull
    private final GiphyImageDTO giphyImageDTO;
//    private final Bitmap bitmap;
    public ImageEntity(@NonNull Layer layer,
                       @NonNull GiphyImageDTO giphyImageDTO,
                       @IntRange(from = 1) int canvasWidth,
                       @IntRange(from = 1) int canvasHeight) {
        super(layer, canvasWidth, canvasHeight);

        this.giphyImageDTO = giphyImageDTO;
        float width = Float.parseFloat(giphyImageDTO.getWidth());
        float height = Float.parseFloat(giphyImageDTO.getHeight());

        float widthAspect = 1.0F * canvasWidth / width;
        float heightAspect = 1.0F * canvasHeight / height;
        // fit the smallest size
        holyScale = Math.min(widthAspect, heightAspect);

        // initial position of the entity
        srcPoints[0] = 0;
        srcPoints[1] = 0;
        srcPoints[2] = width;
        srcPoints[3] = 0;
        srcPoints[4] = width;
        srcPoints[5] = height;
        srcPoints[6] = 0;
        srcPoints[7] = height;
        srcPoints[8] = 0;
        srcPoints[8] = 0;
    }

    @Override
    public void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {
        canvas.drawBitmap(bitmap, matrix, drawingPaint);
    }

    @Override
    public int getWidth() {
        return Integer.parseInt(giphyImageDTO.getWidth());
    }

    @Override
    public int getHeight() {
        return Integer.parseInt(giphyImageDTO.getHeight());
    }

    @Override
    public void release() {
//        if (!bitmap.isRecycled()) {
//            bitmap.recycle();
//        }
    }
}