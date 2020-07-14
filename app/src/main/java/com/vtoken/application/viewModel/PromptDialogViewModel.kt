package com.vtoken.application.viewModel

import android.content.Context
import androidx.databinding.ObservableInt
import android.text.Spanned

//bcp
//915 bek
class PromptDialogViewModel(context: Context):BaseViewModel(context){
    var f9643A = ObservableInt(0)

    //f9644B
    var sapnned: Spanned?=null

    /* renamed from: C */
    var f9645C = false


    //f9647y
    @JvmField
    var cancelString = getStringRescourcesByResName("cancel")

    //f9646x
    @JvmField
    var confirmString =getStringRescourcesByResName("confirm")

    //f9648z
    @JvmField
    var promptString = getStringRescourcesByResName("prompt")

    /* renamed from: L */
    override fun mo41316L(): Boolean {
        return true
    }

    /* renamed from: b */
    fun mo39929b(): String {
        return this.cancelString
    }


    //mo41505a
    fun setConfirmString(str: String) {
        this.confirmString = str
    }

    //mo41507r
    fun setCancelString(str: String) {
        this.cancelString = str
    }

    /* renamed from: N */
    fun mo39911N(): CharSequence {
        return this.sapnned!!
    }

    //mo41504a
    //915 mo41715a
    fun setSpanned(spanned: Spanned) {
        this.sapnned = spanned
    }

    /* renamed from: O */
    fun mo41502O(): Boolean {
        return this.f9645C
    }

    /* renamed from: b */
    fun mo41506b(z: Boolean) {
        this.f9645C = z
    }

    /* renamed from: P */
    fun mo41503P(): String {
        return this.promptString
    }

    //mo41508s
    fun setPromptString(str: String) {
        this.promptString = str
    }
}