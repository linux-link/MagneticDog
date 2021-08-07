package com.wujia.magneticdog.base

import android.app.Application
import com.wujia.jetpack.BaseApplication

abstract class WeiboBaseApplication : BaseApplication() {

    companion object {
        val moduleApps = arrayOf(
            "com.wujia.splash.SplashApplication",
            "com.wujia.login.LoginApplication"
        )
    }

    override fun onCreate() {
        super.onCreate()
        initBmob(this)
    }

    private fun initBmob(application: Application) {
//        val config: BmobConfig = BmobConfig.Builder(application)
//            .setApplicationId(BMOB_APPLICATION_ID)
//            .setConnectTimeout(BMOB_CONNECT_TIME_OUT)
//            .build()
//        Bmob.initialize(config)
    }

    abstract fun initModuleApp(application: Application)

    abstract fun initModuleData(application: Application)
}