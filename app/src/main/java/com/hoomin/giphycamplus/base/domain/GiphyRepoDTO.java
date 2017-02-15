package com.hoomin.giphycamplus.base.domain;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyRepoDTO extends RealmObject{
    private RealmList<GiphyDataDTO> data;

    public List<GiphyDataDTO> getData() {
        return data;
    }

    public void setData(RealmList<GiphyDataDTO> data) {
        this.data = data;
    }
}
