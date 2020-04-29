/**
 * @author Created by qiyei2015 on 2020/4/29.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoPlayer
 */
package com.qiyei.video.core

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Surface
import com.qiyei.video.api.VideoPlayerListener
import com.qiyei.video.utils.VideoUtils


class VideoPlayer(private val mContext: Context) : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    companion object {
        const val TAG = "VideoPlayer"
        const val RetryCount = 3

        private const val MSG_UPDATE_TIME = 0x01
        private const val MSG_UPDATE_SEEK_BAR = 0x02
        private const val TIME_INTERVAL = 100
        private const val TIME_DELAY_3_SEC = 3000L
    }

    /**
     * 播放器View
     */
    private lateinit var mVideoView: VideoView

    /**
     * 播放器监听器
     */
    private var mPlayerListener: VideoPlayerListener? = null

    /**
     * 播放的url
     */
    private lateinit var mUrl: String

    /**
     * 是否静音
     */
    private var isMute: Boolean = false

    /**
     * 播放器状态
     */
    private var mState: State = State.IDLE

    /**
     * 真实的播放器
     */
    private var mMediaPlayer: MediaPlayer? = null

    /**
     * 重试次数
     */
    private var mCount: Int = 0

    private var mCanPlay: Boolean = false

    var isRealPause: Boolean = false

    var isComplete: Boolean = false


    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_TIME -> {
                    if (getState() == State.PLAYING
                        || getState() == State.PAUSED
                    ) {
                        mVideoView.updateSeekBar(
                            getCurrentProgress(),
                            VideoUtils.formatDurationTime(getCurrentProgress())
                        )
                        sendEmptyMessageDelayed(MSG_UPDATE_TIME, TIME_INTERVAL.toLong())
                    }
                }
                MSG_UPDATE_SEEK_BAR -> {
                    mVideoView.hideSeekBar()
                }
            }
        }
    }


    override fun onPrepared(mp: MediaPlayer?) {
        mMediaPlayer = mp
        mMediaPlayer?.let {
            mCount = 0
            setState(State.PAUSED)
            resume()
        }
        mPlayerListener?.onPrepared()
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        mPlayerListener?.onBufferingUpdate(percent)
        Log.i(TAG, "onBufferingUpdate state=$mState")
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.i(TAG, "onCompletion state=$mState")
        mMediaPlayer?.let {
            it.setOnSeekCompleteListener(null)
            it.seekTo(0)
            it.pause()
        }
        isComplete = true
        setState(State.PAUSED)
        mVideoView.showPauseView()
        mPlayerListener?.onCompletion()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mPlayerListener?.onError(what, extra)
        return false
    }

    @Synchronized
    internal fun initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            val audioAttributes =
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build()
            mMediaPlayer?.setAudioAttributes(audioAttributes)
            mMediaPlayer?.setOnPreparedListener(this)
            mMediaPlayer?.setOnBufferingUpdateListener(this)
            mMediaPlayer?.setOnCompletionListener(this)
            mMediaPlayer?.setOnErrorListener(this)
        }
    }

    fun attachVideoView(videoView: VideoView) {
        mVideoView = videoView
        mVideoView.setVideoPlayer(this)
    }


    fun setSurface(surface: Surface) {
        mMediaPlayer?.setSurface(surface)
    }

    fun seekTo(msec: Int) {
        mMediaPlayer?.seekTo(msec)
    }

    /**
     * 加载后播放
     */
    fun load() {
        Log.i(TAG, "load,state=$mState")
        if (mState != State.IDLE) {
            Log.i(TAG, "load error state = ${mState}")
            return
        }
        mVideoView.showLoadingView()
        setState(State.IDLE)
        try {
            initMediaPlayer()
            mute(isMute)
            mMediaPlayer?.setDataSource(mUrl)
            mMediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.i(TAG, "load Exception = ${e.message}")
            //重试后退出
            stop()
        }
    }

    /**
     * 开始播放
     */
    fun start() {
        if (mState == State.PAUSED) {
            resume()
        }
    }

    fun stop() {
        mMediaPlayer?.let {
            it.reset()
            it.setOnSeekCompleteListener(null)
            it.stop()
            it.release()
        }
        setState(State.IDLE)
        if (mCount < RetryCount) {
            mCount++
            load()
        } else {
            mVideoView.showPauseView()
        }
    }

    fun pause() {
        Log.i(TAG, "pause,state=$mState")
        if (mState != State.PLAYING) {
            Log.e(TAG, "pause error,state=$mState")
            return
        }
        setState(State.PAUSED)
        if (isPlaying()) {
            mMediaPlayer?.pause()
            if (!mCanPlay) {
                mMediaPlayer?.seekTo(0)
            }
        }
        mVideoView.showPauseView()
    }


    /**
     * 静音
     */
    fun mute(mute: Boolean) {
        isMute = mute
        if (isMute) {
            mMediaPlayer?.setVolume(0f, 0f)
        } else {
            mMediaPlayer?.setVolume(1f, 1f)
        }
    }

    fun isPlaying(): Boolean {
        mMediaPlayer?.let {
            return it.isPlaying
        }
        return false
    }

    fun isPause(): Boolean {
        return mState == State.PAUSED
    }

    /**
     * 获取当前播放进度
     */
    fun getCurrentProgress(): Int {
        if (mState == State.PAUSED || mState == State.PLAYING) {
            mMediaPlayer?.let {
                return it.currentPosition
            }
        }
        return -1
    }

    /**
     * 播放总时长
     */
    fun getTotalDuration(): Int {
        if (mState == State.PAUSED || mState == State.PLAYING) {
            return mMediaPlayer?.duration!!
        }
        return -1
    }

    /**
     * 销毁
     */
    fun destroy() {
        mMediaPlayer?.let {
            it.setOnSeekCompleteListener(null)
            it.stop()
            it.release()
            mMediaPlayer = null
        }
        setState(State.IDLE)
        mVideoView.destroy()
    }

    fun resume() {
        Log.i(TAG, "resume,state=$mState")
        if (mState != State.PAUSED) {
            Log.e(TAG, "resume error,state=$mState")
            return
        }
        if (!isPlaying()) {
            mCanPlay = true
            setState(State.PLAYING)
            isRealPause = false
            isComplete = false

            mMediaPlayer?.setOnSeekCompleteListener(null)
            mMediaPlayer?.start()
            mHandler.sendEmptyMessage(MSG_UPDATE_TIME)
            Log.i(TAG, "resume,state=$mState,isPlay=${isPlaying()}")
        }
        mVideoView.showPlayView()

        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, TIME_DELAY_3_SEC)
    }

    /**
     * 设置播放状态
     */
    fun setState(state: State) {
        mState = state
    }

    /**
     * 获取状态
     */
    fun getState(): State {
        return mState
    }

    fun setUrl(url: String) {
        mUrl = url
    }

    fun getUrl(): String {
        return mUrl
    }

    fun addVideoPlayListener(listener: VideoPlayerListener) {
        mPlayerListener = listener
    }

    fun removeVideoPlayListener() {
        mPlayerListener = null
    }
}