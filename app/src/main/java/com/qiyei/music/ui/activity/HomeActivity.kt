package com.qiyei.music.ui.activity


import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.qiyei.common.ui.activity.BaseActivity
import com.qiyei.music.R
import com.qiyei.music.ui.fragment.FindFragment
import com.qiyei.music.ui.fragment.FriendFragment
import com.qiyei.music.ui.fragment.MyFragment
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author Created by qiyei2015 on 2020/3/27.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 首页
 */
class HomeActivity : BaseActivity() {


    private val names = listOf<String>("我的","发现","朋友")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
    }

    override protected fun initView():Unit{

        slide_toggle_imv.setOnClickListener {
            if (!draw_layout.isDrawerOpen(GravityCompat.START)){
                draw_layout.openDrawer(GravityCompat.START)
            } else {
                draw_layout.closeDrawer(GravityCompat.START)
            }
        }

        home_view_pager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getItem(position: Int): Fragment {
                val name = names[position]
                when (name) {
                    "我的" -> return MyFragment.newInstance("","");
                    "发现" -> return FindFragment()
                    "朋友" -> return FriendFragment()
                    else -> return FindFragment()
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return names[position]
            }

            override fun getCount(): Int {
                return names.size;
            }
        }
        home_tab_layout.setupWithViewPager(home_view_pager)

    }
}
