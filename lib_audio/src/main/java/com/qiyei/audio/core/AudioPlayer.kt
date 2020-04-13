/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioPlayer
 */
package com.qiyei.audio.core

import android.content.Context
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.RequiresApi
import com.qiyei.audio.exception.AudioStatusException
import com.qiyei.audio.model.AudioBean


class AudioPlayer(private val mContext: Context) : MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    AudioFocusManager.AudioFocusChangeListener {

    companion object {
        const val TAG = "AudioPlayer"
        private const val TIME_MSG = 0x1
        private const val TIME_INTERVAL = 100
    }

    /**
     * 核心播放类
     */
    private var mMediaPlayer: AudioMediaPlayer? = AudioMediaPlayer()

    /**
     * 焦点管理器
     */
    private var mFocusManager: AudioFocusManager?

    /**
     * 音频状态监听器
     */
    private val mAudioStatusListeners: List<IAudioStatusListener> = listOf()

    /**
     * wifi lock 用于保活
     */
    private var mWifiLock: WifiManager.WifiLock? = (mContext.getSystemService(Context.WIFI_SERVICE)
            as WifiManager).createWifiLock(WifiManager.WIFI_MODE_FULL_LOW_LATENCY, TAG)

    /**
     * 播放的音频
     */
    private var mCurrentBean: AudioBean? = null

    /**
     * 是否是短暂失去焦点
     */
    private var isPausedByFocusLossTransient = false

    init {
        mMediaPlayer?.setOnCompletionListener(this)
        mMediaPlayer?.setOnBufferingUpdateListener(this)
        mMediaPlayer?.setOnPreparedListener(this)
        mMediaPlayer?.setOnErrorListener(this)
        mFocusManager = AudioFocusManager(mContext, this)
    }


    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIME_MSG -> {
                    if (getStatus() == Status.STARTED
                        || getStatus() == Status.PAUSED
                    ) {
                        mAudioStatusListeners.forEach {
                            it.onAudioProgress(getStatus(), getCurrentProgress(), getDuration())
                        }
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INTERVAL.toLong())
                    }
                }
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mAudioStatusListeners.forEach {
            it.onAudioCompleted(mCurrentBean)
        }
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }

    override fun onPrepared(mp: MediaPlayer?) {
        start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mAudioStatusListeners.forEach {
            it.onError(AudioStatusException(3, "播放错误"))
        }
        return false
    }

    override fun onAudioFocusGrant() {
        //获得焦点
        setVolumn(1.0f, 1.0f)
        if (isPausedByFocusLossTransient) {
            resume()
        }

        isPausedByFocusLossTransient = false
    }

    override fun onAudioFocusLoss() {
        //失去焦点，不再播放
        mMediaPlayer.let {
            pause()
        }
    }

    override fun onAudioFocusLossTransient() {
        //短暂失去焦点，后续会重新播放
        mMediaPlayer.let {
            pause()
        }
        isPausedByFocusLossTransient = true
    }

    override fun onAudioFocusLossDuck() {
        //瞬间失去焦点
        setVolumn(0.5f, 0.5f)
    }

    /**
     * 加载音频
     */
    fun load(bean: AudioBean) {
        try {//重置播放器
            mCurrentBean = bean
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(bean.uri)
            //准备
            mMediaPlayer?.prepareAsync()

            //发送视频加载事件
            mAudioStatusListeners.onEach {
                it.onAudioLoaded(bean)
            }
        } catch (e: Exception) {
            mAudioStatusListeners.forEach {
                it.onError(
                    AudioStatusException(
                        1,
                        "load fail"
                    )
                )
            }
        }
    }

    /**
     * prepare之后自动调用，外部不能调用
     */
    private fun start() {
        try {
            if (mFocusManager?.requestAudioFocus()!!) {
                mMediaPlayer?.start()
                //启用lock
                mWifiLock?.acquire()
                //更新进度
                //mHandler.send
                mAudioStatusListeners.forEach {
                    it.onAudioStarted(mCurrentBean)
                }
            } else {
                Log.w(TAG, "获取音频焦点失败")
            }
        } catch (e: Exception) {
            mAudioStatusListeners.forEach {
                it.onError(
                    AudioStatusException(
                        2,
                        "开始失败"
                    )
                )
            }
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        if (getStatus() == Status.STARTED) {
            mMediaPlayer?.pause()
            //关闭wifi锁
            if (mWifiLock?.isHeld!!) {
                mWifiLock?.release()
            }
            //取消音频焦点
            mFocusManager?.abandonAudioFocus()
            mAudioStatusListeners.forEach {
                it.onAudioPause(mCurrentBean)
            }
        }
    }

    /**
     * 继续播放
     */
    fun resume() {
        if (getStatus() == Status.PAUSED) {
            start()
        }
    }

    /**
     * 释放
     */
    fun release() {
        mMediaPlayer?.release()
        mMediaPlayer = null

        mFocusManager?.abandonAudioFocus()
        mFocusManager == null

        //关闭wifi
        if (mWifiLock?.isHeld!!) {
            mWifiLock?.release()
        }
        mWifiLock = null
        mHandler.removeCallbacksAndMessages(null)
        mAudioStatusListeners.onEach {
            it.onAudioReleased(mCurrentBean)
        }
        mCurrentBean = null
    }

    /**
     * 获取播放器状态
     */
    fun getStatus(): Status {
        return mMediaPlayer?.mStatus!!
    }

    /**
     * 获取当前播放进度
     */
    fun getCurrentProgress(): Int {
        if (getStatus() == Status.STARTED || getStatus() == Status.PAUSED) {
            return mMediaPlayer?.currentPosition!!
        }
        return -1
    }

    /**
     * 播放总时长
     */
    fun getDuration(): Int {
        if (getStatus() == Status.STARTED || getStatus() == Status.PAUSED) {
            return mMediaPlayer?.duration!!
        }
        return -1
    }

    /**
     * 设置音量
     * @param left 左声道
     * @param right 右声道
     */
    fun setVolumn(left: Float, right: Float) {
        mMediaPlayer?.setVolume(left, right)
    }
}