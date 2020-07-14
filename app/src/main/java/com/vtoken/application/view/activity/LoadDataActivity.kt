package com.vtoken.application.view.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivityLoadDataBinding
import com.vtoken.application.viewModel.LoadDataViewModel

class LoadDataActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityLoadDataBinding

    /* renamed from: b */
    private  lateinit var viewModel: LoadDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this, R.layout.activity_load_data)
        this.viewModel = LoadDataViewModel(this)
        this.dataBinding.setVariable(BR.loadDataViewModel,this.viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.viewModel.removeHandlerAndClearDialog()
    }
}
