/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: MusicMediaPlayer
 */
package com.qiyei.audio.core

import android.media.MediaPlayer

class AudioMediaPlayer : MediaPlayer(), MediaPlayer.OnCompletionListener {


    companion object {
        const val TAG = "MusicMediaPlayer"
    }

    var mStatus: Status
    private var mOnCompletionListener: OnCompletionListener? = null

    init {
        mStatus = Status.IDLE
        setOnCompletionListener(this)
    }

    override fun start() {
        super.start()
        mStatus = Status.STARTED
    }

    override fun stop() {
        super.stop()
        mStatus = Status.STOPPED
    }

    override fun pause() {
        super.pause()
        mStatus = Status.PAUSED
    }

    override fun reset() {
        super.reset()
        mStatus = Status.IDLE
    }

    override fun setDataSource(path: String?) {
        super.setDataSource(path)
        mStatus = Status.INITIALIZED
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mStatus = Status.COMPLETED
        mOnCompletionListener?.onCompletion(mp)
    }

    override fun setOnCompletionListener(listener: OnCompletionListener?) {
        mOnCompletionListener = listener
    }
}