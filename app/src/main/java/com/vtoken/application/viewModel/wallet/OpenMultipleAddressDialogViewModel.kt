package com.vtoken.application.viewModel.wallet

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vtoken.application.ApplicationLoader
import com.vtoken.application.viewModel.BaseViewModel
import vdsMain.tool.SharedPreferencesUtil

class OpenMultipleAddressDialogViewModel(context: Context,val parent:BaseViewModel): BaseViewModel(context) {

    val walletPwd = ObservableField("")
    val tipString = ObservableField("")
    fun confirm(){
        if(!this.vCashCore.checkMatchPassword(this.walletPwd.get() as CharSequence)){
            tipString.set(getStringRescourcesByResName("wallet_pwd_error"))
            return
        }
        vCashCore.fixWalletFullPublicKey(walletPwd.get()!!)
        SharedPreferencesUtil.getSharedPreferencesUtil().putBooleanValue("needFixFullPub" + vCashCore.getAppWalletPath(),false, ApplicationLoader.applicationContext)
        LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(Intent("refresh_address"))
        parent.dismissConfirmDialog()
    }
}