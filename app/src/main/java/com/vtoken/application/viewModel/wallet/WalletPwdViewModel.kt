package com.vtoken.application.viewModel.wallet

import android.content.Context
import androidx.databinding.ObservableField
import com.vtoken.application.viewModel.BaseViewModel
import generic.exceptions.DecryptException
import generic.exceptions.EncryptException
import generic.exceptions.InvalidatePasswordException
import generic.exceptions.NotMatchException
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

//beo
class WalletPwdViewModel(context: Context):BaseViewModel(context) {

    //f10396A
    var hasSetPwd = ObservableField<Boolean>(false)

    //f10397x
    var oldPwdString = ObservableField<String>()

    //f10398y
    var newPwdString = ObservableField<String>()

    //f10399z
    var newPwdRepeatString = ObservableField<String>()

    init {
        this.hasSetPwd.set(this.vCashCore.hasSetWalletPwd())
    }


    //mo39924a
    fun confirmChangeWalletPwd() {
        if (this.hasSetPwd.get()!!) {
            if (isStringEmpty(this.oldPwdString.get())) {
                showToast(getStringRescourcesByResName("toast_wallet_pwd_old"))
                return
            } else if (!this.vCashCore.checkMatchPassword(this.oldPwdString.get() as CharSequence)) {
                showToast(getStringRescourcesByResName("toast_wallet_pwd_old_error"))
                return
            }
        }
        if (isStringEmpty(this.newPwdString.get())) {
            showToast(getStringRescourcesByResName("toast_wallet_pwd_new"))
        }else if(!checkWalletPwdFormat(this.newPwdString.get())){
            showToast(getStringRescourcesByResName("toast_wallet_error_format"))
        } else if (isStringEmpty(this.newPwdRepeatString.get())) {
            showToast(getStringRescourcesByResName("toast_wallet_pwd_confirm"))
        } else if (this.newPwdString.get() as String != this.newPwdRepeatString.get()) {
            showToast(getStringRescourcesByResName("toast_wallet_pwd_two_key_differ"))
        } else {
            showLoadingFragment(getStringRescourcesByResName("splash_load_info"))
            Observable.create(object : ObservableOnSubscribe<String> {
                override fun subscribe(observableEmitter: ObservableEmitter<String>) {
                    var str: String
                    val str2 = "success"
                    try {
                        this@WalletPwdViewModel.vCashCore.changeWalletPassword(
                            this@WalletPwdViewModel.oldPwdString.get() as CharSequence,
                            this@WalletPwdViewModel.newPwdString.get() as CharSequence
                        )
                        str = str2
                    } catch (e: InvalidatePasswordException) {
                        str = "fail"
                        e.printStackTrace()
                    } catch (e2: NotMatchException) {
                        str = "fail"
                        e2.printStackTrace()
                    } catch (e3: DecryptException) {
                        str = "fail"
                        e3.printStackTrace()
                    } catch (e4: EncryptException) {
                        str = "fail"
                        e4.printStackTrace()
                    } catch (e5: Exception) {
                        str = "fail"
                        e5.printStackTrace()
                    }

                    observableEmitter.onNext(str)
                }
            }).subscribeOn(Schedulers.io()).compose(bindDestoryEvent()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {}

                    override fun onSubscribe(disposable: Disposable) {}

                    /* renamed from: a */
                    override fun onNext(str: String) {
                        this@WalletPwdViewModel.dismissLoadingDialog()
                        if (str == "success") {
                            this@WalletPwdViewModel.showToast(this@WalletPwdViewModel.getStringRescourcesByResName("edit_success"))
                            this@WalletPwdViewModel.finish()
                            return
                        }
                        this@WalletPwdViewModel.showToast(this@WalletPwdViewModel.getStringRescourcesByResName("fail_save"))
                    }

                    override fun onError(th: Throwable) {
                        this@WalletPwdViewModel.dismissLoadingDialog()
                    }
                })
        }
    }
}