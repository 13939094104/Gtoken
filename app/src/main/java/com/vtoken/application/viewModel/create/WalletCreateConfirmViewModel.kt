package com.vtoken.application.viewModel.create

import android.content.Context
import android.content.Intent
import androidx.databinding.*
import android.util.Log
import com.vcwallet.core.part.PartWallet
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.util.PartLoader
import com.vtoken.application.view.activity.create.WalletCreateSecondActivity
import com.vtoken.application.viewModel.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import vdsMain.ActivityManager
import vdsMain.tool.DeviceUtil
import java.util.ArrayList

class WalletCreateConfirmViewModel(context: Context):BaseViewModel(context) {

    companion object{
        //f9880D
        private val maxWordLength = Integer.valueOf(12)

        //f9881E
        private val maxErrorTime = Integer.valueOf(5)
    }

    internal var f9884C: Int = 0

    //f9885F
    private val wordStringList: ArrayList<String>

    //f9888x
    var selectWordsString = ObservableField<String>()

    //f9886G
    private var curErrorTime = 0

    //f9883B
    var selectedList: ObservableList<String> = ObservableArrayList()

    //f9889y
    var isAllSelected = ObservableBoolean()

    var f9890z = ObservableInt(4)

    //f9887H
    private var walletCreateConfirmEvent: WalletCreateConfirmEvent? = null

    //C3293a
    interface WalletCreateConfirmEvent {
        //mo39731a
        fun onRevokeFinish()
    }

    init {
        this.f9884C = ApplicationLoader.getSingleApplicationContext().getActivityWidthPixels() - DeviceUtil.dp2px(context, 60.0f)
        this.wordStringList = intent.getStringArrayListExtra("wordList")
    }

    //mo41629O
    fun getWordStringList(): ArrayList<String> {
        return this.wordStringList
    }

    fun setListener(event: WalletCreateConfirmEvent) {
        this.walletCreateConfirmEvent = event
    }

    fun mo41631a(i: Int) {
        this.f9884C = i
    }

    //mo39929b
    fun checkWordsAndJump() {
        if (this.wordStringList != this.selectedList) {
            if (this.curErrorTime > maxErrorTime.toInt()) {
                showToast(getStringRescourcesByResName("toast_create_confirm_word_error_to_many"))
            } else {
                showToast(getStringRescourcesByResName("toast_create_confirm_word_error"))
            }
            this.curErrorTime++
        } else if (sHDSeed != null) {
            val stringExtra = intent.getStringExtra("pwd")
            showLoadingFragment(getStringRescourcesByResName("loading_create_msg") as CharSequence)
            io.reactivex.Observable.create(object : ObservableOnSubscribe<Boolean> {
                override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                    this@WalletCreateConfirmViewModel.vCashCore.InitHDAccount(stringExtra as CharSequence, BaseViewModel.sHDSeed, 0)
                    val partLoader = PartLoader.getSingleInstance(this@WalletCreateConfirmViewModel.context)
                    val partWalletList = partLoader.getPartWalletList()
                    if (partWalletList == null) {
                        val arrayList = ArrayList<PartWallet>()
                        arrayList.add(PartWallet(this@WalletCreateConfirmViewModel.vCashCore.getAppWalletPath(), this@WalletCreateConfirmViewModel.vCashCore.getWalletLabel()))
                        partLoader.setPartWalletList(arrayList)
                    } else {
                        partWalletList.add(PartWallet(this@WalletCreateConfirmViewModel.vCashCore.getAppWalletPath(), this@WalletCreateConfirmViewModel.vCashCore.getWalletLabel()))
                    }
                    observableEmitter.onNext(java.lang.Boolean.valueOf(true))
                }
            }).compose(bindDestoryEvent()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Boolean> {
                    /* renamed from: a */
                    override fun accept(bool: Boolean?) {
                        if (bool!!) {
                            val walletCreateConfirmViewModel = this@WalletCreateConfirmViewModel
                            walletCreateConfirmViewModel.showToast(walletCreateConfirmViewModel.getStringRescourcesByResName("toast_create_confirm_word_success"))
                            this@WalletCreateConfirmViewModel.jumpToWalletMainAndRemoveAll()
                            return
                        }
                        this@WalletCreateConfirmViewModel.dismissLoadingDialog()
                    }
                }, object : Consumer<Throwable> {
                    /* renamed from: a */
                    override fun accept(th: Throwable) {
                        this@WalletCreateConfirmViewModel.dismissLoadingDialog()
                        Log.e("error", "jumpToMain:but has trouble ")
                        th.printStackTrace()
                        val walletCreateConfirmViewModel = this@WalletCreateConfirmViewModel
                        walletCreateConfirmViewModel.showToast(walletCreateConfirmViewModel.getStringRescourcesByResName("init_failed"))
                    }
                })
        } else {
            Log.e("aaa", "jumpToMain:but hdseed is null ")
        }
    }

    fun revokeOne(){
        walletCreateConfirmEvent!!.onRevokeFinish();

    }

    //mo41632b
    fun checkAndAddToObserverList(str: String, isSelected: Boolean) {
        if (isSelected) {
            this.selectedList.add(str)
        } else {
            this.selectedList.remove(str)
        }
        this.selectWordsString.set(mergeStingListWithBlank(this.selectedList as List<String>))
        if (!this.selectedList.isEmpty()) {
            this.f9890z.set(0)
        } else {
            this.f9890z.set(4)
        }
        if (this.selectedList.size >= maxWordLength.toInt()) {
            this.isAllSelected.set(true)
        } else {
            this.isAllSelected.set(false)
        }
    }

    fun returnStep() {
        showLoadingFragment(getStringRescourcesByResName("loading_resetting") as CharSequence)
        Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                this@WalletCreateConfirmViewModel.vCashCore.InitWalletIfNeed(true)
                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    if (bool) {
                        val viewModel = this@WalletCreateConfirmViewModel
                        viewModel.startActivity(Intent(viewModel.context, WalletCreateSecondActivity::class.java))
                        this@WalletCreateConfirmViewModel.dismissLoadingDialog()
                        this@WalletCreateConfirmViewModel.finish()
                        ActivityManager.getInstance().removeAll()
                    }
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                    this@WalletCreateConfirmViewModel.dismissLoadingDialog()
                }
            })
    }
    
}