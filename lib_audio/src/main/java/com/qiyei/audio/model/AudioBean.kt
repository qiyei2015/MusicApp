/**
 * @author Created by qiyei2015 on 2020/4/6.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
package com.qiyei.audio.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class AudioBean(
    var audioName: String?,
    var albumName: String?,
    var uri: String?,
    var albumBitmap: Bitmap?
) :Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(audioName)
        parcel.writeString(albumName)
        parcel.writeString(uri)
        parcel.writeParcelable(albumBitmap, flags)
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


}