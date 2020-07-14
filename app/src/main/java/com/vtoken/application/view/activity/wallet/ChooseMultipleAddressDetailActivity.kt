package com.vtoken.application.view.activity.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityChooseMultipleAddressDetailBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.MultipleWalletViewModel
import etherum.web3j.Web3jManager

class ChooseMultipleAddressDetailActivity : BaseActivity() {
    lateinit var multipleWalletViewModel: MultipleWalletViewModel
    lateinit var dataBinding:ActivityChooseMultipleAddressDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_multiple_wallet)
        dataBinding= DataBindingUtil.setContentView(this, R.layout.activity_choose_multiple_address_detail)
        multipleWalletViewModel= MultipleWalletViewModel(this)
        dataBinding.setVariable(BR.multipleWalletViewModel,multipleWalletViewModel)
        Web3jManager.getInstance().startWeb3jThread()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        multipleWalletViewModel.onActivityResult(requestCode,resultCode,data)
    }
    fun getMoreButton():View{
        return dataBinding.moreImage
    }
    override fun onDestroy() {
        super.onDestroy()
        multipleWalletViewModel.onDestroy()
    }
}
