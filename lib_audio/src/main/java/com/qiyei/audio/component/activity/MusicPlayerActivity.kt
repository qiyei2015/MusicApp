package com.qiyei.audio.component.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qiyei.audio.R
import com.qiyei.audio.api.AudioPlayerManager
import com.qiyei.audio.component.dialog.AudioListDialog
import com.qiyei.audio.component.view.AudioStylusView
import com.qiyei.audio.core.IAudioStatusListener
import com.qiyei.audio.core.PlayMode
import com.qiyei.audio.core.Status
import com.qiyei.audio.exception.AudioStatusException
import com.qiyei.audio.model.AudioBean
import kotlinx.android.synthetic.main.audio_activity_music_player.*

class MusicPlayerActivity : AppCompatActivity(), View.OnClickListener {


    companion object {
        const val TAG = "MusicPlayerActivity"
    }

    private var mPlayMode: PlayMode = PlayMode.LOOP
    private var isPlay: Boolean = false

    private var mCurrentBean: AudioBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity_music_player)
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioPlayerManager.getInstance().removeAudioStatusListener("music")
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.favourite_btn_imv -> toggleMusicFavourite()
            R.id.play_mode_btn_imv -> togglePlayMode()
            R.id.prev_btn_imv -> playPrev()
            R.id.play_btn_imv -> toggleMusicPlay()
            R.id.next_btn_play -> playNext()
            R.id.show_more_music -> showMusicList()
        }
    }


    private fun initData() {
        isPlay = false
        mCurrentBean = AudioBean()
        AudioPlayerManager.getInstance().addAudioStatusListener("music",object :IAudioStatusListener{
            override fun onAudioLoaded(bean: AudioBean?) {
                Log.i(TAG,"onAudioLoaded")
            }

            override fun onAudioStarted(bean: AudioBean?) {
                Log.i(TAG,"onAudioStarted")
            }

            override fun onAudioProgress(status: Status, progress: Int, maxLength: Int) {

            }

            override fun onAudioCompleted(bean: AudioBean?) {

            }

            override fun onAudioPause(bean: AudioBean?) {
                Log.i(TAG,"onAudioPause")
            }

            override fun onAudioReleased(bean: AudioBean?) {

            }

            override fun onError(exception: AudioStatusException) {
                Log.i(TAG,"onError exception=$exception")
            }
        })
    }

    private fun initView() {
        play_btn_imv.setImageDrawable(getDrawable(R.mipmap.icon_audio_music_play))

        favourite_btn_imv.setOnClickListener(this)
        play_mode_btn_imv.setOnClickListener(this)
        prev_btn_imv.setOnClickListener(this)
        play_btn_imv.setOnClickListener(this)
        next_btn_play.setOnClickListener(this)
        show_more_music.setOnClickListener(this)
        AudioPlayerManager.getInstance().play()
        isPlay = true
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
        AudioPlayerManager.getInstance().playPrev()
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
        AudioPlayerManager.getInstance().playNext()
    }

    private fun showMusicList(){
        val dialog = AudioListDialog(MusicPlayerActivity@this)
        dialog.show()
    }
}
