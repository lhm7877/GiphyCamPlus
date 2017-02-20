package com.hoomin.giphycamplus.result.presenter;

import android.util.Log;
import android.widget.ImageView;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.base.util.ImageManager;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Response;


/**
 * Created by Hooo on 2017-02-13.
 */

public class ResultPresenterImpl implements ResultPresenter.Presenter, GiphyModel.GiphyModelDataChange {
    private ResultPresenter.View view;
    private GiphyModel giphyModel;
    private ImageManager imageManager;
    private Response<?> response;

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
    public void saveImage(File albumImageFile, ArrayList<ImageView> iv_Stickers) {
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
    public void loadSeletedSticker(int data) {
//        view.
        giphyModel.callSelectedSticker(data);
//        view.addSticker(data);
    }

    @Override
    public void dragandDropSticker() {

    }

    @Override
    public void update(Response<?> response) {
        if (response.body() == null) {
            return;
        } else if (response.body() != null) {
            view.updateReaction(response);
            this.response = response;
        }
    }

    @Override
    public void updateSelectedSticker(GiphyImageDTO giphyImageDTOs) {

        view.addSticker(giphyImageDTOs);
    }

}
