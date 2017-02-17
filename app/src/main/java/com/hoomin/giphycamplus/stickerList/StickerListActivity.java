package com.hoomin.giphycamplus.stickerList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;


import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class StickerListActivity extends Activity {

    @BindView(R.id.ibtn_sticker)
    protected ImageButton ibtn_sticker;
    @BindView(R.id.ibtn_text)
    protected ImageButton ibtn_text;
    @BindView(R.id.ibtn_pencil)
    protected ImageButton ibtn_pencil;
    @BindView(R.id.rv_sticker)
    protected RecyclerView rv_sticker;

    private StickerRecyclerViewAdapter stickerRecyclerViewAdapter;
    private RecyclerView.LayoutManager rv_layoutManager;


    private Realm mRealm;

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
        mRealm = Realm.getDefaultInstance();
        RealmResults<GiphyDataDTO> giphyDataDTOs = mRealm.where(GiphyDataDTO.class).findAll();
        rv_layoutManager = new GridLayoutManager(this,4);
        rv_sticker.setLayoutManager(rv_layoutManager);
        stickerRecyclerViewAdapter = new StickerRecyclerViewAdapter(this,mRealm,giphyDataDTOs);
        rv_sticker.setAdapter(stickerRecyclerViewAdapter);
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

