package com.qiyei.audio.component.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.core.PlayMode
import com.qiyei.audio.model.AudioBean
import kotlinx.android.synthetic.main.audio_activity_music_player.*

class MusicPlayerActivity : AppCompatActivity(), View.OnClickListener {

    private var mPlayMode: PlayMode = PlayMode.LOOP
    private var isPlay: Boolean = false

    private var mCurrentBean: AudioBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity_music_player)
        initData()
        initView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.favourite_btn_imv -> toggleMusicFavourite()
            R.id.play_mode_btn_imv -> togglePlayMode()
            R.id.prev_btn_imv -> playPrev()
            R.id.play_btn_imv -> toggleMusicPlay()
            R.id.next_btn_play -> playNext()
        }
    }

    private fun initData() {
        isPlay = false
        mCurrentBean = AudioBean()
    }

    private fun initView() {
        play_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_music_pause))

        favourite_btn_imv.setOnClickListener(this)
        play_mode_btn_imv.setOnClickListener(this)
        prev_btn_imv.setOnClickListener(this)
        play_btn_imv.setOnClickListener(this)
        next_btn_play.setOnClickListener(this)
    }

    private fun togglePlayMode() {
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

    private fun toggleMusicFavourite() {
        favourite_btn_imv.isActivated = !favourite_btn_imv.isActivated
        if (favourite_btn_imv.isActivated) {
            AudioPlayerManager.getInstance().addFavourite(mCurrentBean!!)
        } else {
            AudioPlayerManager.getInstance().removeFavourite(mCurrentBean!!)
        }
    }

    private fun playPrev() {
        AudioPlayerManager.getInstance().prev()
    }

    private fun toggleMusicPlay() {
        if (isPlay) {
            isPlay = false
            play_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_music_pause))
            AudioPlayerManager.getInstance().pause()
        } else {
            isPlay = true
            play_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_music_play))
            AudioPlayerManager.getInstance().resume()
        }
    }

    private fun playNext() {

    }
}
