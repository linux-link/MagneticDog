package com.wj.arch;

import android.app.Application;

public class BaseApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
    }

    public static Application getApplication() {
        return sApplication;
    }

    private void setApplication(Application application) {
        sApplication = application;
    }
}
