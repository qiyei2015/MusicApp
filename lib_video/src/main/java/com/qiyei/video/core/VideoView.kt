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
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import com.qiyei.video.R
import com.qiyei.video.api.VideoPlayerListener
import kotlinx.android.synthetic.main.video_view_video_player.view.*

class VideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    TextureView.SurfaceTextureListener {

    companion object {
        const val TAG = "VideoView"
        const val ASPECT_RATIO: Float = 9.0f / 16f
        const val RetryCount = 3
    }

    /**
     * 播放器监听器
     */
    var mPlayerListener: VideoPlayerListener? = null

    /**
     * 播放的url
     */
    private lateinit var mUrl:String

    /**
     * 是否静音
     */
    private var isMute:Boolean = false
    /**
     * 播放器状态
     */
    private var mState: State = State.IDLE

    /**
     * 真实的播放器
     */
    private var mMediaPlayer: MediaPlayer? = null

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
     * 重试次数
     */
    private var mCount:Int = 0

    private var mCanPlay:Boolean = false

    var isRealPause:Boolean = false

    var isComplete:Boolean = false

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

        full_play_imv.setOnClickListener(this)
        video_player_play_btn.setOnClickListener(this)
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
        val params = LayoutParams(mWidth,mHeight)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        mTextureView.layoutParams = params

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
        Log.i(TAG,"onPrepared")
        showPlayView()
        mMediaPlayer = mp
        mMediaPlayer?.let {
            mCount = 0
            setState(State.PAUSED)
            resume()
        }
        mPlayerListener?.onPrepared()
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        mPlayerListener?.onBufferingUpdate(percent)
        Log.i(TAG,"onBufferingUpdate state=$mState")
        showPlayView()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mPlayerListener?.onCompletion()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mPlayerListener?.onError(what, extra)
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
        Log.i(TAG,"onSurfaceTextureAvailable state=$mState")
        mSurfaceTexture = surface
        initPlayer()
        mMediaPlayer?.setSurface(Surface(mSurfaceTexture))

        //触发load加载视频播放
        load()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        Log.i(TAG,"onVisibilityChanged")
        if (visibility == View.VISIBLE && mState == State.PAUSED){
            if (isRealPause || isComplete){
                pause()
            } else {
                resume()
            }
        } else {
            pause()
        }
    }

    fun setUrl(url:String){
        mUrl = url
    }

    fun getUrl():String {
        return mUrl
    }

    /**
     * 加载后播放
     */
    fun load(){
        Log.i(TAG,"load,state=$mState")
        if (mState != State.IDLE){
            Log.i(TAG,"load error state = ${mState}")
            return
        }
        showLoadingView()
        setState(State.IDLE)
        try {
            initPlayer()
            mute(isMute)
            mMediaPlayer?.setDataSource(mUrl)
            mMediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.i(TAG,"load Exception = ${e.message}")
            //重试后退出
            stop()
        }
    }

    fun start(){

    }

    fun stop(){
        mMediaPlayer?.let {
            it.reset()
            it.setOnSeekCompleteListener(null)
            it.stop()
            it.release()
        }
        setState(State.IDLE)
        if (mCount < RetryCount) {
            mCount++
            load()
        } else {
            showPauseView(false)
        }
    }

    fun pause(){
        Log.i(TAG,"pause,state=$mState")
        if (mState != State.PLAYING) {
            Log.e(TAG,"pause error,state=$mState")
            return
        }
        setState(State.PAUSED)
        if (isPlaying()){
            mMediaPlayer?.pause()
            if (!mCanPlay){
                mMediaPlayer?.seekTo(0)
            }
        }
        showPauseView(false)
    }

    fun resume(){
        Log.i(TAG,"resume,state=$mState")
        if (mState != State.PAUSED){
            Log.e(TAG,"resume error,state=$mState")
            return
        }
        if (!isPlaying()){
            entryResumeState()
            mMediaPlayer?.setOnSeekCompleteListener(null)
            mMediaPlayer?.start()
            Log.i(TAG,"resume,state=$mState,isPlay=${isPlaying()}")
            showPauseView(true)
        } else {
            showPauseView(false)
        }
    }


    /**
     * 设置播放状态
     */
    fun setState(state: State){
        mState = state
    }

    /**
     * 静音
     */
    fun mute(mute: Boolean){
        isMute = mute
        if (isMute) {
            mMediaPlayer?.setVolume(0f,0f)
        } else {
            mMediaPlayer?.setVolume(1f,1f)
        }
    }

    fun isPlaying():Boolean {
        mMediaPlayer?.let {
            return it.isPlaying
        }
        return false
    }

    /**
     * 获取播放进度
     */
    fun getCurrentPosition():Int{
        mMediaPlayer?.let {
            return it.currentPosition
        }
        return 0
    }

    /**
     * 销毁
     */
    fun destroy(){
        mMediaPlayer?.let {
            it.setOnSeekCompleteListener(null)
            it.stop()
            it.release()
            mMediaPlayer = null
        }
        setState(State.IDLE)
        context.unregisterReceiver(mScreenReceiver)
    }

    @Synchronized
    private fun initPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
            val audioAttributes =
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build()
            mMediaPlayer?.setAudioAttributes(audioAttributes)
            mMediaPlayer?.setOnPreparedListener(this)
            mMediaPlayer?.setOnBufferingUpdateListener(this)
            mMediaPlayer?.setOnCompletionListener(this)
            mMediaPlayer?.setOnErrorListener(this)
        }
    }

    private fun showLoadingView() {
        full_play_imv.visibility = View.GONE
        video_player_play_btn.visibility = View.GONE
        video_player_loading_bar.visibility = View.VISIBLE
        val anim = video_player_loading_bar.background as AnimationDrawable
        anim.start()
    }

    private fun showPauseView(show:Boolean){
        full_play_imv.visibility = if (show) View.VISIBLE else View.GONE
        video_player_play_btn.visibility = if (show) View.VISIBLE else View.GONE
        video_player_loading_bar.clearAnimation()
        video_player_loading_bar.visibility = View.GONE
    }

    private fun showPlayView(){
        video_player_loading_bar.clearAnimation()
        video_player_loading_bar.visibility = View.GONE
        full_play_imv.visibility = View.GONE
        video_player_play_btn.visibility = View.GONE
    }

    private fun entryResumeState() {
        mCanPlay = true
        setState(State.PLAYING)
        isRealPause = false
        isComplete = false
    }

    private class ScreenEventReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> {

                }

                Intent.ACTION_SCREEN_ON -> {

                }

                Intent.ACTION_USER_PRESENT -> {

                }
            }
        }
    }
}