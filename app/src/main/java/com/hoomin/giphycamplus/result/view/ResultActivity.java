package com.hoomin.giphycamplus.result.view;

import android.content.ClipData;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.util.ImageManager;
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
    @BindView(R.id.ibtn_text)
    protected ImageButton ibtn_text;
    @BindView(R.id.ibtn_pencil)
    protected ImageButton ibtn_pencil;
    @BindView(R.id.ibtn_save)
    protected ImageButton ibtn_save;

    private ArrayList<Sticker> stickers;

    //TODO: 어떤거 쓰지
    PhotoView photoView;
//    ImageView iv_sticker;

    private ResultPresenterImpl resultPresenter;
    private File albumImageFile;

    private Bitmap baseBitmap;

    PhotoViewAttacher mAttacher;

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

        Log.i("lifecycle","result OnCreate");
        String url = getIntent().getStringExtra("baseImage");
        albumImageFile = new File(url);
        Log.i("baseImage",url);
        Bitmap mBitmap = BitmapFactory.decodeFile(url);
        iv_base.setImageBitmap(mBitmap);
        //같은 url 재사용 시 이미지가 안바뀜
//        Glide.with(this).load(albumImageFile)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_base);
        stickers = new ArrayList<>();
    }

    @OnClick(R.id.ibtn_save)
    void clickSave() {
        Toast.makeText(MyApplication.getMyContext(), "저장클릭", Toast.LENGTH_SHORT).show();
        resultPresenter.saveImage(albumImageFile, stickers);
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
        photoView = sticker.getPhotoView();

//        iv_sticker = sticker.getImageView();
//        photoView = sticker.getPhotoView();
        Glide.with(this)
                .load(sticker.getGiphyImageDTO().getUrl())
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);
        photoView.setOnTouchListener(myTouchListener);

//        iv_sticker.setTag(stickerIndex);
        //결과 화면의 FrameLayout에 Imageview 추가
        activity_result.addView(photoView);
        //터치 리스너 등록
//        photoView.setOnTouchListener(testListener);
//        gestureImageView.setOnTouchListener(testListener);
        stickers.add(sticker);
//        iv_Stickers.add(iv_sticker);
//        imageDTO.setImageView(iv_sticker);
//        giphyImageDTOs.add(imageDTO);


//        mAttacher = new PhotoViewAttacher(photoView);
//        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//            @Override
//            public void onViewTap(View view, float x, float y) {
//                Log.i("mylog","OnViewTapListener()");
//                view.setOnTouchListener(testListener);
//            }
//        });
//        mAttacher.update();


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
                    Log.i("mylog", "ActionDown");
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
