package com.hoomin.giphycamplus.result.model;

import android.os.AsyncTask;
import android.util.Log;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.base.domain.GiphyRepoDTO;
import com.hoomin.giphycamplus.base.util.Define;
import com.hoomin.giphycamplus.base.util.Sticker;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyModel {
    private GiphyRepoDTO giphyRepoDTOList = null;
    //    private GiphyDataDTO GiphyRepoDTO = null;
    private GiphyModel.GiphyModelDataChange modelDataChange;
    private Realm mRealm;

    public interface GiphyRepoService {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.giphy.com//")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        @GET("/v1/gifs/search")
        Call<GiphyRepoDTO> repoStickerList(
                @Query("q") String q,
                @Query("api_key") String api_key);


    }

    public interface GifInputStreamService {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.giphy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        @GET
        Call<ResponseBody> getInputStream(
                @Url String url);
    }


    public interface GiphyModelDataChange {
        void update(Response<?> response);
    }

    public void setOnChangeListener(GiphyModel.GiphyModelDataChange dataChange) {
        modelDataChange = dataChange;
    }

    public void callSticker(Boolean isFilled) {
        GiphyModel.GiphyRepoService giphyRepoService = GiphyRepoService.retrofit.create(GiphyModel.GiphyRepoService.class);
        String stringSticker = "";
        if(!isFilled){
            stringSticker = " sticker";
        }
            Call<GiphyRepoDTO> call = giphyRepoService.repoStickerList(
                    MyApplication.getMyContext().getResources().getString(R.string.sticker_effect)+stringSticker
                    , Define.GIPHY_API_KEY);
            call.enqueue(dataCallBackListener);
    }

    private Callback<GiphyRepoDTO> dataCallBackListener = new Callback<GiphyRepoDTO>() {
        @Override
        public void onResponse(Call<GiphyRepoDTO> call, Response<GiphyRepoDTO> response) {
            if (response.isSuccessful()) {
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mRealm.deleteAll();
                    }
                });
                mRealm.beginTransaction();
                mRealm.copyToRealm(response.body());
                mRealm.commitTransaction();

                if (modelDataChange != null) {
                    modelDataChange.update(response);
                }
            }
//            Log.i("intent","response 실패");
        }

        @Override
        public void onFailure(Call<GiphyRepoDTO> call, Throwable t) {

        }
    };

    public Sticker callSelectedSticker(final int position) {
        RealmResults<GiphyDataDTO> giphyDataDTOs = mRealm.where(GiphyDataDTO.class).findAll();
//        if(giphyDataDTOs.get(position).getImages().getFixed_height().getSize())
        GiphyImageDTO giphyImageDTO = giphyDataDTOs.get(position).getImages().getFixed_height();
        Log.i("sizeCheck","id : "+giphyImageDTO.getUrl());
        final Sticker sticker = new Sticker(giphyImageDTO);

        //inputStream 가져옴
        GiphyModel.GifInputStreamService gifInputStreamService = GiphyModel.GifInputStreamService.retrofit.create(GiphyModel.GifInputStreamService.class);
        Call<ResponseBody> callback = gifInputStreamService.getInputStream(giphyImageDTO.getUrl());

        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("callbackRequest", String.valueOf(response.body().byteStream()));
                sticker.setInputStream(response.body().byteStream());
                Log.i("response", String.valueOf(sticker.getInputStream()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

//       new AsyncTask<Void,Void,InputStream>()
//        try {
//            Response<ResponseBody> body = callback.execute();
//            sticker.setInputStream(body.body().byteStream());
//            Log.i("response", String.valueOf(sticker.getInputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return sticker;
    }

    private class getInputStreamAsyncTask extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
        }
    }
}
