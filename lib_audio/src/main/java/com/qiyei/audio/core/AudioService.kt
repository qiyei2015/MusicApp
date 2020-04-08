/**
 * @author Created by qiyei2015 on 2020/4/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioService 音频播放后台服务
 */
package com.qiyei.audio.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.qiyei.audio.api.AudioPlayerManager

class AudioService :Service() {


    companion object {
        const val TAG = "AudioService"

        fun startService(){

        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}