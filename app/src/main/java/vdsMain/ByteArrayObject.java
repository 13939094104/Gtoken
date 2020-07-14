package vdsMain;

import java.util.Arrays;

public class ByteArrayObject {

    /* renamed from: a */
    public byte[] f12758a = null;

    public ByteArrayObject(byte[] bArr) {
        this.f12758a = bArr;
    }

    public boolean equals(Object obj) {
        byte[] bArr;
        if (obj instanceof ByteArrayObject) {
            bArr = ((ByteArrayObject) obj).f12758a;
        } else if (!(obj instanceof byte[])) {
            return false;
        } else {
            bArr = (byte[]) obj;
        }
        if (bArr == null) {
            return false;
        }
        return Arrays.equals(bArr, this.f12758a);
    }

    /* renamed from: a */
    public static int m11543a(byte[] bArr) {
        int i = 1;
        if (bArr == null) {
            return 1;
        }
        for (int length = bArr.length - 1; length >= 0; length--) {
            i = (i * 31) + bArr[length];
        }
        return i;
    }

    public int hashCode() {
        return m11543a(this.f12758a);
    }
}