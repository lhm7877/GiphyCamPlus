package com.hoomin.giphycamplus.result.presenter;

import android.util.Log;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.base.util.ImageManager;
import com.hoomin.giphycamplus.base.util.Sticker;
import com.hoomin.giphycamplus.viewmodel.Layer;
import com.hoomin.giphycamplus.widget.MotionView;
import com.hoomin.giphycamplus.widget.entity.ImageEntity;

import java.io.File;

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
    public void loadSeletedSticker(int data) {
//        view.
        giphyModel.callSelectedSticker(data);
//        view.addSticker(data);
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
    public void updateSelectedSticker(final GiphyImageDTO giphyImageDTOs) {
        final MotionView mv_result = view.getMv_result();
        mv_result.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
                ImageEntity entity = new ImageEntity(
                        layer,
                        giphyImageDTOs,
                        Integer.valueOf(mv_result.getWidth()),
                        Integer.valueOf(mv_result.getHeight())
                );
                mv_result.addEntityAndPosition(entity);
            }
        });
    }

}
