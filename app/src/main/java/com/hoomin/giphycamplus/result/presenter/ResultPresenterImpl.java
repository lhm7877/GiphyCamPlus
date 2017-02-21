package com.hoomin.giphycamplus.result.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.base.util.ImageManager;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
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
    public void loadSticker() {
        if (giphyModel == null) {
            return;
        }
        giphyModel.callSticker();
    }

    @Override
    public void loadSeletedSticker(final int position) {

//        view.
        new AsyncTask<Integer, Void, Sticker>() {
            @Override
            protected Sticker doInBackground(Integer... params) {
                mRealm = Realm.getDefaultInstance();
                RealmResults<GiphyDataDTO> giphyDataDTOs = mRealm.where(GiphyDataDTO.class).findAll();
                GiphyImageDTO giphyImageDTO = giphyDataDTOs.get(position).getImages().getFixed_height();
                Log.i("testLog",giphyImageDTO.getUrl());
                return giphyModel.callSelectedSticker(giphyImageDTO);
            }

            @Override
            protected void onPostExecute(Sticker sticker) {
                super.onPostExecute(sticker);
                view.addSticker(sticker);
            }
        }.execute();
//        view.addSticker(giphyModel.callSelectedSticker(position));
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

}
