package com.vtoken.application.view.activity.wallet

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletCreateAddressBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.WalletCreateAddressViewModel

class WalletAddressCreateActivity : BaseActivity() {

    lateinit var dataBinding: WalletCreateAddressBinding

    lateinit var createModel: WalletCreateAddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding= DataBindingUtil.setContentView(this, R.layout.wallet_create_address)
        createModel= WalletCreateAddressViewModel(this)
        dataBinding.setVariable(BR.walletCreateAddressModel,createModel);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null)
        {
            createModel.onActivityResult(requestCode,resultCode,data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
