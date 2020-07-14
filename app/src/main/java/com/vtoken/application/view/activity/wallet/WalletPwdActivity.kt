package com.vtoken.application.view.activity.wallet

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityWalletChangePwdBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletPwdViewModel

class WalletPwdActivity : BaseActivity() {

    lateinit var dataBinding:ActivityWalletChangePwdBinding

    lateinit var viewModel:WalletPwdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_wallet_change_pwd)
        viewModel=WalletPwdViewModel(this)
        dataBinding.setVariable(BR.walletPwdModel,viewModel)
    }
}
