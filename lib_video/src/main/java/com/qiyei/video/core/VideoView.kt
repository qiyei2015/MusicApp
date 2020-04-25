/**
 * @author Created by qiyei2015 on 2020/4/25.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoView
 */
package com.qiyei.video.core

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.RelativeLayout
import com.qiyei.video.R

class VideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    TextureView.SurfaceTextureListener {

    /**
     * 播放器状态
     */
    private var mStatus: Status = Status.IDLE

    /**
     * 真实的播放器
     */
    private val mPlayer: MediaPlayer = MediaPlayer()

    private lateinit var mTextureView: TextureView
    private var mSurfaceTexture:SurfaceTexture? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.video_view_video_player, this)
        mTextureView = findViewById(R.id.video_textureView)
        mTextureView.surfaceTextureListener = this

        val audioAttributes =
            AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build()
        mPlayer.setAudioAttributes(audioAttributes)
        mPlayer.setOnPreparedListener(this)
        mPlayer.setOnBufferingUpdateListener(this)
        mPlayer.setOnCompletionListener(this)
        mPlayer.setOnErrorListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.full_play_imv -> {

            }
            R.id.video_player_play_btn -> {

            }
            R.id.video_textureView -> {

            }
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {

    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mSurfaceTexture = null
        return false
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        mSurfaceTexture = surface
    }
}