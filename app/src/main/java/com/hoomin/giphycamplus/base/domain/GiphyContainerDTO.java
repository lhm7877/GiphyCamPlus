package com.hoomin.giphycamplus.base.domain;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyContainerDTO extends RealmObject implements Serializable {
    private GiphyImageDTO fixed_height;
    private GiphyImageDTO fixed_height_still;
    private GiphyImageDTO original;
    private GiphyImageDTO fixed_height_downsampled;
    private GiphyImageDTO fixed_height_small;

    public GiphyImageDTO getFixed_height_small() {
        return fixed_height_small;
    }

    public void setFixed_height_small(GiphyImageDTO fixed_height_small) {
        this.fixed_height_small = fixed_height_small;
    }

    public GiphyImageDTO getFixed_height_downsampled() {
        return fixed_height_downsampled;
    }

    public void setFixed_height_downsampled(GiphyImageDTO fixed_height_downsampled) {
        this.fixed_height_downsampled = fixed_height_downsampled;
    }

    public GiphyImageDTO getFixed_height_still() {
        return fixed_height_still;
    }

    public void setFixed_height_still(GiphyImageDTO fixed_height_still) {
        this.fixed_height_still = fixed_height_still;
    }

    public GiphyImageDTO getFixed_height() {
        return fixed_height;
    }

    public void setFixed_height(GiphyImageDTO fixed_height) {
        this.fixed_height = fixed_height;
    }

    public GiphyImageDTO getOriginal() {
        return original;
    }

    public void setOriginal(GiphyImageDTO original) {
        this.original = original;
    }
}
