/**
 * @author Created by qiyei2015 on 2020/4/14.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStylusView 音乐播放唱针View
 */
package com.qiyei.audio.component.view

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.component.adapter.AudioStylusPagerAdapter
import com.qiyei.audio.core.IAudioStatusListener
import com.qiyei.audio.core.Status
import com.qiyei.audio.exception.AudioStatusException
import com.qiyei.audio.model.AudioBean

class AudioStylusView @JvmOverloads constructor(
    private val mContext: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext,attributeSet,defStyleAttr) {


    companion object {
        const val TAG = "AudioStylusView"
    }

    private lateinit var mViewPager:ViewPager2
    private lateinit var mQueues:MutableList<AudioBean>

    init {
        LayoutInflater.from(mContext).inflate(R.layout.audio_view_audio_stylus,this)
        mViewPager = findViewById(R.id.audio_stylus_viewpager)
        initData()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        AudioPlayerManager.getInstance().removeAudioStatusListener("AudioStylusView")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mViewPager.adapter = AudioStylusPagerAdapter(mContext, mQueues)
    }

    private fun initData() {
        AudioPlayerManager.getInstance().addAudioStatusListener("AudioStylusView",object :
            IAudioStatusListener {
            override fun onAudioLoaded(bean: AudioBean?) {
                Log.i(TAG,"onAudioLoaded")
            }

            override fun onAudioStarted(bean: AudioBean?) {
                mViewPager.adapter?.notifyDataSetChanged()
                Log.i(TAG,"onAudioStarted")
            }

            override fun onAudioProgress(status: Status, progress: Int, maxLength: Int) {

            }

            override fun onAudioCompleted(bean: AudioBean?) {

            }

            override fun onAudioPause(bean: AudioBean?) {
                mViewPager.adapter?.notifyDataSetChanged()
                Log.i(TAG,"onAudioPause")
            }

            override fun onAudioReleased(bean: AudioBean?) {

            }

            override fun onError(exception: AudioStatusException) {
                Log.i(TAG,"onError exception=$exception")
            }
        })
        val bean = AudioBean("听妈妈的话","七里香","","")
        mQueues = mutableListOf()
        mQueues.add(bean)
        AudioPlayerManager.getInstance().addQueue(mQueues)
    }
}