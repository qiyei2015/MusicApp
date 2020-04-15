/**
 * @author Created by qiyei2015 on 2020/4/12.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: AudioListDialog
 */
package com.qiyei.audio.component.dialog

import android.content.Context
import android.os.Bundle
import android.view.Display
import android.view.Window
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.qiyei.audio.R


class AudioListDialog(
    context: Context,
    styleTheme: Int = 0
) : BottomSheetDialog(context, styleTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_dialog_audio_list)

        val dm = context.resources.displayMetrics
        val layoutParams = window?.attributes
        layoutParams?.width = dm.widthPixels
        window?.attributes = layoutParams
        setCancelable(true)
        setCanceledOnTouchOutside(true)

    }

    override fun show() {
        super.show()

    }

    override fun dismiss() {
        super.dismiss()
    }


}