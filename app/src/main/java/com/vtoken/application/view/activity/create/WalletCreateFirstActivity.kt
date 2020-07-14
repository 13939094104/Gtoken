package com.vtoken.application.view.activity.create

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletCreateInportBinding
import com.vtoken.application.view.activity.IntroBaseActivity
import com.vtoken.application.viewModel.create.WalletCreateFirstViewModel

class WalletCreateFirstActivity : IntroBaseActivity() {

    lateinit var createFirstViewModel:WalletCreateFirstViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dataBinding=DataBindingUtil.setContentView<WalletCreateInportBinding>(this,R.layout.wallet_create_inport)
        createFirstViewModel= WalletCreateFirstViewModel(this)
        dataBinding.setVariable(BR.walletCreateFirstModel,createFirstViewModel)
    }


    override fun onBackPressed() {
        createFirstViewModel.backReInitWallet()
    }
}
