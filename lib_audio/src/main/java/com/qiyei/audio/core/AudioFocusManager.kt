/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioFocusManager 音频焦点管理器
 */
package com.qiyei.audio.core

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build


class AudioFocusManager(
    private val mContext: Context,
    private val mAudioFocusListener: AudioFocusChangeListener
) : AudioManager.OnAudioFocusChangeListener {

    /**
     * 原生音频管理器
     */
    private var mAudioManager: AudioManager =
        mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * 音频焦点请求
     */
    private lateinit var mFocusRequest: AudioFocusRequest

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build()
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> mAudioFocusListener.onAudioFocusGrant()
            AudioManager.AUDIOFOCUS_LOSS -> mAudioFocusListener.onAudioFocusLoss()
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> mAudioFocusListener.onAudioFocusLossTransient()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mAudioFocusListener.onAudioFocusLossDuck()
        }
    }

    /**
     * 申请焦点
     */
    fun requestAudioFocus(): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mAudioManager.requestAudioFocus(mFocusRequest) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            mAudioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    /**
     * 取消焦点
     */
    fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(mFocusRequest)
        } else {
            mAudioManager.abandonAudioFocus(this)
        }
    }

    interface AudioFocusChangeListener {

        /**
         * 获得焦点回调处理
         */
        fun onAudioFocusGrant()

        /**
         * 永久失去焦点回调处理
         */
        fun onAudioFocusLoss()

        /**
         * 短暂失去焦点，来电话
         */
        fun onAudioFocusLossTransient()

        /**
         * 瞬间失去焦点，比如短信提醒
         */
        fun onAudioFocusLossDuck()
    }
}