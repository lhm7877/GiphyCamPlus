package com.hoomin.giphycamplus.result.model;

import io.realm.RealmObject;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyContainerDTO extends RealmObject{
    private GiphyImageDTO fixed_height;
    private GiphyImageDTO fixed_height_still;
    private GiphyImageDTO original;

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
