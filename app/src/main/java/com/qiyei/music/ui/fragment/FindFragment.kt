package com.qiyei.music.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.qiyei.image.GlideImpl
import com.qiyei.image.ImageManager
import com.qiyei.music.MusicApplication
import com.qiyei.music.R
import kotlinx.android.synthetic.main.fragment_find.*

/**
 * @author Created by qiyei2015 on 2020/3/29.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
class FindFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find,container,false)
    }


    override fun onResume() {
        super.onResume()
        Log.i("TAG","onViewCreated")
        val url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585679717920&di=dfbdbfeccf57617cc1dc07381e2d95ca&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170122%2F20170122145324_c074bd4d20c537b795f6cc97f90d9e50_2.jpeg"
        val url2 = "https://t1.hddhhn.com/uploads/tu/201910/222/1.jpg"
        val url3 = "https://t1.hddhhn.com/uploads/tu/201910/204/1.jpg"
        ImageManager.getInstance().load(test_imv,url3)
    }

}