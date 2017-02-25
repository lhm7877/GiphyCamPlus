package com.hoomin.giphycamplus.result.presenter;

import android.util.Log;

import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.base.util.ImageManager;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Response;


/**
 * Created by Hooo on 2017-02-13.
 */

public class ResultPresenterImpl implements ResultPresenter.Presenter, GiphyModel.GiphyModelDataChange {
    private ResultPresenter.View view;
    private GiphyModel giphyModel;
    private ImageManager imageManager;
    private Response<?> response;
    private Realm mRealm;

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
    public void saveImage(File albumImageFile, ArrayList<Sticker> stickers) {
        imageManager.new mergeBitmapTask(stickers).execute(albumImageFile);
    }

    @Override
    public void loadSticker(Boolean isFilled) {
        if (giphyModel == null) {
            return;
        }
        giphyModel.callSticker(isFilled);
    }

    @Override
    public void loadSeletedSticker(int position) {
        view.addSticker(giphyModel.callSelectedSticker(position));
    }

    @Override
    public void dragandDropSticker() {

    }

    @Override
    public void update(Response<?> response, Boolean isFilled) {
        if (response.body() == null) {
            return;
        } else if (response.body() != null) {
            view.updateReaction(response,isFilled);
            this.response = response;
        }
    }

}
