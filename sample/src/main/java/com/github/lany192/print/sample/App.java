package com.github.lany192.print.sample;

import android.app.Application;

import com.github.lany192.print.AppInfo;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInfo.init(getApplicationContext());
    }
}
