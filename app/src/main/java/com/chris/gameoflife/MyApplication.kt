package com.chris.gameoflife

import android.app.Application

/**
 * @author chenchris on 2019-06-15.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ScreenUtil.init(this)
    }
}