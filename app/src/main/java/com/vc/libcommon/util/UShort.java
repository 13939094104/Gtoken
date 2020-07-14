package com.vc.libcommon.util;

import java.util.Locale;

public class UShort extends UnsignedNumber {

    /* renamed from: a */
    protected short f675a;

    /* renamed from: a */
    public static int m864a(short s) {
        return s & 65535;
    }

    public static native int fromBytesNative(byte[] bArr);

    public int length() {
        return 2;
    }

    /* renamed from: b */
    public int mo18942b() {
        return m864a(this.f675a);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%d", new Object[]{java.lang.Integer.valueOf(mo18942b())});
    }

    /* renamed from: a */
    public long mo18928a() {
        return (long) m864a(this.f675a);
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        super.mo18929a(bArr);
        this.f675a = (short) fromBytesNative(bArr);
    }
}