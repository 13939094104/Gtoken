package com.vc.libcommon.util;

public class Size_t extends UnsignedNumber {

    /* renamed from: a */
    private long f671a = 0;

    public static native long fromBytesNative(byte[] bArr);

    public static native byte[] toCBytesNative(long j);

    private native String toStringNative(long j);

    public native int length();

    public Size_t(long j) {
        this.f671a = j;
    }

    public String toString() {
        return toStringNative(this.f671a);
    }

    /* renamed from: a */
    public long mo18928a() {
        return this.f671a;
    }

    /* renamed from: b */
    public byte[] mo18930b() {
        return toCBytesNative(this.f671a);
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        super.mo18929a(bArr);
        this.f671a = fromBytesNative(bArr);
    }
}