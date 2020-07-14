package com.vtoken.application.viewModel.create

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import com.vtoken.application.view.activity.create.WalletCreateConfirmActivity
import com.vtoken.application.view.activity.create.WalletCreateSecondActivity
import com.vtoken.application.viewModel.BaseViewModel
import com.vtoken.application.viewModel.PromptDialogViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

//bdm
class WalletCreateThirdViewModel(context: Context): BaseViewModel(context) {
    //f9962x
    var isScrapeFinish = ObservableBoolean()

    var mnemonicWordsArrayList: ArrayList<String>

    var mnemonicWordsString = ObservableField<String>()

    init {
        sHDSeed=vCashCore.getNewHDSeed()
        var mnemonicWordsList= sHDSeed.mnemonicWords.split(" ")
        mnemonicWordsArrayList= arrayListOf()
        mnemonicWordsArrayList.addAll(mnemonicWordsList)
        mnemonicWordsString.set(mergeStingListWithBlank(mnemonicWordsArrayList))
    }

    //m7679O
    fun checkAndShowConfirmDialog() {
        if(!isScrapeFinish.get()){
            return
        }
        val promptDialogViewModel = PromptDialogViewModel(this.context)
        promptDialogViewModel.setSpanned(SpannableString(getStringRescourcesByResName("bak_memsword_prompt")) as Spanned)
        createConfirmView(promptDialogViewModel, {
            val intent = Intent(this@WalletCreateThirdViewModel.getContext(), WalletCreateConfirmActivity::class.java)
            intent.putStringArrayListExtra("wordList", this@WalletCreateThirdViewModel.mnemonicWordsArrayList)
            intent.putExtra("pwd", this@WalletCreateThirdViewModel.getIntent().getStringExtra("pwd"))
            this@WalletCreateThirdViewModel.startActivity(intent)
            this@WalletCreateThirdViewModel.finish()
        }, { this@WalletCreateThirdViewModel.dismissConfirmDialog() })
        showConfirmDialog("memsBak", true)
    }


    fun returnStep() {
        showLoadingFragment(getStringRescourcesByResName("loading_resetting") as CharSequence)
        Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                val sb = StringBuilder()
                sb.append("subscribe: start destroyWallet")
                sb.append(Thread.currentThread().name)
                Log.e("IntroThirdViewModel", sb.toString())
                this@WalletCreateThirdViewModel.vCashCore.InitWalletIfNeed(true)
                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    val sb = StringBuilder()
                    sb.append("onNext: destroyWallet success ? ")
                    sb.append(bool)
                    Log.i("IntroThirdViewModel", sb.toString())
                    if (bool) {
                        this@WalletCreateThirdViewModel.dismissLoadingDialog()
                        val viewModel = this@WalletCreateThirdViewModel
                        viewModel.startActivity(Intent(viewModel.context, WalletCreateSecondActivity::class.java))
                        this@WalletCreateThirdViewModel.finish()
                    }
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                    this@WalletCreateThirdViewModel.dismissLoadingDialog()
                }
            })
    }

}
