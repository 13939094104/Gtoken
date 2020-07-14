package com.vc.libcommon.util;

public class UInt64 extends UnsignedNumber {

    //f674a
    protected long value;

    public static native String UInt64DecimalString(long j);

    public static native long addUInt64WithUInt64(long j, long j2);

    public static native long fromBytesNative(byte[] bArr);

    public static native long multiplyUInt64WithUInt64(long j, long j2);

    public static native long substractUInt64WithUInt64(long j, long j2);

    public int length() {
        return 8;
    }

    public UInt64() {
    }

    public UInt64(int i) {
        this.value = UInt.m851a(i);
    }

    public UInt64(long j) {
        this.value = j;
    }

    //mo18936a
    public UInt64 add(long j) {
        this.value = addUInt64WithUInt64(this.value, j);
        return this;
    }

    /* renamed from: a */
    public UInt64 mo18937a(UInt64 uInt64) {
        this.value = addUInt64WithUInt64(this.value, uInt64.value);
        return this;
    }

    /* renamed from: b */
    public UInt64 mo18940b(UInt64 uInt64) {
        this.value = substractUInt64WithUInt64(this.value, uInt64.value);
        return this;
    }

    /* renamed from: b */
    public UInt64 mo18939b(long j) {
        this.value = multiplyUInt64WithUInt64(this.value, j);
        return this;
    }

    //mo18938b
    //910 mo18968b
    public long getValue() {
        return this.value;
    }

    /* renamed from: c */
    public static UInt64 m856c() {
        return new UInt64(-1);
    }

    public String toString() {
        return UInt64DecimalString(this.value);
    }

    /* renamed from: a */
    public long mo18928a() {
        return this.value;
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        super.mo18929a(bArr);
        this.value = fromBytesNative(bArr);
    }
}
