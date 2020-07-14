package com.vtoken.application.viewModel.create

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableInt
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.view.activity.create.WalletCreateSecondActivity
import com.vtoken.application.view.activity.create.WalletRestoreWordActivity
import com.vtoken.application.viewModel.BaseViewModel
import vdsMain.wallet.WalletType
import androidx.databinding.ObservableField
import android.text.TextUtils
import android.util.Log
import com.vtoken.application.service.SyncService
import com.vtoken.application.util.PartLoader
import com.vtoken.application.view.activity.wallet.ChooseAccountActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import vdsMain.FileUtils


class WalletCreateFirstViewModel(context: Context):BaseViewModel(context) {

    //0 创建钱包 1恢复钱包
    var  walletType: ObservableInt= ObservableInt();
    val walletStepNum = ObservableField<String>()

    init {
        walletType.set(0)
        walletStepNum.set("(1/4)")
        this.vCashCore.checkAndSetWallType(WalletType.HOT);
    }

    fun changeWalletType(type:Int){
        walletType.set(type)
        when(type) {
            0 -> {
                walletStepNum.set("(1/4)")
                nextStep()
            }
            1 -> {
                walletStepNum.set("(1/3)")
                nextStep()
            }
        }
    }

    fun nextStep(){
        when(walletType.get()){
            0 -> {
                startActivity(Intent(this.context,WalletCreateSecondActivity::class.java))
            }
            1->{
                startActivity(Intent(this.context,WalletRestoreWordActivity::class.java))
            }
        }
    }


    fun backReInitWallet() {
        showLoadingFragment(getStringRescourcesByResName("loading_resetting") as CharSequence)
        Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                val walletPath = this@WalletCreateFirstViewModel.vCashCore.getAppWalletPath()
                val sb = StringBuilder()
                sb.append("unInitWallet: ")
                sb.append(walletPath)
                Log.i("IntroFirstViewModel", sb.toString())
                this@WalletCreateFirstViewModel.vCashCore.destoryWalletAndDB()
                try {
                    if (!TextUtils.equals(PartLoader.getSingleInstance(this@WalletCreateFirstViewModel.context).getDefaultWalletPath(), walletPath)) {
                        FileUtils.checkAndDeleteDirectory(walletPath)
                    }
                } catch (unused: Exception) {
                }

                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    this@WalletCreateFirstViewModel.dismissLoadingDialog()
                    val partWalletList = PartLoader.getSingleInstance(this@WalletCreateFirstViewModel.context).getPartWalletList()
                    if (partWalletList == null || partWalletList.isEmpty()) {
                        ApplicationLoader.getSingleApplicationContext().stopApp()
                        return
                    }
                    this@WalletCreateFirstViewModel.context.stopService(Intent(this@WalletCreateFirstViewModel.context, SyncService::class.java))
                    //this@WalletCreateFirstViewModel.context.stopService(Intent(this@WalletCreateFirstViewModel.context, ImService::class.java))
                    val viewModel = this@WalletCreateFirstViewModel
                    viewModel.startActivity(
                        Intent(
                            viewModel.context,
                            ChooseAccountActivity::class.java
                        ).putParcelableArrayListExtra("part_wallet", partWalletList)
                    )
                    this@WalletCreateFirstViewModel.finish()
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                    this@WalletCreateFirstViewModel.dismissLoadingDialog()
                    ApplicationLoader.getSingleApplicationContext().stopApp()
                }
            })
    }
}