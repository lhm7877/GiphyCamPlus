package com.hoomin.giphycamplus.result.view;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyImageDTO;
import com.hoomin.giphycamplus.base.util.Sticker;
import com.hoomin.giphycamplus.result.presenter.ResultPresenter;
import com.hoomin.giphycamplus.result.presenter.ResultPresenterImpl;
import com.hoomin.giphycamplus.stickerList.StickerListActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity implements ResultPresenter.View {
    public static final int SELECT_STICKER_REQUEST_CODE = 123;

    @BindView(R.id.activity_result)
    protected FrameLayout activity_result;
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

    //Todo: 네 개 중 하나만 필요
    private ArrayList<ImageView> iv_Stickers;
    private ArrayList<GiphyImageDTO> giphyImageDTOs;
    private int stickerIndex = 0;
    private ArrayList<Sticker> stickers;


    ImageView iv_sticker;

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
        stickers = new ArrayList<>();
    }

    @OnClick(R.id.ibtn_save)
    void clickSave() {
        resultPresenter.saveImage(albumImageFile,stickers);
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
    }


    @Override
    public void addSticker(Sticker sticker) {
        iv_sticker = sticker.getImageView();
        Glide.with(this)
                .load(sticker.getGiphyImageDTO().getUrl())
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_sticker);

//        iv_sticker.setTag(stickerIndex);
        //결과 화면의 FrameLayout에 Imageview 추가
        activity_result.addView(iv_sticker);

        //터치 리스너 등록
        iv_sticker.setOnTouchListener(testListener);

        stickers.add(sticker);
//        iv_Stickers.add(iv_sticker);
//        imageDTO.setImageView(iv_sticker);
//        giphyImageDTOs.add(imageDTO);

        //TODO: stickerIndex 필요하나?
        stickerIndex++;
    }

    View.OnTouchListener testListener = new View.OnTouchListener() {
        private int xDelta;
        private int yDelta;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) view.getLayoutParams();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    xDelta = x - layoutParams1.leftMargin;
                    yDelta = y - layoutParams1.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    layoutParams1.leftMargin = x - xDelta;
                    layoutParams1.topMargin = y - yDelta;
                    layoutParams1.rightMargin = 0;
                    layoutParams1.bottomMargin = 0;
                    view.setLayoutParams(layoutParams1);
                    break;
            }
            activity_result.invalidate();
            return true;
        }
    };

    @Override
    public void dragandDropSticker() {
        for (int i = 0; i < iv_Stickers.size(); i++) {
            final int finalI = i;
            iv_Stickers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MyApplication.getMyContext(), "클릭"
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_STICKER_REQUEST_CODE) {
                if (data != null) {
                    int position = data.getIntExtra("imagePosition", 0);
                    resultPresenter.loadSeletedSticker(position);
                }
            }
        }
    }

}
