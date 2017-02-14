package com.hoomin.giphycamplus;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by Hooo on 2017-01-26.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Realm.init(this);
    }
    public static Context getMyContext(){
        return context;
    }
}
