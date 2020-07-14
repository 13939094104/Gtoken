package com.vtoken.application.util

import android.content.ClipData
import android.content.ClipboardManager
import com.vtoken.application.view.activity.BaseActivity

class CopyUtil: BaseActivity()  {
    companion object{
        lateinit var myClipboard: ClipboardManager

        fun copy(test:String){


            myClipboard.primaryClip= ClipData.newPlainText("text",test)

        }

    }
}