/**
 * @author Created by qiyei2015 on 2020/4/7.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: MusicBottomView
 */
package com.qiyei.audio.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.qiyei.audio.R
import com.qiyei.audio.model.AudioBean
import com.qiyei.audio.ui.activity.MusicPlayerActivity
import com.qiyei.audio.ui.dialog.AudioListDialog

class MusicBottomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr),View.OnClickListener{

    companion object {
        const val TAG = "MusicBottomView"
        private const val ANIM_DURATION:Long = 3000
    }

    private var mAlbumView:ImageView
    private var mMusicNameTv:TextView
    private var mAlbumNameTv:TextView
    private var mMusicPlayBtn:ImageView
    private var mMusicListBtn:ImageView
    private var mAlbumRotateAnimation:ObjectAnimator
    private var isPlay:Boolean = false
    private var mCurrentBean: AudioBean? = null

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.view_music_bottom,this)
        mAlbumView = findViewById(R.id.album_imageView)
        mMusicNameTv = findViewById(R.id.music_name_tv)
        mAlbumNameTv = findViewById(R.id.album_name_tv)
        mMusicPlayBtn = findViewById(R.id.music_play_btn_imageView)
        mMusicListBtn = findViewById(R.id.music_list_btn_imageView)

        mAlbumView.setOnClickListener(this)
        mMusicNameTv.setOnClickListener(this)
        mAlbumNameTv.setOnClickListener(this)
        mMusicPlayBtn.setOnClickListener(this)
        mMusicListBtn.setOnClickListener(this)

        mAlbumRotateAnimation = ObjectAnimator.ofFloat(mAlbumView, View.ROTATION.name,0f,360f)
        mAlbumRotateAnimation.duration = ANIM_DURATION
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

    override fun onClick(v: View?) {
        when (v?.id) {
           R.id.music_play_btn_imageView -> toggleMusicPlay()
           R.id.music_list_btn_imageView -> showMusicList()
           R.id.album_imageView,R.id.album_name_tv,R.id.music_name_tv -> gotoMusicPlayerActivity()
        }
    }

    /**
     * 开始播放
     */
    fun startPlay(bean: AudioBean?){
        isPlay = true
        mMusicPlayBtn.setImageDrawable(resources.getDrawable(R.mipmap.icon_btn_audio_pause_white,null))
//        mAlbumView.setImageBitmap(bean?.albumBitmap)
//        mMusicNameTv.text = bean?.audioName
//        mAlbumNameTv.text = bean?.albumName
        mAlbumRotateAnimation.start()
    }

    /**
     * 暂停播放
     */
    fun stopPlay(){
        isPlay = false
        mMusicPlayBtn.setImageDrawable(resources.getDrawable(R.mipmap.icon_btn_audio_play_white,null))
        mAlbumRotateAnimation.pause()
    }

//    /**
//     * 点击事件
//     */
//    interface onClickListener {
//
//        /**
//         * 播放按钮点击
//         */
//        fun onPlayClick(play: Boolean)
//
//        /**
//         * 列表按钮
//         */
//        fun onMusicList()
//    }

    private fun toggleMusicPlay() {
        if (isPlay){
            stopPlay()
        } else {
            startPlay(mCurrentBean)
        }
    }

    private fun showMusicList() {
        val dialog = AudioListDialog(context)
        dialog.show()
    }

    private fun gotoMusicPlayerActivity(){
        context.startActivity(Intent(context, MusicPlayerActivity::class.java))
    }
}