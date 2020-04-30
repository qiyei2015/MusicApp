/**
 * @author Created by qiyei2015 on 2020/4/25.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: VideoView
 */
package com.qiyei.video.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.SurfaceTexture
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.SeekBar
import com.qiyei.video.R
import com.qiyei.video.utils.VideoUtils
import kotlinx.android.synthetic.main.video_view_video_player.view.*

class VideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener,
    TextureView.SurfaceTextureListener {

    companion object {
        const val TAG = "VideoView"
        const val ASPECT_RATIO: Float = 9.0f / 16f
    }

    /**
     * 播放器渲染View
     */
    private lateinit var mTextureView: TextureView

    /**
     * 播放器Surface
     */
    private var mSurfaceTexture: SurfaceTexture? = null

    /**
     * 屏幕变化广播
     */
    private lateinit var mScreenReceiver: ScreenEventReceiver

    /**
     * 宽度
     */
    private var mWidth: Int = 0

    /**
     * 高度
     */
    private var mHeight: Int = 0

    /**
     * 小窗模式
     */
    private var isMiniWindow: Boolean = true

    /**
     * VideoPlayer
     */
    private lateinit var mVideoPlayer: VideoPlayer

    init {
        initView(context)
        initData()
        registerScreenReceiver(context)
        initSmallLayout()
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.video_view_video_player, this)
        mTextureView = findViewById(R.id.video_textureView)
        mTextureView.surfaceTextureListener = this
        mTextureView.keepScreenOn = true
        mTextureView.setOnClickListener(this)
        video_start_time_tv.text = VideoUtils.formatDurationTime(0)
        video_full_time_tv.text = VideoUtils.formatDurationTime(0)

        play_window_toggle_imv.setOnClickListener(this)
        video_player_play_btn.setOnClickListener(this)
        video_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged progress=$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mVideoPlayer?.seekTo(seekBar?.progress)
                }
            }
        })
    }

    private fun registerScreenReceiver(context: Context) {
        mScreenReceiver = ScreenEventReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        context.registerReceiver(mScreenReceiver, filter)
    }

    private fun initData() {
        val dm = DisplayMetrics()
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        mWidth = dm.widthPixels
        mHeight = (mWidth * ASPECT_RATIO).toInt()
    }

    private fun initSmallLayout() {
        val params = LayoutParams(mWidth, mHeight)
        params.gravity = Gravity.CENTER
        mTextureView.layoutParams = params

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.play_window_toggle_imv -> {
                isMiniWindow = !isMiniWindow
                play_window_toggle_imv.setImageResource(if (isMiniWindow) R.drawable.icon_video_player_mini else R.drawable.icon_video_player_full)
                if (isMiniWindow){
                    mVideoPlayer.showFullDialogPlay()
                } else {
                    mVideoPlayer.closeFullDialogPlay()
                }
            }
            R.id.video_player_play_btn -> {
                mVideoPlayer.start()
            }
            R.id.video_textureView -> {
                mVideoPlayer.pause()
            }
        }
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
        Log.i(TAG, "onSurfaceTextureAvailable width=$width,height=$height")
        mSurfaceTexture = surface

        mVideoPlayer.initMediaPlayer()
        mVideoPlayer.setSurface(Surface(mSurfaceTexture))
        //触发load加载视频播放
        mVideoPlayer.load()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        Log.i(TAG, "onVisibilityChanged")
        if (visibility == View.VISIBLE && mVideoPlayer.isPause()) {
            if (mVideoPlayer.isRealPause || mVideoPlayer.isComplete) {
                mVideoPlayer.pause()
            } else {
                mVideoPlayer.resume()
            }
        } else {
            mVideoPlayer.pause()
        }
    }

    fun destroy() {
        context.unregisterReceiver(mScreenReceiver)
    }

    fun setVideoPlayer(player: VideoPlayer) {
        mVideoPlayer = player
    }

    fun showLoadingView() {
        play_window_toggle_imv.visibility = View.VISIBLE
        video_player_play_btn.visibility = View.GONE
        video_player_loading_bar.visibility = View.VISIBLE
        val anim = video_player_loading_bar.background as AnimationDrawable
        anim.start()
    }

    fun showPauseView() {
        play_window_toggle_imv.visibility = View.VISIBLE
        video_player_play_btn.visibility = View.VISIBLE
        video_player_loading_bar.clearAnimation()
        video_player_loading_bar.visibility = View.GONE
        video_seek_bar_layout.visibility = View.VISIBLE
    }

    fun showPlayView() {
        video_player_loading_bar.clearAnimation()
        video_player_loading_bar.visibility = View.GONE
        play_window_toggle_imv.visibility = View.VISIBLE
        video_player_play_btn.visibility = View.GONE
        video_seek_bar_layout.visibility = View.VISIBLE
        video_seek_bar.max = mVideoPlayer.getTotalDuration()
        video_full_time_tv.text = VideoUtils.formatDurationTime(mVideoPlayer.getTotalDuration())
    }

    fun updateSeekBar(progress: Int, duration: String) {
        video_seek_bar.progress = progress
        video_start_time_tv.text = duration
    }

    fun hideSeekBar() {
        video_seek_bar_layout.visibility = View.GONE
        play_window_toggle_imv.visibility = View.GONE
    }

    private inner class ScreenEventReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    mVideoPlayer.pause()
                }

                Intent.ACTION_SCREEN_ON, Intent.ACTION_USER_PRESENT -> {
                    if (mVideoPlayer.isComplete || mVideoPlayer.isRealPause) {
                        mVideoPlayer.pause()
                    } else {
                        mVideoPlayer.resume()
                    }
                }
            }
        }
    }
}