package com.vtoken.application.viewModel.wallet

import android.content.Context
import androidx.databinding.ObservableField
import com.vtoken.application.viewModel.BaseViewModel

//bhf
class AddressEmptyViewModel(context: Context):BaseViewModel(context) {
    var f11494x = ObservableField<String>()

    /* renamed from: L */
    override fun mo41316L(): Boolean {
        return true
    }

    /* renamed from: a */
    fun mo42238a(str: String) {
        this.f11494x.set(str)
    }
}