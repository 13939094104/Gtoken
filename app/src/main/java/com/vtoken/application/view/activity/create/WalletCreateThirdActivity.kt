package com.vtoken.application.view.activity.create

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.WalletCreateThirdBinding
import com.vtoken.application.view.activity.IntroBaseActivity
import com.vtoken.application.viewModel.create.WalletCreateThirdViewModel
import kotlinx.android.synthetic.main.wallet_create_third.*

class WalletCreateThirdActivity : IntroBaseActivity() {

    lateinit var thirdModel:WalletCreateThirdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding=DataBindingUtil.setContentView<WalletCreateThirdBinding>(this,R.layout.wallet_create_third)
        thirdModel= WalletCreateThirdViewModel(this)
        dataBinding.setVariable(BR.walletCreateThirdModel,thirdModel)
        scrapeView.setOnScrapeListener {
            thirdModel.isScrapeFinish.set(true)
        }
        memericWords.viewTreeObserver.addOnGlobalLayoutListener {
            this@WalletCreateThirdActivity.scrapeView.initBitmap(this@WalletCreateThirdActivity.memericWords.width,this@WalletCreateThirdActivity.memericWords.height)
        }
    }

    override fun onBackPressed() {
        thirdModel.returnStep()
    }
}
