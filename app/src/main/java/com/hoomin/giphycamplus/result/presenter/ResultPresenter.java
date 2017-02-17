package com.hoomin.giphycamplus.result.presenter;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

import retrofit2.Response;

/**
 * Created by Hooo on 2017-02-13.
 */

public interface ResultPresenter {
    interface View{
        void updateReaction(Response<?> response);
        void addSticker(Intent data);
    }
    interface Presenter{
        void attachView(ResultPresenter.View view);
        void detachView();
        void saveImage(File albumImageFile);
        void loadSticker();
        void loadSeletedSticker(Intent position);
    }
}
