package com.example.yangjiang.utilsdmo;

import android.app.Application;

import com.tengxunyun.OSSService;
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
        OSSService.getInstance().init(this);
        OSSService.getInstance().creareDir();

    }
}
