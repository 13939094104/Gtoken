package com.vtoken.vdsecology.vcash.tools

object NativeLibLoader {
    //915 m12148a
    fun loadLibrary() {
        System.loadLibrary("crypto_1_1")
        System.loadLibrary("ssl_1_1")
        System.loadLibrary("gmp")
        System.loadLibrary("univalue")
        System.loadLibrary("vccommon")
        System.loadLibrary("vccore")
    }
}