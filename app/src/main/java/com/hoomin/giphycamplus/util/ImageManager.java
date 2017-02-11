package com.hoomin.giphycamplus.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hooo on 2017-02-11.
 */

public class ImageManager {
    private InputStream getInputStreamfromGif(Context context, String path) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(path);
        } catch (IOException e) {
        }
        return stream;
    }
}
