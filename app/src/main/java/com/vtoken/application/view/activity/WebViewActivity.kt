package com.vtoken.application.view.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityWebViewBinding
import com.vtoken.application.viewModel.wallet.EthWebBrowerViewModel
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {
    lateinit var ethWebBrowerViewModel: EthWebBrowerViewModel
    lateinit var dataBinding:ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_web_view)
        dataBinding= DataBindingUtil.setContentView<ActivityWebViewBinding>(this, R.layout.activity_web_view)
        ethWebBrowerViewModel= EthWebBrowerViewModel(this)
        //dataBinding.setVariable(BR.ethWebBrowerViewModel,ethWebBrowerViewModel)
        val url=intent.getStringExtra("url")
        webView.settings.javaScriptEnabled=true
        webView.loadUrl(url)
        webView.webViewClient=object:WebViewClient(){
        }
        finishpage.setOnClickListener({ finish() })
    }

    fun getWebView():WebView{
        return dataBinding.webView
    }
}
