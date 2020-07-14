package com.vtoken.vdsecology.vcash.tools

import android.app.Application
import android.content.Context

/* renamed from: gl */
object ThreadToolkit {
    /* renamed from: a */
    fun getApplicationContext(context: Context): Application {
        return context as? Application ?: context.applicationContext as Application
    }
}