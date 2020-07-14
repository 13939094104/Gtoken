package com.vtoken.application.view.activity.wallet

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityEthAddressDetailBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.EthAddressDetailViewModel

class EthAddressDetailActivity : BaseActivity() {
    lateinit var ethAddressDetailViewModel: EthAddressDetailViewModel
    lateinit var dataBinding: ActivityEthAddressDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding= DataBindingUtil.setContentView(this, R.layout.activity_eth_address_detail)
        ethAddressDetailViewModel= EthAddressDetailViewModel(this)
        dataBinding.setVariable(BR.usdtAddressDetailViewModel,ethAddressDetailViewModel)
        setOnCheckedChangeListener()
        this.dataBinding.radioAll.performClick()
    }

    private fun setOnCheckedChangeListener() {
        this.dataBinding.radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (!(radioGroup.findViewById<View>(i) as RadioButton).isChecked) {
                return@OnCheckedChangeListener
            }
            if (i == R.id.radio_all) {
                this@EthAddressDetailActivity.ethAddressDetailViewModel.getTransactionRecord(0)
            } else if (i == R.id.radio_receive) {
                this@EthAddressDetailActivity.ethAddressDetailViewModel.getTransactionRecord(1)
            } else if (i == R.id.radio_send) {
                this@EthAddressDetailActivity.ethAddressDetailViewModel.getTransactionRecord(2)
            }
//            else if (i == R.id.radio_fail) {
//                this@EthAddressDetailActivity.ethAddressDetailViewModel.getTransactionRecord(3)
//            }
        })
    }

    override fun onDestroy() {
        ethAddressDetailViewModel.onDestroy()
        super.onDestroy()
    }
}
