package bitcoin;

import java.util.Arrays;
import net.bither.bitherj.utils.Utils;

public class CSHA256 {

    //f402a
    private int[] s = new int[8];

    //f403b
    private byte[] buf = new byte[64];

    //f404c
    private long bytes = 0;

    private native void Initialize(int[] iArr);

    private static native void Transform3(int[] iArr, byte[] bArr, int i, int i2);

    public CSHA256() {
        Initialize(this.s);
    }

    /* renamed from: a */
    public CSHA256 mo9459a(byte[] bArr) {
        return writeBytes(bArr, bArr.length);
    }

    //mo9460a
    public CSHA256 writeBytes(byte[] bArr, int i) {
        return Write(bArr, 0, i);
    }

    //mo9461a
    public CSHA256 Write(byte[] bArr, int start, int length) {
        int bufsize = (int) (this.bytes % 64);
        int i4 = 0;
        if (bufsize <= 0 || bufsize + length < 64) {
            i4 = bufsize;
        } else {
            int i5 = 64 - bufsize;
            System.arraycopy(bArr, start, this.buf, bufsize, i5);
            this.bytes += (long) i5;
            start += i5;
            Transform3(this.s, this.buf, 0, 1);
        }
        int i6 = length - start;
        if (i6 >= 64) {
            int i7 = i6 / 64;
            Transform3(this.s, bArr, start, i7);
            int i8 = i7 * 64;
            start += i8;
            this.bytes += (long) i8;
        }
        if (length > start) {
            int i9 = length - start;
            System.arraycopy(bArr, start, this.buf, i4, i9);
            this.bytes += (long) i9;
        }
        return this;
    }

    /* renamed from: b */
    public void mo9463b(byte[] bArr) {
        if (bArr.length >= 32) {
            byte[] bArr2 = new byte[64];
            bArr2[0] = Byte.MIN_VALUE;
            byte[] bArr3 = new byte[8];
            Utils.m3457c(this.bytes << 3, bArr3, 0);
            writeBytes(bArr2, (int) (((119 - (this.bytes % 64)) % 64) + 1));
            writeBytes(bArr3, 8);
            FinalizeNoPadding(bArr, false);
            return;
        }
        throw new IllegalArgumentException("Hash length must bigger than 32");
    }

    /* renamed from: c */
    public CSHA256 mo9464c(byte[] bArr) {
        return FinalizeNoPadding(bArr, true);
    }

    //mo9462a
    public CSHA256 FinalizeNoPadding(byte[] hash, boolean enforce_compression) {
        if (hash.length != 32) {
            throw new IllegalArgumentException("Hash length must be 32");
        } else if (!enforce_compression || this.bytes == 64) {
            Utils.uint32ToByteArrayBE((long) this.s[0], hash, 0);
            Utils.uint32ToByteArrayBE((long) this.s[1], hash, 4);
            Utils.uint32ToByteArrayBE((long) this.s[2], hash, 8);
            Utils.uint32ToByteArrayBE((long) this.s[3], hash, 12);
            Utils.uint32ToByteArrayBE((long) this.s[4], hash, 16);
            Utils.uint32ToByteArrayBE((long) this.s[5], hash, 20);
            Utils.uint32ToByteArrayBE((long) this.s[6], hash, 24);
            Utils.uint32ToByteArrayBE((long) this.s[7], hash, 28);
            return this;
        } else {
            throw new IllegalArgumentException("SHA256Compress should be invoked with a 512-bit block");
        }
    }

    //mo9458a
    public CSHA256 Reset() {
        this.bytes = 0;
        Initialize(this.s);
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSHA256)) {
            return false;
        }
        CSHA256 csha256 = (CSHA256) obj;
        return this.bytes == csha256.bytes && Arrays.equals(this.s, csha256.s) && Arrays.equals(this.buf, csha256.buf);
    }
}
