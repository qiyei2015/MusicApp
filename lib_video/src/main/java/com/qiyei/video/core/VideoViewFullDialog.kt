/**
 * @author Created by qiyei2015 on 2020/4/30.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoViewFullDialog
 */
package com.qiyei.video.core

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.qiyei.video.R

class VideoViewFullDialog(
    context: Context,
    styleTheme: Int = 0
) : AppCompatDialog(context, styleTheme) {


    private lateinit var mVideoView:VideoView

    private lateinit var mViewPlayer: VideoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_dialog_full_video_player)
        //全透明，要不然有padding
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

        mVideoView = findViewById(R.id.video_view_full)!!
        mViewPlayer.attachVideoView(mVideoView)
    }

    fun setVideoPlayer(player: VideoPlayer){
        mViewPlayer = player
    }


    override fun show() {
        super.show()

    }

    override fun dismiss() {
        super.dismiss()

    }
}