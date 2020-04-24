package com.qiyei.music

import android.app.Application
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.share.ShareManager

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
        instance = this

        AudioPlayerManager.init(this)
        ShareManager.init(this)
    }

}