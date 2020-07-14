package com.vc.libcommon.util;

import org.spongycastle.asn1.cmc.BodyPartID;

import java.util.Locale;

public class UInt extends UnsignedNumber {

    /* renamed from: b */
    public static final UInt f672b = new UInt(4294967295L);

    protected int f673a = 0;

    /* renamed from: a */
    public static long m851a(int i) {
        return ((long) i) & BodyPartID.bodyIdMax;
    }

    public static native long fromBytesNative(byte[] bArr);

    public int length() {
        return 4;
    }

    public UInt() {
    }

    public UInt(int i) {
        this.f673a = i;
    }

    public UInt(long j) {
        this.f673a = (int) j;
    }

    /* renamed from: b */
    public int mo18933b() {
        return this.f673a;
    }

    /* renamed from: c */
    public long mo18934c() {
        return m851a(this.f673a);
    }

    /* renamed from: a */
    public long mo18928a() {
        return m851a(this.f673a);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%d", new Object[]{Long.valueOf(mo18934c())});
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        super.mo18929a(bArr);
        this.f673a = (int) fromBytesNative(bArr);
    }
}