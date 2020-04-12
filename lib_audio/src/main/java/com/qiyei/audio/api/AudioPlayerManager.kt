/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioManager
 */
package com.qiyei.audio.api

import android.content.Context
import com.qiyei.audio.core.PlayMode
import com.qiyei.audio.model.AudioBean


class AudioPlayerManager private constructor(private val context: Context){

    /**
     * 注意单例写法,DCL 单例，加参数
     */
    companion object {
        @Volatile
        private var instance: AudioPlayerManager? = null
        fun getInstance(context: Context): AudioPlayerManager {
            return instance ?: synchronized(this) {
                instance ?: AudioPlayerManager(context.applicationContext).also { instance = it }
            }
        }
    }

    init {

    }

    fun addQueue(beans:List<AudioBean>){

    }

    /**
     * 添加到播放队列中
     */
    fun addQueue(bean: AudioBean) {

    }

    /**
     * 删除队列中的歌曲
     */
    fun removeQueue(index:Int) {

    }

    /**
     * 清空播放列表
     */
    fun clearQueue(){

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
    fun setPlayMode(mode: PlayMode){

    }

    /**
     * 播放
     */
    fun play() {

    }

    /**
     * 下一首
     */
    fun next(){

    }

    /**
     * 上一首
     */
    fun prev() {

    }

    /**
     * 暂停
     */
    fun pause() {

    }

    /**
     * 重新播放
     */
    fun resume() {

    }

//    /**
//     * 获取下一首要播放的
//     */
//    fun getNextPlay(): AudioBean {
//        return
//    }
//
//    /**
//     * 获取上一首播放
//     */
//    fun getPrevPlay(): AudioBean {
//
//    }


    /**
     * 添加收藏
     */
    fun addFavourite(bean: AudioBean){

    }

    /**
     * 移除收藏
     */
    fun removeFavourite(bean: AudioBean){

    }

}