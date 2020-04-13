/**
 * @author Created by qiyei2015 on 2020/4/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioService 音频播放后台服务
 */
package com.qiyei.audio.component.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.component.notification.NotificationHelper
import com.qiyei.audio.model.AudioBean
import java.io.Serializable

class AudioService :Service() {


    companion object {
        const val TAG = "AudioService"
        const val DATA = "data"
        /**
         * 启动Service
         */
        fun startService(beans:MutableList<AudioBean>){
            var intent = Intent(AudioPlayerManager.mContext,AudioService::class.java)
            intent.putExtra(DATA,beans as Serializable)
            AudioPlayerManager.mContext.startService(intent)
        }
    }

    private var mBeans:MutableList<AudioBean>? = null

    private var mManager:AudioPlayerManager? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mBeans = intent?.getSerializableExtra(DATA) as MutableList<AudioBean>
        Log.i(TAG,"onStartCommand startId=$startId")
        //弹通知
        NotificationHelper.startNotification(this,mManager?.getCurrentPlay()!!)
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
        mBeans?.clear()
        mBeans = null;
    }
}