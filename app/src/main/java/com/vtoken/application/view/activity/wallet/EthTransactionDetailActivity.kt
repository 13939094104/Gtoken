package com.vtoken.application.view.activity.wallet

import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityEthTransactionDetailBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.TranslationRecordViewModel

class EthTransactionDetailActivity : BaseActivity() {
    lateinit var translationRecordViewModel: TranslationRecordViewModel
    lateinit var dataBinding:ActivityEthTransactionDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_translation_record)
        dataBinding= DataBindingUtil.setContentView(this, R.layout.activity_eth_transaction_detail)
        translationRecordViewModel= TranslationRecordViewModel(this)
        dataBinding.setVariable(BR.translationRecordViewModel,translationRecordViewModel)
    }

    fun getQrImageView():ImageView{
        return dataBinding.qrCode
    }
}
