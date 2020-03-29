package com.qiyei.music.ui.activity


import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.qiyei.common.ui.activity.BaseActivity
import com.qiyei.music.R
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 首页
 */
class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }


    override protected fun initView():Unit{

    }
}
