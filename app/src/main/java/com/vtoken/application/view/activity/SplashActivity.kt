package com.vtoken.application.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.BR
import com.vtoken.application.R
import com.vtoken.application.databinding.ActivitySplashBinding
import com.vtoken.application.viewModel.SplashViewModel

class SplashActivity : IntroBaseActivity() {

    private lateinit var activitySplashBinding: ActivitySplashBinding

    /* renamed from: b */
    private  lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(ApplicationLoader.isApkInDebug()){
            Logger.addLogAdapter(AndroidLogAdapter())
        }
        activitySplashBinding=DataBindingUtil.setContentView(this, R.layout.activity_splash)
        this.splashViewModel = SplashViewModel(this)
        this.activitySplashBinding.setVariable(BR.splashViewModel,this.splashViewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.splashViewModel.removeHandlerAndClearDialog()
    }
}
