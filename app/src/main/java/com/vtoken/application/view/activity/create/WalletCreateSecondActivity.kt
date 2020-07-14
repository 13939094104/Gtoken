package com.vtoken.application.view.activity.create

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletCreateSecondSetPwdBinding
import com.vtoken.application.view.activity.IntroBaseActivity
import com.vtoken.application.viewModel.create.WalletCreateSecondViewModel

class WalletCreateSecondActivity : IntroBaseActivity() {

    lateinit var secondModel: WalletCreateSecondViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dataBinding=DataBindingUtil.setContentView<WalletCreateSecondSetPwdBinding>(this,R.layout.wallet_create_second_set_pwd)
        secondModel= WalletCreateSecondViewModel(this)
        dataBinding.setVariable(BR.walletCreateSecondModel,secondModel)
    }

    override fun onBackPressed() {
        secondModel.returnStep()
    }
}
