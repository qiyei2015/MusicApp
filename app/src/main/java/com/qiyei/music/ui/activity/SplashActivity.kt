package com.qiyei.music.ui.activity

import android.content.Intent
import android.os.Bundle
import com.qiyei.common.ui.activity.BaseActivity
import com.qiyei.music.R
import java.util.*


/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 启动闪屏页
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val timer = Timer()
        timer.schedule(object:TimerTask(){
            override fun run() {
                startActivity(Intent(this@SplashActivity,HomeActivity::class.java))
            }
        },1000)
    }
}
