/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioController
 */
package com.qiyei.audio.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.qiyei.audio.model.AudioBean
import kotlin.random.Random

@ExperimentalStdlibApi
@RequiresApi(Build.VERSION_CODES.O)
class AudioController(private val mContext: Context) {

    /**
     * 音频播放
     */
    private var mPlayer: AudioPlayer

    /**
     * 歌曲队列
     */
    private lateinit var mQueue: ArrayDeque<AudioBean>

    /**
     * 播放模式
     */
    private lateinit var mMode: PlayMode

    /**
     * 播放索引
     */
    private var mIndex: Int = 0;

    init {
        mPlayer = AudioPlayer(mContext)
        mQueue = ArrayDeque()
        mMode = PlayMode.REPEAT
    }

    /**
     * 添加到播放队列中
     */
    fun addQueue(bean: AudioBean) {
        mQueue.add(bean)
    }

    fun play() {

    }

    fun next() {

    }

    fun prev() {

    }

    fun pause() {

    }

    fun resume() {

    }

    /**
     * 获取下一首要播放的
     */
    fun getNextPlay(): AudioBean {
        when (mMode) {
            PlayMode.REPEAT -> {
                mIndex = mIndex
            }

            PlayMode.LOOP -> {
                mIndex++
                if (mIndex == mQueue.size) {
                    mIndex = 0
                }
            }

            PlayMode.RANDOM -> {
                mIndex = Random.nextInt(0, mQueue.size)
            }
        }
        return mQueue[mIndex]
    }

    /**
     * 获取上一首播放
     */
    fun getPrevPlay(): AudioBean {
        when (mMode) {
            PlayMode.REPEAT -> {
                mIndex = mIndex
            }

            PlayMode.LOOP -> {
                mIndex--
                if (mIndex == -1) {
                    mIndex = mQueue.size - 1
                }
            }

            PlayMode.RANDOM -> {
                mIndex = Random.nextInt(0, mQueue.size)
            }
        }
        return mQueue[mIndex]
    }
}