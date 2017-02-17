package com.hoomin.giphycamplus.base.domain;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyRepoDTO extends RealmObject implements Serializable {
    private RealmList<GiphyDataDTO> data;

    public RealmList<GiphyDataDTO> getData() {
        return data;
    }

    public void setData(RealmList<GiphyDataDTO> data) {
        this.data = data;
    }
}
