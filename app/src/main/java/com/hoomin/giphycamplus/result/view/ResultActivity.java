package com.hoomin.giphycamplus.result.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoomin.giphycamplus.R;
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
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ResultActivity extends AppCompatActivity implements ResultPresenter.View {
    public static final int SELECT_STICKER_REQUEST_CODE = 123;

    @BindView(R.id.activity_result)
    protected FrameLayout activity_result;
    @BindView(R.id.iv_base)
    protected ImageView iv_base;
    @BindView(R.id.ibtn_sticker)
    protected ImageButton ibtn_sticker;
    @BindView(R.id.ibtn_sticker_filled)
    protected ImageButton ibtn_sticker_filled;
    @BindView(R.id.ibtn_save)
    protected ImageButton ibtn_save;
    @BindView(R.id.ibtn_plus)
    protected ImageButton ibtn_plus;
    @BindView(R.id.ibtn_minus)
    protected ImageButton ibtn_minus;


    private ArrayList<Sticker> stickers;

    //TODO: 어떤거 쓰지
    PhotoView photoView;
//    ImageView iv_sticker;

    private ResultPresenterImpl resultPresenter;
    private File albumImageFile;

    private Bitmap baseBitmap;

    PhotoViewAttacher mAttacher;

    Boolean isFilled = true;

    private PhotoView currentPhotoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ibtn_sticker.setBackgroundResource(R.drawable.sticker100);
        ibtn_sticker_filled.setBackgroundResource(R.drawable.stickerfilled100);
    }

    private void init() {
        resultPresenter = new ResultPresenterImpl();
        resultPresenter.attachView(this);

        Log.i("lifecycle", "result OnCreate");
        String url = getIntent().getStringExtra("baseImage");
        albumImageFile = new File(url);
        Log.i("baseImage", url);
        Bitmap mBitmap = BitmapFactory.decodeFile(url);
        iv_base.setImageBitmap(mBitmap);
        //같은 url 재사용 시 이미지가 안바뀜
//        Glide.with(this).load(albumImageFile)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_base);
        stickers = new ArrayList<>();

        ibtn_plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentPhotoview != null) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            ibtn_plus.setBackgroundResource(R.drawable.plusfilled120);
                            currentPhotoview.getLayoutParams().height *= 1.1;
                            currentPhotoview.getLayoutParams().width *= 1.1;
                            currentPhotoview.requestLayout();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            ibtn_plus.setBackgroundResource(R.drawable.plus120);
                            break;
                        }
                    }
                }
                return true;
            }
        });

        ibtn_minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (currentPhotoview != null) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN: {
                            ibtn_minus.setBackgroundResource(R.drawable.minusfilled120);
                            currentPhotoview.getLayoutParams().height *= 0.9;
                            currentPhotoview.getLayoutParams().width *= 0.9;
                            currentPhotoview.requestLayout();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            ibtn_minus.setBackgroundResource(R.drawable.minus120);
                            break;
                        }
                    }
                }
                return true;
            }
        });
    }

    @OnClick(R.id.ibtn_save)
    void clickSave() {
        resultPresenter.saveImage(albumImageFile, stickers);
    }


    @OnClick(R.id.ibtn_sticker_filled)
    void clickStickerFilled() {
        isFilled = true;
        resultPresenter.loadSticker(isFilled);
        ibtn_sticker.setBackgroundResource(R.drawable.stickerpressed);
    }

    @OnClick(R.id.ibtn_sticker)
    void clickSticker() {
        isFilled = false;
        resultPresenter.loadSticker(isFilled);
        ibtn_sticker_filled.setBackgroundResource(R.drawable.stickerfilledpressed);
    }

    @OnClick(R.id.ibtn_back)
    void backButton() {
        finish();
    }


    @Override
    public void updateReaction(Response<?> response) {
        Intent intent = new Intent(this, StickerListActivity.class);
        intent.putExtra("isFilled", this.isFilled);
        startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
    }


    @Override
    public void addSticker(Sticker sticker) {
        photoView = sticker.getPhotoView();
        Glide.with(this)
                .load(sticker.getGiphyImageDTO().getUrl())
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);
        photoView.setOnTouchListener(myTouchListener);
        photoView.setTag(89);
        currentPhotoview = photoView;
        activity_result.addView(photoView);
        stickers.add(sticker);
    }

    View.OnTouchListener myTouchListener = new View.OnTouchListener() {
        private int xDelta;
        private int yDelta;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) view.getLayoutParams();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    currentPhotoview = (PhotoView) view;
                    Log.i("currentpv", String.valueOf(currentPhotoview.getWidth()));
                    xDelta = x - layoutParams1.leftMargin;
                    yDelta = y - layoutParams1.topMargin;
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
                case MotionEvent.ACTION_MOVE:
                    layoutParams1.leftMargin = x - xDelta;
                    layoutParams1.topMargin = y - yDelta;
                    layoutParams1.rightMargin = 0;
                    layoutParams1.bottomMargin = 0;
                    view.setLayoutParams(layoutParams1);
                    return true;
            }
            activity_result.invalidate();
            return false;
        }
    };


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
