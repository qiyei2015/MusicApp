/**
 * @author Created by qiyei2015 on 2020/4/22.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: ShareManager
 */
package com.qiyei.share

import android.app.Application
import android.content.Context
import cn.sharesdk.onekeyshare.OnekeyShare
import com.mob.MobSDK


object ShareManager {

    fun init(context: Application){
        MobSDK.init(context)
    }

    fun showShare(context: Context){
        var oks = OnekeyShare()
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享测试");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(context);
    }
}