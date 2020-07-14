package com.vtoken.application.view.activity.create

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletRestorePwdBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.create.WalletRestorePwdViewModel

class WalletRestorePwdActivity : BaseActivity() {

    lateinit var dataBinding:WalletRestorePwdBinding

    lateinit var pwdModel:WalletRestorePwdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.wallet_restore_pwd)
        pwdModel=WalletRestorePwdViewModel(this)
        dataBinding.setVariable(BR.walletRestorePwdModel,pwdModel)
    }

    override fun onBackPressed() {
        pwdModel.returnStep()
    }
}
