package com.vtoken.application.viewModel.wallet

import android.content.Context
import androidx.databinding.ObservableField
import com.vtoken.application.view.activity.WebViewActivity
import com.vtoken.application.viewModel.BaseViewModel

class EthWebBrowerViewModel(context: Context): BaseViewModel(context) {

    var title = ObservableField("")

    init {
        title.set(intent.getStringExtra("title"))
        showLoadingFragment("正在加载")
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                dismissLoadingDialog()
            }

        },2000)
    }
    fun refreshWebView(){
        (this.context as WebViewActivity).getWebView().reload()
    }
}