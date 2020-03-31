package com.qiyei.common.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qiyei.common.ui.utils.StatusBarUtils

/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 基础父类Activity
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtils.statusBarLightMode(this)
    }

    open protected fun initView():Unit{

    }

    open protected fun initData():Unit{

    }
}
