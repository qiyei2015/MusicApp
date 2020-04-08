/**
 * @author Created by qiyei2015 on 2020/4/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: MusicBottomView
 */
package com.qiyei.audio.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.qiyei.audio.R
import com.qiyei.audio.model.AudioBean

class MusicBottomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "MusicBottomView"
    }

    private var mAlbumView:ImageView
    private var mMusicNameTv:TextView
    private var mAlbumNameTv:TextView
    private var mMusicPlayBtn:ImageView
    private var mMusicListBtn:ImageView

    private var mAlbumRotateAnimation:ObjectAnimator

    private var isPlay:Boolean = false

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.view_music_bottom,this)
        mAlbumView = findViewById(R.id.album_imageView)
        mMusicNameTv = findViewById(R.id.music_name_tv)
        mAlbumNameTv = findViewById(R.id.album_name_tv)
        mMusicPlayBtn = findViewById(R.id.music_play_btn_imageView)
        mMusicListBtn = findViewById(R.id.music_list_btn_imageView)

        mAlbumRotateAnimation = ObjectAnimator.ofFloat(mAlbumView, View.ROTATION.name,0f,360f)
        mAlbumRotateAnimation.duration = 1000
        mAlbumRotateAnimation.interpolator = LinearInterpolator()
        mAlbumRotateAnimation.repeatCount = -1
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isPlay){
            mAlbumRotateAnimation.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAlbumRotateAnimation.cancel()
    }

    /**
     * 开始播放
     */
    fun startPlay(bean: AudioBean){
        isPlay = true
        mAlbumView.setImageBitmap(bean.albumBitmap)
        mMusicNameTv.text = bean.audioName
        mAlbumNameTv.text = bean.albumName

        mAlbumRotateAnimation.start()
    }

    /**
     * 暂停播放
     */
    fun stopPlay(){
        isPlay = false
        mAlbumRotateAnimation.pause()
    }


}