/**
 * @author Created by qiyei2015 on 2020/4/14.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStylusView 音乐播放唱针View
 */
package com.qiyei.audio.component.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.component.activity.MusicPlayerActivity
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
    private lateinit var mPagerAdapter:AudioStylusPagerAdapter

    init {
        LayoutInflater.from(mContext).inflate(R.layout.audio_view_audio_stylus,this)
        mViewPager = findViewById(R.id.audio_stylus_viewpager)

        mQueues = mutableListOf()
        mPagerAdapter = AudioStylusPagerAdapter(mContext, mQueues)
        mViewPager.adapter = mPagerAdapter
        mViewPager.currentItem = 0

        AudioPlayerManager.getInstance().addAudioStatusListener("AudioStylusView",object :
            IAudioStatusListener {
            override fun onAudioLoaded(bean: AudioBean?) {
                Log.i(MusicPlayerActivity.TAG,"onAudioLoaded")
                mPagerAdapter.notifyDataSetChanged()
            }

            override fun onAudioStarted(bean: AudioBean?) {
                Log.i(MusicPlayerActivity.TAG,"onAudioStarted")
                mPagerAdapter.notifyDataSetChanged()
            }

            override fun onAudioProgress(status: Status, progress: Int, maxLength: Int) {

            }

            override fun onAudioCompleted(bean: AudioBean?) {

            }

            override fun onAudioPause(bean: AudioBean?) {
                Log.i(MusicPlayerActivity.TAG,"onAudioPause")
                mPagerAdapter.notifyDataSetChanged()
            }

            override fun onAudioReleased(bean: AudioBean?) {

            }

            override fun onError(exception: AudioStatusException) {
                Log.i(MusicPlayerActivity.TAG,"onError exception=$exception")
            }
        })
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        AudioPlayerManager.getInstance().removeAudioStatusListener("AudioStylusView")
    }

    fun setQueue(queue:MutableList<AudioBean>){
        mQueues = queue
        mPagerAdapter.setData(mQueues)
    }
}