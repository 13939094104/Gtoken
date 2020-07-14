package com.vtoken.application.view.activity.wallet

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletSendChooseUtxoBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletSendChooseUtxoViewModel

class WalletSendChooseUtxoActivity : BaseActivity() {

    lateinit var dataBinding:WalletSendChooseUtxoBinding

    lateinit var chooseUtxoModel:WalletSendChooseUtxoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.wallet_send_choose_utxo)
        chooseUtxoModel= WalletSendChooseUtxoViewModel(this)
        dataBinding.setVariable(BR.walletSendChooseUtxoModel,chooseUtxoModel)
    }
}
