package com.hoomin.giphycamplus.base.domain;


import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Hooo on 2017-02-13.
 */

public class GiphyImageDTO extends RealmObject implements Serializable {
    private String url;
    private String width;
    private String height;
    private String size;
    private String frames;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getFrames() {
        return frames;
    }

    public void setFrames(String frames) {
        this.frames = frames;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
//    private String mp4;
//    private String mp4Size;
//    private String webp;
//    private String webpSize;
}
