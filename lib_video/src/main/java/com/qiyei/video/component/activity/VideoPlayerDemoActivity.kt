package com.qiyei.video.component.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qiyei.video.R
import com.qiyei.video.core.VideoPlayer
import com.qiyei.video.core.VideoView

class VideoPlayerDemoActivity : AppCompatActivity() {

    private lateinit var mVideoView:VideoView

    private lateinit var mViewPlayer: VideoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player_demo)

        mVideoView = findViewById(R.id.video_view)

        mViewPlayer = VideoPlayer(this)
        mViewPlayer.setUrl("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4")
        mViewPlayer.attachVideoView(mVideoView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewPlayer.destroy()
    }
}
