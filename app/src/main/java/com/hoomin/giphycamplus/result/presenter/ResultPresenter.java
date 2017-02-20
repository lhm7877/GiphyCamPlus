package com.hoomin.giphycamplus.result.presenter;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by Hooo on 2017-02-13.
 */

public interface ResultPresenter {
    interface View{
        void updateReaction(Response<?> response);
        void addSticker(GiphyImageDTO imageDTO);
        void dragandDropSticker();
    }
    interface Presenter{
        void attachView(ResultPresenter.View view);
        void detachView();
        void saveImage(File albumImageFile, ArrayList<ImageView> iv_Stickers);
        void loadSticker();
        void loadSeletedSticker(int data);
        void dragandDropSticker();
    }
}
