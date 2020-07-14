package com.vtoken.application.view.activity.wallet

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletTransactionDetailBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletTransactionDetailViewModel

class WalletTransactionRecordActivity : BaseActivity() {

    lateinit var dataBinding:WalletTransactionDetailBinding

    lateinit var transactionModel: WalletTransactionDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this,R.layout.wallet_transaction_detail)
        transactionModel=WalletTransactionDetailViewModel(this)
        dataBinding.setVariable(BR.walletTransactionDetailModel,transactionModel)
        setOnCheckedChangeListener()
        this.dataBinding.radioFrom.performClick()
    }

    //m4136a
    private fun setOnCheckedChangeListener() {
        this.dataBinding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (!(radioGroup.findViewById<View>(i) as RadioButton).isChecked) {
                return@OnCheckedChangeListener
            }
            if (i == R.id.radio_from) {
                this@WalletTransactionRecordActivity.transactionModel.initFromDataList()
            } else {
                this@WalletTransactionRecordActivity.transactionModel.initToDataList()
            }
        })
    }
}
