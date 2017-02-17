package com.hoomin.giphycamplus.stickerList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hoomin.giphycamplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hooo on 2017-02-15.
 */

public class StickerViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.iv_itemSticker)
    protected ImageView iv_itemSticker;

    public StickerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
}
