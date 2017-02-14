package com.hoomin.giphycamplus.result.presenter;

import android.net.Uri;

import java.io.File;

/**
 * Created by Hooo on 2017-02-13.
 */

public interface ResultPresenter {
    interface View{
        void updateReaction(String url);
    }
    interface Presenter{
        void attachView(ResultPresenter.View view);
        void detachView();
        void saveImage(File albumImageFile);
        void loadSticker();
    }
}
