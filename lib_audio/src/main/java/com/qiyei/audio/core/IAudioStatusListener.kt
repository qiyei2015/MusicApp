/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStatusListener
 */
package com.qiyei.audio.core

import com.qiyei.audio.exception.AudioStatusException
import com.qiyei.audio.model.AudioBean


interface IAudioStatusListener {

    /**
     * 音频已加载
     */
    fun onAudioLoaded(bean: AudioBean?)

    /**
     * 音频播放已经开始
     */
    fun onAudioStarted(bean: AudioBean?)

    /**
     * 播放时的进度
     */
    fun onAudioProgress(status: Status, progress: Int, maxLength: Int)

    /**
     * 音频完成
     */
    fun onAudioCompleted(bean: AudioBean?)

    /**
     * 音频暂停
     */
    fun onAudioPause(bean: AudioBean?)

    /**
     * 音频释放
     */
    fun onAudioReleased(bean: AudioBean?)

    /**
     * 错误回调
     */
    fun onError(exception: AudioStatusException)

}