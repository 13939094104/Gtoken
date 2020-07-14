package com.vtoken.application.view.activity.wallet

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletSendTranslationBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletSendTransactionViewModel

class WalletSendTransactionActivity : BaseActivity() {

    lateinit var dataBinding:WalletSendTranslationBinding

    lateinit var sendModel:WalletSendTransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.wallet_send_translation)
        sendModel=WalletSendTransactionViewModel(this)
        dataBinding.setVariable(BR.walletSendTransactionModel,sendModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null){
            this.sendModel.onActivityResult(requestCode,resultCode,data)
        }
    }
}
