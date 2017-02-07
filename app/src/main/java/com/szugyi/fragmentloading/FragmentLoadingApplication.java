package com.szugyi.fragmentloading;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class FragmentLoadingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}