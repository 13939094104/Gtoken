package com.vtoken.application.view.activity.wallet

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityPrivatekeyTypeBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.PrivateKeyTypeViewModel

//
class PrivateKeyTypeActivity : BaseActivity() {
    lateinit var exporteOrImportKeyViewModel: PrivateKeyTypeViewModel
    lateinit var dataBinding:ActivityPrivatekeyTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding= DataBindingUtil.setContentView(this, R.layout.activity_privatekey_type)
        exporteOrImportKeyViewModel= PrivateKeyTypeViewModel(this)
        dataBinding.setVariable(BR.exporteOrImportKeyViewModel,exporteOrImportKeyViewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        exporteOrImportKeyViewModel.onActivityResult(requestCode,resultCode,data)
    }
}
