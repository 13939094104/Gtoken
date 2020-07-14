package com.vtoken.application.view.activity.wallet

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityChooseAccountBinding
import com.vtoken.application.view.activity.BaseActivity
import com.vtoken.application.viewModel.wallet.ChooseAccountViewModel

class ChooseAccountActivity : BaseActivity() {
    lateinit var chooseAccountViewModel: ChooseAccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dataBinding= DataBindingUtil.setContentView<ActivityChooseAccountBinding>(this, R.layout.activity_choose_account)
        chooseAccountViewModel= ChooseAccountViewModel(this)
        dataBinding.setVariable(BR.chooseAccountViewModel,chooseAccountViewModel)
    }
    override fun onBackPressed() {
        chooseAccountViewModel.checkSwitchAndBack()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        chooseAccountViewModel.onActivityResult(requestCode,resultCode,data)
    }
}
