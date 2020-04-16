/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
package com.qiyei.audio.model

import android.os.Parcel
import android.os.Parcelable

class AudioBean(
    var audioName: String? = null,
    var albumName: String? = null,
    var albumInfo: String? = null,
    var author: String? = null,
    var uri: String? = null,
    var albumPic: String? = null,
    var totalTime: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(audioName)
        parcel.writeString(albumName)
        parcel.writeString(albumInfo)
        parcel.writeString(author)
        parcel.writeString(uri)
        parcel.writeString(albumPic)
        parcel.writeString(totalTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioBean> {
        override fun createFromParcel(parcel: Parcel): AudioBean {
            return AudioBean(parcel)
        }

        override fun newArray(size: Int): Array<AudioBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "AudioBean(audioName=$audioName, albumName=$albumName, albumInfo=$albumInfo, author=$author, uri=$uri, albumPic=$albumPic, totalTime=$totalTime)"
    }
}