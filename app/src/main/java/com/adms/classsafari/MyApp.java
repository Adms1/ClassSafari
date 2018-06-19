package com.adms.classsafari;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by admsandroid on 3/5/2018.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "font/TitilliumWeb-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "font/TitilliumWeb-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "font/TitilliumWeb-Regular.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "font/TitilliumWeb-Regular.ttf");


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
