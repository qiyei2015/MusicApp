/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioManager
 */
package com.qiyei.audio.api

import android.content.Context
import com.qiyei.audio.core.AudioController
import com.qiyei.audio.core.IAudioStatusListener
import com.qiyei.audio.core.PlayMode
import com.qiyei.audio.favorite.FavouriteManager
import com.qiyei.audio.model.AudioBean


class AudioPlayerManager private constructor(val mContext: Context) {

    /**
     * 注意单例写法,DCL 单例，加参数
     */
    companion object {
        @Volatile
        private var instance: AudioPlayerManager? = null

        lateinit var mContext: Context

        /**
         * 初始化
         */
        fun init(context: Context) {
            mContext = context.applicationContext
            instance = AudioPlayerManager(context)
        }

//        fun getInstance(): AudioPlayerManager {
//            return instance ?: synchronized(this) {
//                instance ?: AudioPlayerManager(mContext.applicationContext).also { instance = it }
//            }
//        }

        fun getInstance(): AudioPlayerManager {
            return instance!!
        }
    }

    private val mStatusListenerMap: MutableMap<String, IAudioStatusListener> = mutableMapOf()

    /**
     * 音乐播放控制器
     */
    private var mAudioController: AudioController? = null

    /**
     * 收藏管理
     */
    private var mFavouriteManager: FavouriteManager? = null


    init {
        mAudioController = AudioController(mContext)
        mFavouriteManager = FavouriteManager()
    }

    /**
     * 添加到播放队列中
     */
    fun addQueue(beans: List<AudioBean>) {
        mAudioController?.addQueue(beans)
    }

    /**
     * 添加到播放队列中
     */
    fun addQueue(bean: AudioBean) {
        mAudioController?.addQueue(bean)
    }

    /**
     * 删除队列中的歌曲
     */
    fun removeQueue(index: Int) {
        mAudioController?.removeQueue(index)
    }

    /**
     * 清空播放列表
     */
    fun clearQueue() {
        mAudioController?.clearQueue()
    }

    /**
     * 获取播放队列
     */
    fun getQueue(): MutableList<AudioBean> {
        mAudioController?.let {
            return it.getQueue()
        }
        return mutableListOf()
    }

    /**
     * 获取播放模式
     */
    fun getPlayMode(): PlayMode {
        return PlayMode.RANDOM
    }

    /**
     * 设置播放模式
     */
    fun setPlayMode(mode: PlayMode) {
        mAudioController?.setPlayMode(mode)
    }

    /**
     * 播放
     */
    fun play() {
        mAudioController?.play()
    }

    /**
     * 是否处于播放状态
     */
    fun isPlay(): Boolean {
        mAudioController?.let {
            return it.isPlay()
        }
        return false
    }

    /**
     * 下一首
     */
    fun playNext() {
        mAudioController?.next()
    }

    /**
     * 上一首
     */
    fun playPrev() {
        mAudioController?.prev()
    }

    /**
     * 暂停
     */
    fun pause() {
        mAudioController?.pause()
    }

    /**
     * 重新播放
     */
    fun resume() {
        mAudioController?.resume()
    }

    /**
     * 获取下一首要播放的
     */
    fun getNextPlay(): AudioBean? {
        return mAudioController?.getNextPlay()
    }

    /**
     * 获取上一首播放
     */
    fun getPrevPlay(): AudioBean? {
        return mAudioController?.getNextPlay()
    }

    fun getCurrentPlay(): AudioBean? {
        return mAudioController?.getCurrentPlay()
    }

    fun addAudioStatusListener(name: String, listener: IAudioStatusListener) {
        mStatusListenerMap[name] = listener
        mAudioController?.addAudioStatusListener(listener)
    }

    fun removeAudioStatusListener(listener: IAudioStatusListener) {
        mAudioController?.removeAudioStatusListener(listener)
    }

    fun removeAudioStatusListener(name: String) {
        mStatusListenerMap[name]?.let {
            mAudioController?.removeAudioStatusListener(it)
        }
    }

    /**
     * 添加收藏
     */
    fun addFavourite(bean: AudioBean) {
        mFavouriteManager?.addFavourite(bean)
    }

    /**
     * 移除收藏
     */
    fun removeFavourite(bean: AudioBean) {
        mFavouriteManager?.removeFavourite(bean)
    }


}