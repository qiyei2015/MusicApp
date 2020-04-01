package com.qiyei.music

import android.app.Application

/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
class MusicApplication:Application() {

    companion object {
        lateinit var instance:MusicApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this;
    }

}