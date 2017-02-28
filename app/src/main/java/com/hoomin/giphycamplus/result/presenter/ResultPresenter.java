package com.hoomin.giphycamplus.result.presenter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by Hooo on 2017-02-13.
 */

public interface ResultPresenter {
    interface View{
        void updateReaction(Response<?> response);
        void addSticker(Sticker sticker);
    }
    interface Presenter{
        void attachView(ResultPresenter.View view);
        void detachView();
        void saveImage(File albumImageFile, ArrayList<Sticker> stickers);
        void loadSticker(Boolean isFilled);
        void loadSeletedSticker(int data);
        void dragandDropSticker();
    }
}
