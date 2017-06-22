package com.example.yangjiang.utilsdmo;

import android.app.Application;

import com.yutils.YUtils;


/**
 * Created by yangc on 2017/5/5.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */
public class App extends Application {
    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        YUtils.initialize(this);

    }
}
