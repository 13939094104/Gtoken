package com.vtoken.application.view.activity.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityEthSendTransactionBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.EthSendTransactionViewModel
import android.graphics.Typeface
import androidx.core.content.ContextCompat.getSystemService
import com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent



class EthSendTransactionActivity : BaseActivity() {
    lateinit var translationViewModel: EthSendTransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_usdt_send_translation)
        var dataBinding= DataBindingUtil.setContentView<ActivityEthSendTransactionBinding>(this, R.layout.activity_eth_send_transaction)
        translationViewModel= EthSendTransactionViewModel(this)
        dataBinding.setVariable(BR.usdtSendTranslationViewModel,translationViewModel)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        translationViewModel.onActivityResult(requestCode,resultCode,data)
    }

    override fun onDestroy() {
        translationViewModel.onDestroy()
        super.onDestroy()
    }
}
