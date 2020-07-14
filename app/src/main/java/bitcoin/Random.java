package bitcoin;

import zcash.RandomBytes;

public final class Random {
    public static native long GetRand(long j);

    /* renamed from: a */
    public static void m449a(byte[] bArr, int i) {
        RandomBytes.m4836a(bArr, i);
    }

    /* renamed from: a */
    public static long m448a(long j) {
        return GetRand(j);
    }
}

