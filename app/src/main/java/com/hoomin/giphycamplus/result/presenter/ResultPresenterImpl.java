package com.hoomin.giphycamplus.result.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.result.model.GiphyModel;
import com.hoomin.giphycamplus.result.model.GiphyRepoDTO;
import com.hoomin.giphycamplus.util.ImageManager;
import com.hoomin.giphycamplus.util.Sticker;

import java.io.File;
import java.io.IOException;


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
        Sticker sticker = new Sticker(MyApplication.getMyContext(),"giphy.gif");
//        Bitmap baseBitmap = BitmapFactory.decodeResource(MyApplication.getMyContext().getResources(), R.drawable.boostcamp);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;


            Bitmap src = BitmapFactory.decodeFile(albumImageFile.getAbsolutePath(),options);
            Bitmap baseBitmap = Bitmap.createScaledBitmap(src,src.getWidth(),src.getHeight(),true);
//            Bitmap baseBitmap = MediaStore.Images.Media.getBitmap(
//                    MyApplication.getMyContext().getContentResolver(), albumImageURI);
            imageManager.new mergeBitmapTask(sticker).execute(baseBitmap);
//        File file = new File(albumImagePath);
//        Bitmap baseBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        Bitmap baseBitmap = null;
//        try {
//            baseBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            Log.i("baseBitmap", file.getAbsolutePath());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        baseBitmap.getWidth();
//        Log.i("baseBitmap", file.getAbsolutePath());
//        Log.i("baseBitmap", String.valueOf(baseBitmap));
//        Bitmap bitmap = null;
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getMyContext().getContentResolver(), Uri.parse(albumImagePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        imageManager.new mergeBitmapTask(sticker).execute(bitmap);
    }

    @Override
    public void loadSticker() {
        if(giphyModel == null){
            return;
        }
        giphyModel.callSticker();
    }

    @Override
    public void update(GiphyRepoDTO models) {
        if(models == null){
            return;
        }
        if(models.getData() != null){
            Log.i("loadSticker","models.getData()널아님");
            view.updateReaction(models.getData().get(0).getImages().getFixed_height().getUrl());
        }
    }
}
