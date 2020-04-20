/**
 * @author Created by qiyei2015 on 2020/4/14.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStylusPagerAdapter
 */
package com.qiyei.audio.component.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.component.activity.MusicPlayerActivity
import com.qiyei.audio.model.AudioBean
import com.qiyei.image.ImageManager


class AudioStylusPagerAdapter(private val mContext: Context, var mQueues:MutableList<AudioBean>):RecyclerView.Adapter<AudioStylusPagerAdapter.AudioBeanHolder>() {


    companion object {
        const val TAG = "AudioStylusPagerAdapter"
        private const val ANIM_DURATION:Long = 3000
    }

    private val mAnimMap:SparseArray<ObjectAnimator> = SparseArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBeanHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.audio_recycler_item_audio_stylus,parent,false)
        return AudioBeanHolder(view)
    }

    override fun getItemCount(): Int {
        return mQueues.size
    }

    override fun onBindViewHolder(holder: AudioBeanHolder, position: Int) {
        val bean = mQueues[position]
        Log.i(MusicPlayerActivity.TAG,"position=$position onBindViewHolder albumPic=${bean?.albumPic}")
        ImageManager.getInstance().load(holder.mCircleView,bean.albumPic)

        if (mAnimMap[position] == null){
            val animator = ObjectAnimator.ofFloat(holder.mCircleView,View.ROTATION.name,0f,360f)
            animator.duration = ANIM_DURATION
            animator.interpolator = LinearInterpolator()
            animator.repeatCount = -1
            mAnimMap.put(position,animator)
        }
        if (AudioPlayerManager.getInstance().isPlay()){
            mAnimMap[position].start()
        } else {
            mAnimMap[position].pause()
        }

    }

    fun setData(queue:MutableList<AudioBean>){
        mQueues = queue
        notifyDataSetChanged()
    }

    /**
     * Holder
     */
    class AudioBeanHolder(val mItemView: View):RecyclerView.ViewHolder(mItemView){
        val mCircleView:ImageView = mItemView.findViewById(R.id.circle_view)
    }
}