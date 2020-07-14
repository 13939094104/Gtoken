package zcash.crypto;

import net.bither.bitherj.utils.Utils;

public class CRIPEMD160 {

    /* renamed from: a */
    private int[] f4723a = new int[5];

    /* renamed from: b */
    private byte[] f4724b = new byte[64];

    /* renamed from: c */
    private long f4725c = 0;

    private native void Initialize(int[] iArr);

    private native void Transform(int[] iArr, byte[] bArr, int i);

    public CRIPEMD160() {
        Initialize(this.f4723a);
    }

    /* renamed from: a */
    public CRIPEMD160 mo40492a(byte[] bArr) {
        return mo40493a(bArr, bArr.length);
    }

    /* renamed from: a */
    public CRIPEMD160 mo40493a(byte[] bArr, int i) {
        int i2 = (int) (this.f4725c % 64);
        int i3 = 0;
        if (i2 > 0 && i2 + i >= 64) {
            int i4 = 64 - i2;
            System.arraycopy(bArr, 0, this.f4724b, i2, i4);
            this.f4725c += (long) i4;
            int i5 = i4 + 0;
            m4838a(this.f4723a, this.f4724b);
            i3 = i5;
            i2 = 0;
        }
        while (true) {
            int i6 = i3 + 64;
            if (i < i6) {
                break;
            }
            Transform(this.f4723a, bArr, i3);
            this.f4725c += 64;
            i3 = i6;
        }
        if (i > i3) {
            int i7 = i - i3;
            System.arraycopy(bArr, i3, this.f4724b, i2, i7);
            this.f4725c += (long) i7;
        }
        return this;
    }

    /* renamed from: b */
    public CRIPEMD160 mo40494b(byte[] bArr) {
        if (bArr.length == 20) {
            byte[] bArr2 = new byte[64];
            bArr2[0] = Byte.MIN_VALUE;
            byte[] bArr3 = new byte[8];
            Utils.m3458d(this.f4725c << 3, bArr3, 0);
            mo40493a(bArr2, (int) (((119 - (this.f4725c % 64)) % 64) + 1));
            mo40493a(bArr3, 8);
            Utils.uint32ToByteArrayLE((long) this.f4723a[0], bArr, 0);
            Utils.uint32ToByteArrayLE((long) this.f4723a[1], bArr, 4);
            Utils.uint32ToByteArrayLE((long) this.f4723a[2], bArr, 8);
            Utils.uint32ToByteArrayLE((long) this.f4723a[3], bArr, 12);
            Utils.uint32ToByteArrayLE((long) this.f4723a[4], bArr, 16);
            return this;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hash length ");
        sb.append(bArr.length);
        sb.append(" must be ");
        sb.append(20);
        throw new IllegalArgumentException(sb.toString());
    }

    /* renamed from: a */
    private void m4838a(int[] iArr, byte[] bArr) {
        Transform(iArr, bArr, 0);
    }
}
