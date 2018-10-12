package com.glg.baselib.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.glg.baselib.BuildConfig;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

public class BaseApplication extends Application {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });

        MMKV.initialize(this);


        FileDownloader.setup(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    // 获取Application
    public static Context getMyApplication() {
        return instance;
    }
}
