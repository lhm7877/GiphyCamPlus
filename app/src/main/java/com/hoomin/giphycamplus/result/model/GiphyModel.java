package com.hoomin.giphycamplus.result.model;

import android.content.Intent;
import android.util.Log;

import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;
import com.hoomin.giphycamplus.base.domain.GiphyRepoDTO;
import com.hoomin.giphycamplus.base.util.Define;

import io.realm.Realm;
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
private Realm mRelam;
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
        void update(Response<?> response);
        void updateSelectedSticker();
    }

    public void setOnChangeListener(GiphyModel.GiphyModelDataChange dataChange) {
        modelDataChange = dataChange;
    }

    public void callSticker() {
        GiphyModel.GiphyRepoService giphyRepoService = GiphyRepoService.retrofit.create(GiphyModel.GiphyRepoService.class);
        Call<GiphyRepoDTO> call = giphyRepoService.repoStickerList(
                MyApplication.getMyContext().getResources().getString(R.string.sticker_effect)
                , Define.GIPHY_API_KEY);
        call.enqueue(dataCallBackListener);
    }

    private Callback<GiphyRepoDTO> dataCallBackListener = new Callback<GiphyRepoDTO>() {
        @Override
        public void onResponse(Call<GiphyRepoDTO> call, Response<GiphyRepoDTO> response) {
            if (response.isSuccessful()) {
                mRelam = Realm.getDefaultInstance();
                mRelam.beginTransaction();
                mRelam.copyToRealm(response.body());
                mRelam.commitTransaction();

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

    public void callSelectedSticker(Intent position){
        modelDataChange.updateSelectedSticker();
    }
}
