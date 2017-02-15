package com.hoomin.giphycamplus.result.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.base.domain.GiphyRepoDTO;
import com.hoomin.giphycamplus.base.util.ImageManager;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.File;


/**
 * Created by Hooo on 2017-02-13.
 */

public class ResultPresenterImpl implements ResultPresenter.Presenter, GiphyModel.GiphyModelDataChange {
    private ResultPresenter.View view;
    private GiphyModel giphyModel;
    private ImageManager imageManager;

    @Override
    public void attachView(ResultPresenter.View view) {
        this.view = view;
        giphyModel = new GiphyModel();
        giphyModel.setOnChangeListener(this);
        if (this instanceof ResultPresenterImpl) {
            Log.i("this", "같음");
        }
        imageManager = new ImageManager();
    }

    @Override
    public void detachView() {
        view = null;
        giphyModel = null;
        giphyModel.setOnChangeListener(null);
        imageManager = null;
    }

    @Override
    public void saveImage(File albumImageFile) {
        Sticker sticker = new Sticker(MyApplication.getMyContext(), "giphy.gif");


        imageManager.new mergeBitmapTask(sticker).execute(albumImageFile);
    }

    @Override
    public void loadSticker() {
        if (giphyModel == null) {
            return;
        }
        giphyModel.callSticker();
    }

    @Override
    public void update(GiphyRepoDTO models) {
        Log.i("mylog", "업데이트됨1");
        if (models == null) {
            return;
        }
        if (models.getData() != null) {
            Log.i("loadSticker", "models.getData()널아님");
            view.updateReaction(models.getData().get(0).getImages().getFixed_height().getUrl());
        }
    }
}
