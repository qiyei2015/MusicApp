/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 用户登录
 */
package com.qiyei.music.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.qiyei.common.ui.activity.BaseActivity
import com.qiyei.music.R
import com.qiyei.music.api.IMusicHttpService
import com.qiyei.music.api.protocol.UserLoginReq
import com.qiyei.music.common.Env
import com.qiyei.network.api.HttpManager
import com.qiyei.network.api.HttpRequest
import com.qiyei.network.api.IHttpListener
import com.qiyei.video.component.activity.VideoPlayerDemoActivity
import kotlinx.android.synthetic.main.activity_user_login.*
import java.lang.Exception

class UserLoginActivity : BaseActivity() {

    companion object {
        const val TAG = "UserLoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        user_login_tv.setOnClickListener {
//            HttpManager().execute(supportFragmentManager,buildUserLoginRequest(),object:IHttpListener<Any>{
//                override fun onSuccess(response: Any?) {
//                    Log.i(TAG,"onSuccess $response")
//                }
//
//                override fun onFailure(exception: Exception?) {
//                    Log.i(TAG,"onFailure $exception")
//                }
//            })

            startActivity(Intent(UserLoginActivity@this,VideoPlayerDemoActivity::class.java))
        }
    }

    private fun buildUserLoginRequest():HttpRequest<Any>{
        val req = UserLoginReq("18734924592","999999q")
        return HttpRequest.Builder<UserLoginReq>()
            .get()
            .setBaseUrl(Env.ROOT_URL)
            .setPathUrl(Env.LOGIN)
            .setBody(req)
            .setApiClazz(IMusicHttpService::class.java)
            .build()
    }
}
