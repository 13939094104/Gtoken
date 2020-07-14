package com.vtoken.application.view.activity.wallet

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletImportPrivatekeyBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletImportKeyViewModel


class WalletImportKeyActivity : BaseActivity() {

    lateinit var dataBinding:WalletImportPrivatekeyBinding

    lateinit var viewModel: WalletImportKeyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this,R.layout.wallet_import_privatekey)
        viewModel=WalletImportKeyViewModel(this)
        dataBinding.setVariable(BR.importKeyModel,viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode,resultCode,data)
        val parseActivityResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (parseActivityResult != null) {
            if (parseActivityResult.contents != null) {
                this.viewModel.setPrivateKeyString(parseActivityResult.contents)
            }
        } else if (resultCode == -1 && requestCode == 1002) {
            val importKeyViewModel = this.viewModel
            if (importKeyViewModel != null) {
                importKeyViewModel.startImportKey(data!!.getStringExtra("pwd"))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
