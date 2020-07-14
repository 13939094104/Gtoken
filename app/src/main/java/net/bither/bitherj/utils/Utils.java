package net.bither.bitherj.utils;

import com.google.common.primitives.UnsignedBytes;
import vdsMain.StringToolkit;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class Utils {

    /* renamed from: a */
    private static final int[] f2126a = {1, 2, 4, 8, 16, 32, 64, 128};

    public static native byte[] sha256hash160(byte[] bArr);

    /* renamed from: a */
    public static void m3446a(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) ((i >> 8) & 255);
        bArr[i2 + 1] = (byte) (i & 255);
    }

    /* renamed from: b */
    public static void m3453b(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i & 255);
        bArr[i2 + 1] = (byte) ((i >> 8) & 255);
    }

    //m3448a
    public static void uint32ToByteArrayBE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & (val >> 24));
        out[offset + 1] = (byte) (0xFF & (val >> 16));
        out[offset + 2] = (byte) (0xFF & (val >> 8));
        out[offset + 3] = (byte) (0xFF & (val));
    }

    //m3454b
    public static void uint32ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & (val));
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
    }

    /* renamed from: c */
    public static void m3457c(long j, byte[] bArr, int i) {
        bArr[i + 7] = (byte) ((int) (j & 255));
        bArr[i + 6] = (byte) ((int) ((j >> 8) & 255));
        bArr[i + 5] = (byte) ((int) ((j >> 16) & 255));
        bArr[i + 4] = (byte) ((int) ((j >> 24) & 255));
        bArr[i + 3] = (byte) ((int) ((j >> 32) & 255));
        bArr[i + 2] = (byte) ((int) ((j >> 40) & 255));
        bArr[i + 1] = (byte) ((int) ((j >> 48) & 255));
        bArr[i] = (byte) ((int) ((j >> 56) & 255));
    }

    /* renamed from: d */
    public static void m3458d(long j, byte[] bArr, int i) {
        bArr[i] = (byte) ((int) (j & 255));
        bArr[i + 1] = (byte) ((int) ((j >> 8) & 255));
        bArr[i + 2] = (byte) ((int) ((j >> 16) & 255));
        bArr[i + 3] = (byte) ((int) ((j >> 24) & 255));
        bArr[i + 4] = (byte) ((int) ((j >> 32) & 255));
        bArr[i + 5] = (byte) ((int) ((j >> 40) & 255));
        bArr[i + 6] = (byte) ((int) ((j >> 48) & 255));
        bArr[i + 7] = (byte) ((int) ((j >> 56) & 255));
    }

    //m3447a
    public static void writeLong(long j, OutputStream outputStream) throws IOException {
        outputStream.write((int) (j & 255));
        outputStream.write((int) ((j >> 8) & 255));
        outputStream.write((int) ((j >> 16) & 255));
        outputStream.write((int) ((j >> 24) & 255));
    }

    /* renamed from: a */
    public static byte[] m3449a(long j) {
        return new byte[]{(byte) ((int) (j & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 24) & 255)), (byte) ((int) ((j >> 32) & 255)), (byte) ((int) ((j >> 40) & 255)), (byte) ((int) ((j >> 48) & 255)), (byte) ((int) ((j >> 56) & 255))};
    }

    /* renamed from: a */
    public static byte[] m3451a(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length <= 8) {
            byte[] b = reverseByteArr(byteArray);
            if (b.length == 8) {
                return b;
            }
            byte[] bArr = new byte[8];
            System.arraycopy(b, 0, bArr, 0, b.length);
            return bArr;
        }
        throw new RuntimeException("Input too large to encode into a uint64");
    }

    /* renamed from: a */
    public static String m3445a(byte[] bArr) {
        return bArr == null ? "" : StringToolkit.bytesToString(reverseByteArr(bArr));
    }

    //m3450a
    public static byte[] getReverseStringBytes(String str) {
        return reverseByteArr(StringToolkit.getBytes(str));
    }

    //m3455b
    public static byte[] reverseByteArr(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[(bArr.length - 1) - i];
        }
        return bArr2;
    }

    /* renamed from: a */
    public static long m3444a(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((((long) bArr[i3 + 1]) & 255) << 24) | (((long) bArr[i]) & 255) | ((((long) bArr[i2]) & 255) << 8) | ((((long) bArr[i3]) & 255) << 16);
    }

    /* renamed from: b */
    public static long m3452b(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        int i7 = i6 + 1;
        return ((((long) bArr[i7 + 1]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i2]) & 255) << 8) | ((((long) bArr[i3]) & 255) << 16) | ((((long) bArr[i4]) & 255) << 24) | ((((long) bArr[i5]) & 255) << 32) | ((((long) bArr[i6]) & 255) << 40) | ((((long) bArr[i7]) & 255) << 48);
    }

    /* renamed from: c */
    public static int m3456c(byte[] bArr, int i) {
        return ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | (bArr[i] & UnsignedBytes.MAX_VALUE);
    }

    /* renamed from: d */
    public static byte[] m3459d(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, Math.min(i, bArr.length));
        return bArr2;
    }

    /* renamed from: e */
    public static boolean m3460e(byte[] bArr, int nIndex) {
        return (bArr[nIndex >>> 3] & f2126a[nIndex & 7]) != 0;
    }

    /* renamed from: f */
    public static void m3461f(byte[] bArr, int nIndex) {
        int i2 = nIndex >>> 3;
        bArr[i2] = (byte) (f2126a[nIndex & 7] | bArr[i2]);
    }

    //m3443a
    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}
