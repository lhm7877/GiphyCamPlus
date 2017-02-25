package com.hoomin.giphycamplus.stickerList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;
import com.hoomin.giphycamplus.base.domain.GiphyRepoDTO;
import com.hoomin.giphycamplus.base.util.Define;
import com.hoomin.giphycamplus.result.model.GiphyModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StickerListActivity extends Activity {

    @BindView(R.id.rv_sticker)
    protected RecyclerView rv_sticker;
    @BindView(R.id.etv_search)
    protected EditText etv_search;
    @BindView(R.id.ibtn_sticker_in_listview)
    protected ImageButton ibtn_sticker_in_listview;
    @BindView(R.id.ibtn_sticker_filled_in_listview)
    protected ImageButton ibtn_sticker_filled_in_listview;


    private StickerRecyclerViewAdapter stickerRecyclerViewAdapter;
    private RecyclerView.LayoutManager rv_layoutManager;


    private Realm mRealm;

    private Boolean isFilled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_list);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }

    private void init() {
        isFilled = getIntent().getBooleanExtra("isFlled",false);
        if(isFilled){
            Log.i("isFilled", String.valueOf(isFilled));
            ibtn_sticker_in_listview.setBackgroundResource(R.drawable.stickerpressed);
        }else{
            Log.i("isFilled", String.valueOf(isFilled));
            ibtn_sticker_filled_in_listview.setBackgroundResource(R.drawable.stickerfilledpressed);
        }


        mRealm = Realm.getDefaultInstance();
        final RealmResults<GiphyDataDTO> giphyDataDTOs = mRealm.where(GiphyDataDTO.class).findAll();
        rv_layoutManager = new GridLayoutManager(this,4);
        rv_sticker.setLayoutManager(rv_layoutManager);
        stickerRecyclerViewAdapter = new StickerRecyclerViewAdapter(this,mRealm,giphyDataDTOs);
        rv_sticker.setAdapter(stickerRecyclerViewAdapter);
        etv_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        mRealm.deleteAll();
                    }
                });
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String stringSticker = "";
                    if(!isFilled){
                        stringSticker = " sticker";
                    }
                    GiphyModel.GiphyRepoService giphyRepoService = GiphyModel.GiphyRepoService.retrofit.create(GiphyModel.GiphyRepoService.class);
                    Call<GiphyRepoDTO> call = giphyRepoService.repoStickerList(
                            v.getText().toString()+stringSticker, Define.GIPHY_API_KEY);
                    call.enqueue(dataCallBackListener);
                }
                return false;
            }
        });
    }
    private Callback<GiphyRepoDTO> dataCallBackListener = new Callback<GiphyRepoDTO>() {
        @Override
        public void onResponse(Call<GiphyRepoDTO> call, Response<GiphyRepoDTO> response) {
            if (response.isSuccessful()) {
                mRealm = Realm.getDefaultInstance();
                mRealm.beginTransaction();
                mRealm.copyToRealm(response.body());
                mRealm.commitTransaction();
                stickerRecyclerViewAdapter.notifyDataSetChanged();
            }
//            Log.i("intent","response 실패");
        }

        @Override
        public void onFailure(Call<GiphyRepoDTO> call, Throwable t) {

        }
    };

    @OnClick(R.id.ibtn_sticker_filled_in_listview)void clickStickerFilled(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.deleteAll();
            }
        });
        stickerRecyclerViewAdapter.notifyDataSetChanged();
        isFilled = true;
        ibtn_sticker_in_listview.setBackgroundResource(R.drawable.stickerpressed);
        ibtn_sticker_filled_in_listview.setBackgroundResource(R.drawable.stickerfilled100);
    }

    @OnClick(R.id.ibtn_sticker_in_listview) void clickSticker(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.deleteAll();
            }
        });
        stickerRecyclerViewAdapter.notifyDataSetChanged();
        isFilled = false;
        ibtn_sticker_filled_in_listview.setBackgroundResource(R.drawable.stickerfilledpressed);
        ibtn_sticker_in_listview.setBackgroundResource(R.drawable.sticker100);
    }

    @OnClick(R.id.ibtn_back_in_listview) void clickBack(){
        finish();
    }


//    private void resetRealm() {
//        Realm.deleteRealm(getRealmConfig());
//    }
//
//    public RealmConfiguration getRealmConfig() {
//        return new RealmConfiguration
//                .Builder(this)
//                .setModules(Realm.getDefaultModule(), new NYTimesModule())
//                .build();
//    }

}

