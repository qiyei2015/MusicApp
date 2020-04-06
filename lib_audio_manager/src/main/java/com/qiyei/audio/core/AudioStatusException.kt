/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStatusException
 */
package com.qiyei.audio.core

data class AudioStatusException(val code: Int, val msg: String) : Exception(msg) {

    override fun toString(): String {
        return "AudioStatusException(code=$code, msg='$msg')"
    }
}