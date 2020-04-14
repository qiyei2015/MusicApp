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
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import com.qiyei.audio.R
import com.qiyei.audio.component.adapter.AudioStylusPagerAdapter
import com.qiyei.audio.model.AudioBean

class AudioStylusView @JvmOverloads constructor(
    private val mContext: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext,attributeSet,defStyleAttr) {

    private lateinit var mViewPager:ViewPager2
    private lateinit var mQueues:MutableList<AudioBean>

    init {
        LayoutInflater.from(mContext).inflate(R.layout.audio_view_audio_stylus,this)
        mViewPager = findViewById(R.id.audio_stylus_viewpager)
        initData()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mViewPager.adapter = AudioStylusPagerAdapter(mContext, mQueues)
    }

    private fun initData() {
        val bean = AudioBean("听妈妈的话","七里香","",BitmapFactory.decodeResource(resources,R.mipmap.album_test))
        mQueues = mutableListOf()
        mQueues.add(bean)
    }
}