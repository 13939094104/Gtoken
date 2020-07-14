package zcash;

public class RandomBytes {
    private static native void nativeRandomBuf(byte[] bArr, int i);

    //m4835a
    public static void nativeRandomBufDefaultLength(byte[] bArr) {
        nativeRandomBuf(bArr, bArr.length);
    }

    /* renamed from: a */
    public static void m4836a(byte[] bArr, int i) {
        nativeRandomBuf(bArr, i);
    }
}
