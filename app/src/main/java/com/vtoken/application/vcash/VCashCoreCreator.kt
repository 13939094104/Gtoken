package com.vtoken.vdsecology.vcash

import android.content.Context

object VCashCoreCreator {
    /* renamed from: a */
    fun getVCashCore(context: Context): VCashCore {
        return VCashCoreImpl(context)
    }
}