package com.hoomin.giphycamplus.result.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.result.presenter.ResultPresenter;
import com.hoomin.giphycamplus.result.presenter.ResultPresenterImpl;
import com.hoomin.giphycamplus.stickerList.StickerListActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity implements ResultPresenter.View {
    public static final int SELECT_STICKER_REQUEST_CODE = 123;
    @BindView(R.id.iv_base)
    protected ImageView iv_base;
    @BindView(R.id.ibtn_sticker)
    protected ImageButton ibtn_sticker;
    @BindView(R.id.ibtn_text)
    protected ImageButton ibtn_text;
    @BindView(R.id.ibtn_pencil)
    protected ImageButton ibtn_pencil;
    @BindView(R.id.ibtn_save)
    protected ImageButton ibtn_save;

    private ResultPresenterImpl resultPresenter;
    private File albumImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ButterKnife.bind(this);
        init();

    }

    private void init() {
        resultPresenter = new ResultPresenterImpl();
        resultPresenter.attachView(this);

        albumImageFile = (File) getIntent().getSerializableExtra("baseImage");
        Glide.with(this).load(albumImageFile).into(iv_base);
    }

    @OnClick(R.id.ibtn_save)
    void clickSave() {
        resultPresenter.saveImage(albumImageFile);
    }

    @OnClick(R.id.ibtn_sticker)
    void clickSticker() {
        Toast.makeText(this, "스티커클릭", Toast.LENGTH_SHORT).show();
        resultPresenter.loadSticker();
    }


    @Override
    public void updateReaction(Response<?> response) {
        Intent intent = new Intent(this, StickerListActivity.class);
        startActivityForResult(intent,SELECT_STICKER_REQUEST_CODE);
//        startActivity(intent);
    }

    @Override
    public void addSticker(Intent data) {
        ImageView iv = new ImageView(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_STICKER_REQUEST_CODE) {
                if (data != null) {
                    resultPresenter.loadSeletedSticker(data);
                }

            }

        }
    }
}
