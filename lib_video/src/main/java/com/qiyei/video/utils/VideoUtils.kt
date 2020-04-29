/**
 * @author Created by qiyei2015 on 2020/4/29.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoUtils
 */
package com.qiyei.video.utils

import android.util.Log


object VideoUtils {

    const val TAG = "VideoUtils"

    /**
     * @param duration
     * @return HH:MM:ss
     */
    fun formatDurationTime(duration: Int):String {
        val builder = StringBuilder()
        Log.i(TAG,"formatDurationTime duration=$duration")
        var time = duration / 1000

        val sec = time % 60
        time /= 60
        val minute = time % 60
        time /= 60
        val hour = time
        return String.format("%02d:%02d:%02d",hour,minute,sec)
    }

}