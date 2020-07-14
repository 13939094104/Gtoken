package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.vtoken.application.model.EthTransaction
import com.vtoken.application.view.activity.WebViewActivity
import com.vtoken.application.view.activity.wallet.EthTransactionDetailActivity
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import vdsMain.tool.Util

class TranslationRecordViewModel(context: Context): BaseViewModel(context) {

    val transaction:EthTransaction

    init {
        transaction=intent.getSerializableExtra("transaction") as EthTransaction;
        val observer = object : Observer<Bitmap> {
            override fun onComplete() {}

            override fun onSubscribe(disposable: Disposable) {}

            override fun onError(th: Throwable) {
                th.printStackTrace()
            }

            /* renamed from: a */
            override fun onNext(bitmap: Bitmap) {
                (context as EthTransactionDetailActivity).getQrImageView().setImageBitmap(bitmap)
            }
        }
        Util.getQrCodeBitmapWithCenterDrawable(transaction.hash, observer, -1)
    }

    fun copyHash(){
        Util.copyAndShowToast(transaction.hash)
    }
    fun copyTransactionto(){
        Util.copyAndShowToast(transaction.to)
    }
    fun copyTransactionfrom(){
        Util.copyAndShowToast(transaction.from)
    }

    fun jumpExploer(){
        startActivity(Intent(this.context,WebViewActivity::class.java).putExtra("url"
            ,String.format("https://cn.etherscan.com/tx/%s?locale=zh-CN",transaction.hash)))
    }
}