package com.vtoken.application.viewModel

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.text.TextUtils
import com.vtoken.application.view.activity.ValidatePwdActivity

class ValidatePwdViewModel(context: Context):BaseViewModel(context) {

    var f9705B = ObservableField<String>()

    var skipCheck=false

    //f9713x
    var pwdInputString = ObservableField<String>()

    var f9707D = ObservableInt(4)

    var errorString=ObservableField<String>("")

    var f9710G = getStringRescourcesByResName("cancel")

    init {
        this.f9705B.set(this.f9710G)
        skipCheck=intent.getBooleanExtra("skipCheck",false)
    }

    //mo39929b
    fun confirm() {
        if (TextUtils.isEmpty(this.pwdInputString.get())) {
            showToast(getStringRescourcesByResName("toast_input_wallet_pwd"))
            return
        }

        if(skipCheck){
            val intent = Intent()
            intent.putExtra("pwd", this.pwdInputString.get() as String)
            (this.context as ValidatePwdActivity).setResult(-1, intent)
            finish()
            return
        }
        if (this.vCashCore.checkMatchPassword(this.pwdInputString.get() as CharSequence)) {
            val intent = Intent()
            intent.putExtra("pwd", this.pwdInputString.get() as String)
            (this.context as ValidatePwdActivity).setResult(-1, intent)
            finish()
            return
        }else{
            errorString.set(getStringRescourcesByResName("wallet_pwd_error"))
            mainHandler.postDelayed(object:Runnable{
                override fun run() {
                    errorString.set("")
                }
            },1000)
        }
        this.f9707D.set(0)
    }

    //mo39911N
    fun cancel() {
        (this.context as ValidatePwdActivity).setResult(0)
        finish()
    }
}