package com.vtoken.application.viewModel.create

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.vcwallet.core.part.PartWallet
import com.vtoken.application.R
import com.vtoken.application.databinding.DialogInitInfoBinding
import com.vtoken.application.util.PartLoader
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.ArrayList

class WalletRestorePwdViewModel(context: Context):WalletCreateSecondViewModel(context) {

    //f9918C
    var createFinish = true

    //f9920x
    protected var wordStringList: List<String>

    init {
        this.wordStringList = intent.getStringArrayListExtra("wordList")
    }

    fun checkAndRestoreWallet(){
        if(createFinish){
            if(isStringEmpty(walletLabel.get())){
                showToast(getStringRescourcesByResName("hint_intro_2_input_account_name"))
            }
            else if(isStringEmpty(firstPwd.get())){
                showToast(getStringRescourcesByResName("toast_wallet_no_key"))
            }else if(!checkWalletPwdFormat(firstPwd.get())){
                showToast(getStringRescourcesByResName("toast_wallet_error_format"))
            }else if(isStringEmpty(secondPwd.get())||firstPwd.get()!=secondPwd.get()){
                showToast(getStringRescourcesByResName("toast_wallet_error_repeat"))
            }else if(!wordStringList.isEmpty()){
                createFinish=false
                showPopueWindowToInitAccount(firstPwd.get()!!)
            }
        }
    }


    //m7646a
      private fun showPopueWindowToInitAccount(str:String) {
        val initInfoBinding =  DataBindingUtil.inflate<DialogInitInfoBinding>(LayoutInflater.from(this.context), R.layout.dialog_init_info, null, false)
          setConfirmDialogView(initInfoBinding.getRoot())
          showConfirmDialog("init_number", false);
        initInfoBinding.confirmText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view:View) {
                this@WalletRestorePwdViewModel.dismissConfirmDialog();
                this@WalletRestorePwdViewModel.confirmDialog = null;
                val anonymousStr = initInfoBinding.anonymousEdit.getText().toString();
                val commonStr = initInfoBinding.commonEdit.getText().toString();
                val vidStr = initInfoBinding.vidEdit.getText().toString();
                val model = this@WalletRestorePwdViewModel
                var vidNum = 0;
                val anonymousNum = if(TextUtils.isEmpty(anonymousStr)) 0 else anonymousStr.toInt()
                val commonNum =if(TextUtils.isEmpty(commonStr))  0 else commonStr.toInt();
                if (!TextUtils.isEmpty(vidStr)) {
                    vidNum = Integer.parseInt(vidStr);
                }
                model.startObserverToInitHDAccountAndCreateAddress(str, anonymousNum, commonNum, vidNum);
            }
        });
        initInfoBinding.cancelText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view:View) {
                this@WalletRestorePwdViewModel.dismissConfirmDialog();
                this@WalletRestorePwdViewModel.confirmDialog = null;
                this@WalletRestorePwdViewModel.startObserverToInitHDAccountAndCreateAddress(str, 0, 0, 0);
            }
        });
    }


    //m7643a
    fun createAddresses(i: Int, str: String, i2: Int, i3: Int, observableEmitter: ObservableEmitter<Boolean>) {
        this.vCashCore.changeWalletPassword("" as CharSequence, this.firstPwd.get() as CharSequence)
        val sb = StringBuilder()
        sb.append(this.vCashCore.getAppWalletPath())
        sb.append(File.separator)
        sb.append("vds")
        sb.append(File.separator)
        sb.append("wallet.conf")
        val confPath = sb.toString()
        val properties = PartLoader.getSingleInstance(this.context).loadPropertiesFromPath(confPath)
        properties.setProperty("label", if (TextUtils.isEmpty(this.walletLabel.get() as CharSequence)) "" else this.walletLabel.get())
        try {
            properties.store(OutputStreamWriter(FileOutputStream(confPath), StandardCharsets.UTF_8), "des")
        } catch (unused: Exception) {
        }

        this.vCashCore.setWalletLabel(if (TextUtils.isEmpty(this.walletLabel.get() as CharSequence)) "" else this.walletLabel.get()!!)
        val partLoader = PartLoader.getSingleInstance(this.context)
        val partWalletList = partLoader.getPartWalletList()
        if (partWalletList == null) {
            val arrayList = ArrayList<PartWallet>()
            arrayList.add(PartWallet(this.vCashCore.getAppWalletPath(), this.walletLabel.get() as String))
            partLoader.setPartWalletList(arrayList)
        } else {
            partWalletList.add(PartWallet(this.vCashCore.getAppWalletPath(), this.walletLabel.get() as String))
        }
        val vCashCore1 = this.vCashCore
        val charSequence = this.firstPwd.get() as CharSequence
        val wordList = this.wordStringList
        var isSuccess = true
        val hdAccount = vCashCore1.initHDAccountFromWordList(charSequence, wordList.toTypedArray() as Array<String>, 0)
        var m = this.vCashCore.getWalletAddressModel().getAddressAmount()
        if (i > 0) {
            var i4 = 0
            while (i4 < i) {
                try {
                    //this.vCashCore.createAnonymousAccount(str as CharSequence, m7641Q(), arrayOfNulls<CharSequence>(0))
                    i4++
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        val normalNumber = i2 + i3
        if (normalNumber > 0) {
            try {
                this.vCashCore.createNormalAddress(str as CharSequence, normalNumber)
            } catch (e2: Exception) {
                e2.printStackTrace()
            }

        }
        if (hdAccount == null) {
            isSuccess = false
        }
        observableEmitter.onNext(java.lang.Boolean.valueOf(isSuccess))
    }

    @SuppressLint("CheckResult")
    //mo41650a
    fun startObserverToInitHDAccountAndCreateAddress(str: String, commonNumber: Int, vidNumber: Int, i3: Int) {
        showLoadingFragment(getStringRescourcesByResName("splash_load_info") as CharSequence)
        Observable.create(object :ObservableOnSubscribe<Boolean>{
            override fun subscribe(emitter: ObservableEmitter<Boolean>) {
                this@WalletRestorePwdViewModel.createAddresses(commonNumber,str,vidNumber,i3,emitter)
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<Boolean> {
                /* renamed from: a */
                override fun accept(bool: Boolean?) {
                    if (bool!!) {
                        this@WalletRestorePwdViewModel.jumpToWalletMainAndRemoveAll()
                    } else {
                        val walletRestorePwdViewModel = this@WalletRestorePwdViewModel
                        walletRestorePwdViewModel.showToast(walletRestorePwdViewModel.getStringRescourcesByResName("toast_import_hd_mnemonic_failed"))
                        this@WalletRestorePwdViewModel.dismissLoadingDialog()
                    }
                    this@WalletRestorePwdViewModel.createFinish = true
                }
            }, object : Consumer<Throwable> {
                /* renamed from: a */
                override fun accept(th: Throwable) {
                    Log.e("IntroRecoveryPwdSetting", "importHDMnemonicWords has trouble ")
                    val walletRestorePwdViewModel = this@WalletRestorePwdViewModel
                    walletRestorePwdViewModel.showToast(walletRestorePwdViewModel.getStringRescourcesByResName("toast_import_hd_mnemonic_failed"))
                    th.printStackTrace()
                    this@WalletRestorePwdViewModel.dismissLoadingDialog()
                    this@WalletRestorePwdViewModel.createFinish = true
                }
            })
    }

    override fun returnStep() {
        showLoadingFragment(getStringRescourcesByResName("loading_resetting") as CharSequence)
        Observable.create(object : ObservableOnSubscribe<Boolean> {
            override fun subscribe(observableEmitter: ObservableEmitter<Boolean>) {
                this@WalletRestorePwdViewModel.vCashCore.InitWalletIfNeed(true)
                observableEmitter.onNext(java.lang.Boolean.valueOf(true))
            }
        }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {}

                override fun onSubscribe(disposable: Disposable) {}

                /* renamed from: a */
                override fun onNext(bool: Boolean) {
                    if (bool) {
                        this@WalletRestorePwdViewModel.dismissLoadingDialog()
                        this@WalletRestorePwdViewModel.finish()
                    }
                }

                override fun onError(th: Throwable) {
                    th.printStackTrace()
                    this@WalletRestorePwdViewModel.dismissLoadingDialog()
                }
            })
    }
}