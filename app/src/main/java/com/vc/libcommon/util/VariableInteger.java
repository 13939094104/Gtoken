package com.vc.libcommon.util;

public class VariableInteger {

    /* renamed from: a */
    private long f676a = 0;

    private native byte[] encodeNative(long j);

    public VariableInteger() {
    }

    public VariableInteger(int i) {
        this.f676a = (long) i;
    }

    /* renamed from: a */
    public byte[] mo18948a() {
        return encodeNative(this.f676a);
    }
}