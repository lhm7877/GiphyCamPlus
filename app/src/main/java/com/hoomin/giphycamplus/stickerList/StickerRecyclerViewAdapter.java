package com.hoomin.giphycamplus.stickerList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hoomin.giphycamplus.MyApplication;
import com.hoomin.giphycamplus.R;
import com.hoomin.giphycamplus.base.domain.GiphyDataDTO;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hooo on 2017-02-15.
 */

public class StickerRecyclerViewAdapter extends RecyclerView.Adapter<StickerViewHolder> {
    private Realm mRealm;
    private RealmResults<GiphyDataDTO> mResults;
    private Context mContext;

    public StickerRecyclerViewAdapter(Context context, Realm realm, RealmResults<GiphyDataDTO> results) {
        this.mContext = context;
        this.mRealm = realm;
        this.mResults = results;
    }

    @Override
    public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_view, parent, false);
        StickerViewHolder viewHolder = new StickerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final StickerViewHolder holder, int position) {
        final int mPosition = holder.getAdapterPosition();
        if (mPosition >= 0) {
            Glide.with(MyApplication.getMyContext())
                    .load(mResults.get(mPosition).getImages().getFixed_height_still().getUrl())
                    .into(holder.iv_itemSticker);
            holder.iv_itemSticker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(MyApplication.getMyContext())
                            .load(mResults.get(mPosition).getImages().getFixed_height().getUrl())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(holder.iv_itemSticker);
                }
            });
            holder.iv_itemSticker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("selectImage", "롱클릭");
                    Intent intent = new Intent();
                    intent.putExtra("imageUrl", mResults.get(mPosition).getImages().getFixed_height().getUrl());
//                intent.putExtra("LocationX",(v.getX()-(v.getWidth()/2)));
//                intent.putExtra("LocationY",(v.getY()-(v.getWidth()/2)));
                    int[] pos = new int[2];
                    v.getLocationInWindow(pos);

                    Log.i("location", String.valueOf(pos[0]));
                    intent.putExtra("pos", pos);
                    ((Activity) mContext).setResult(RESULT_OK, intent);
                    ((Activity) mContext).finish();
                    return true;
                }
            });
            //Drag하고 엑티비티 종료
        /*
        holder.iv_itemSticker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData clipData = ClipData.newPlainText("dragtext","dragtext");
                view.startDrag(clipData,new View.DragShadowBuilder(view),null,0);
                return false;
            }
        });
        holder.iv_itemSticker.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.i("drag","ACTION_DRAG_STARTED");
                        ((Activity)mContext).finish();
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i("drag","ACTION_DRAG_ENTERED");
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.i("drag","ACTION_DRAG_EXITED");
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.i("drag","ACTION_DRAG_LOCATION");
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i("drag","ACTION_DRAG_ENDED");
                        return true;
                    case DragEvent.ACTION_DROP:
//                        View view = (View)event.getLocalState();
//                        FrameLayout container = (FrameLayout)v;
//                        ImageView oldView = (ImageView)view;
//                        ImageView newView = new ImageView(mContext);
//                        newView.setImageBitmap(((BitmapDrawable)oldView.getDrawable()).getBitmap());
//                        container.addView(newView);
//                        view.setVisibility(View.VISIBLE);

                        Intent intent = new Intent();
                        intent.putExtra("imageUrl",mResults.get(position).getImages().getFixed_height().getUrl());
                        intent.putExtra("LocationX",(event.getX()-(v.getWidth()/2)));
                        intent.putExtra("LocationY",(event.getY()-(v.getWidth()/2)));
                        ((Activity)mContext).setResult(11,intent);
                        Log.i("drag","ACTION_DROP");
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });*/
        }
    }


    @Override
    public int getItemCount() {
        return mResults.size();
    }

}