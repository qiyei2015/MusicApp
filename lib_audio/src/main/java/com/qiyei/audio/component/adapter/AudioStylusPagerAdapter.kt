/**
 * @author Created by qiyei2015 on 2020/4/14.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioStylusPagerAdapter
 */
package com.qiyei.audio.component.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.qiyei.audio.R
import com.qiyei.audio.model.AudioBean


class AudioStylusPagerAdapter(val mContext: Context,var mQueues:MutableList<AudioBean>):RecyclerView.Adapter<AudioStylusPagerAdapter.AudioBeanHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBeanHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.audio_recycler_item_audio_stylus,parent,false)
        return AudioBeanHolder(view)
    }

    override fun getItemCount(): Int {
        return mQueues.size
    }

    override fun onBindViewHolder(holder: AudioBeanHolder, position: Int) {
        val bean = mQueues[position]
        holder.mCircleView.setImageBitmap(bean.albumBitmap)
    }

    /**
     * Holder
     */
    class AudioBeanHolder(val mItemView: View):RecyclerView.ViewHolder(mItemView){
        val mCircleView:ImageView = mItemView.findViewById(R.id.circle_view)
    }
}