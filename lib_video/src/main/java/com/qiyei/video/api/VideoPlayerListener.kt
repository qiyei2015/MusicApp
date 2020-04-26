/**
 * @author Created by qiyei2015 on 2020/4/25.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoPlayerListener
 */
package com.qiyei.video.api


interface VideoPlayerListener {

    fun onPrepared()

    fun onBufferingUpdate(percent: Int)

    fun onCompletion()

    fun onError(what: Int, extra: Int)
}