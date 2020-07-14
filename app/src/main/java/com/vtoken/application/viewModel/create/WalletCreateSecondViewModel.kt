package com.vtoken.application.viewModel.create

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import android.text.TextUtils
import com.vtoken.application.view.activity.create.WalletCreateThirdActivity
import com.vtoken.application.view.activity.create.WalletCreateFirstActivity
import com.vtoken.application.view.activity.create.WalletRestoreWordActivity
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

open class WalletCreateSecondViewModel(context: Context): BaseViewModel(context) {

    var firstPwd = ObservableField<String>()

    var secondPwd = ObservableField<String>()

    //915 f10467B
    var walletLabel = ObservableField<String>()

    init {
        firstPwd.set("")
        secondPwd.set("")
    }


    fun nextStepCheck(){
        if (isStringEmpty(this.walletLabel.get())) {
            showToast(getStringRescourcesByResName("hint_intro_2_input_account_name")) }
        else if(isStringEmpty(firstPwd.get())){
            showToast(getStringRescourcesByResName("toast_wallet_no_key"))
        }else if(!checkWalletPwdFormat(firstPwd.get())){
            showToast(getStringRescourcesByResName("toast_wallet_error_format"))
        }else if(isStringEmpty(secondPwd.get())||firstPwd.get()!=secondPwd.get()){
            showToast(getStringRescourcesByResName("toast_wallet_error_repeat"))
        }else{
            showLoadingFragment(getStringRescourcesByResName("splash_load_info"))
            Observable.create(object :ObservableOnSubscribe<Boolean>{
                override fun subscribe(emitter: ObservableEmitter<Boolean>) {
                    this@WalletCreateSecondViewModel.vCashCore.changeWalletPassword("",this@WalletCreateSecondViewModel.firstPwd.get()!!)
                    val properties = Properties()
                    val sb = StringBuilder()
                    sb.append(this@WalletCreateSecondViewModel.vCashCore.getAppWalletPath())
                    sb.append(File.separator)
                    sb.append("vds")
                    sb.append(File.separator)
                    sb.append("wallet.conf")
                    val sb2 = sb.toString()
                    try {
                        properties.load(InputStreamReader(FileInputStream(sb2), StandardCharsets.UTF_8))
                    } catch (unused: Exception) {
                    }

                    properties.setProperty(
                        "label",
                        if (TextUtils.isEmpty(this@WalletCreateSecondViewModel.walletLabel.get() as CharSequence)) "" else this@WalletCreateSecondViewModel.walletLabel.get()
                    )
                    try {
                        properties.store(OutputStreamWriter(FileOutputStream(sb2), StandardCharsets.UTF_8), "des")
                    } catch (unused2: Exception) {
                    }

                    this@WalletCreateSecondViewModel.vCashCore.setWalletLabel(if (TextUtils.isEmpty(this@WalletCreateSecondViewModel.walletLabel.get() as CharSequence)) "" else this@WalletCreateSecondViewModel.walletLabel.get()!!)
                    emitter.onNext(true)
                }
            }).compose(bindDestoryEvent()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                this@WalletCreateSecondViewModel.dismissLoadingDialog()
                if(it){
                    val intent=Intent(this@WalletCreateSecondViewModel.getContext(),WalletCreateThirdActivity::class.java)
                    intent.putExtra("pwd",this@WalletCreateSecondViewModel.firstPwd.get())
                    this@WalletCreateSecondViewModel.startActivity(intent)
                    this@WalletCreateSecondViewModel.finish()
                }
            },{
                it.printStackTrace()
                this@WalletCreateSecondViewModel.dismissLoadingDialog()
            })
        }
    }

    open fun returnStep() {
        showLoadingFragment(getStringRescourcesByResName("loading_resetting") as CharSequence)
        io.reactivex.Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                this@WalletCreateSecondViewModel.vCashCore.InitWalletIfNeed(false)
                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    if (bool) {
                        this@WalletCreateSecondViewModel.dismissLoadingDialog()
                        val viewModel = this@WalletCreateSecondViewModel
                        viewModel.startActivity(Intent(viewModel.context, WalletCreateFirstActivity::class.java))
                        this@WalletCreateSecondViewModel.finish()
                    }
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                    this@WalletCreateSecondViewModel.dismissLoadingDialog()
                }
            })
    }
    
    fun toRestoreWord(){
        startActivity(Intent(this.context, WalletRestoreWordActivity::class.java))
    }
}