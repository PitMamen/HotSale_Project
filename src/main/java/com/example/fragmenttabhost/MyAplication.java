package com.example.fragmenttabhost;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Richie on 2017/3/15.
 */

public class MyAplication  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

    }
}
