package com.hoomin.giphycamplus.result.model;

import android.util.Log;

import com.hoomin.giphycamplus.base.domain.GiphyRepoDTO;
import com.hoomin.giphycamplus.base.util.Define;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyModel {
    private GiphyRepoDTO giphyRepoDTOList = null;
    //    private GiphyDataDTO GiphyRepoDTO = null;
    private GiphyModel.GiphyModelDataChange modelDataChange;

    interface GiphyRepoService {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.giphy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        @GET("/v1/stickers/search")
        Call<GiphyRepoDTO> repoStickerList(
                @Query("q") String q,
                @Query("api_key") String api_key);
    }

    public interface GiphyModelDataChange {
        void update(GiphyRepoDTO models);
    }

    public void setOnChangeListener(GiphyModel.GiphyModelDataChange dataChange) {
        modelDataChange = dataChange;
    }

    public void callSticker() {
        GiphyModel.GiphyRepoService giphyRepoService = GiphyRepoService.retrofit.create(GiphyModel.GiphyRepoService.class);
        Call<GiphyRepoDTO> call = giphyRepoService.repoStickerList("cat", Define.GIPHY_API_KEY);
        call.enqueue(dataCallBackListener);
    }

    private Callback<GiphyRepoDTO> dataCallBackListener = new Callback<GiphyRepoDTO>() {
        @Override
        public void onResponse(Call<GiphyRepoDTO> call, Response<GiphyRepoDTO> response) {
            if (response.isSuccessful()) {
                Log.i("loadSticker","response성공");
                giphyRepoDTOList = response.body();
                Log.i("loadSticker", String.valueOf(modelDataChange));
                if (modelDataChange != null) {
                    Log.i("loadSticker","modelDataChange 널 아님");
                    modelDataChange.update(giphyRepoDTOList);
                }
            }
        }

        @Override
        public void onFailure(Call<GiphyRepoDTO> call, Throwable t) {

        }
    };
}
