package com.qiyei.audio.component.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.core.PlayMode
import kotlinx.android.synthetic.main.audio_activity_music_player.*

class MusicPlayerActivity : AppCompatActivity() , View.OnClickListener{

    private var mPlayMode:PlayMode = PlayMode.LOOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity_music_player)
        initData()
        initView()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.play_mode_btn_imv -> togglePlayMode()
        }
    }

    private fun initData(){

    }

    private fun initView(){
        play_mode_btn_imv.setOnClickListener(this)
    }

    private fun togglePlayMode(){
        when (mPlayMode) {
            PlayMode.REPEAT -> {
                mPlayMode = PlayMode.LOOP
                play_mode_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_play_mode_loop))
            }

            PlayMode.LOOP -> {
                mPlayMode = PlayMode.RANDOM
                play_mode_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_play_mode_random))
            }

            PlayMode.RANDOM -> {
                mPlayMode = PlayMode.REPEAT
                play_mode_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_play_mode_once))
            }
        }
        AudioPlayerManager?.getInstance().setPlayMode(mPlayMode)
    }
}
