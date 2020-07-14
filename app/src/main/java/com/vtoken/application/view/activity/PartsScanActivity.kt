package com.vtoken.application.view.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.vtoken.application.viewModel.PartsScanViewModel

class PartsScanActivity : BaseActivity() {

    lateinit var viewModel:PartsScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = 0
        window.setBackgroundDrawable(ColorDrawable(0))
        viewModel= PartsScanViewModel(this)
        viewModel.initScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }
}
