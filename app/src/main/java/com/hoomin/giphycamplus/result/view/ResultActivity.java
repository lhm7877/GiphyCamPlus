package com.hoomin.giphycamplus.result.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.result.presenter.ResultPresenter;
import com.hoomin.giphycamplus.result.presenter.ResultPresenterImpl;
import com.hoomin.giphycamplus.stickerList.StickerListActivity;
import com.hoomin.giphycamplus.viewmodel.Layer;
import com.hoomin.giphycamplus.widget.MotionView;
import com.hoomin.giphycamplus.widget.TextEditorDialogFragment;
import com.hoomin.giphycamplus.widget.entity.ImageEntity;
import com.hoomin.giphycamplus.widget.entity.MotionEntity;
import com.hoomin.giphycamplus.widget.entity.TextEntity;

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
    @BindView(R.id.mv_result)
    protected MotionView mv_result;

    private ResultPresenterImpl resultPresenter;
    private File albumImageFile;
    protected View textEntityEditPanel;


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
        mv_result.setMotionViewCallback(motionViewCallback);
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
        startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
//        startActivity(intent);
    }

    @Override
    public void addSticker(Intent data) {
        mv_result.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
                Bitmap pica = BitmapFactory.decodeResource(getResources(), R.drawable.giphy);

                ImageEntity entity = new ImageEntity(layer, pica, mv_result.getWidth(), mv_result.getHeight());
                mv_result.addEntityAndPosition(entity);
            }
        });
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

    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {

        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
//            if (entity instanceof TextEntity) {
//                textEntityEditPanel.setVisibility(View.VISIBLE);
//            } else {
//                textEntityEditPanel.setVisibility(View.GONE);
//            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            startTextEntityEditing();
        }
    };

    private void startTextEntityEditing() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
    }
    @Nullable
    private TextEntity currentTextEntity() {
        if (mv_result != null && mv_result.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) mv_result.getSelectedEntity());
        } else {
            return null;
        }
    }
}
