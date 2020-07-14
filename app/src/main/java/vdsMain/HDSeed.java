package vdsMain;

import bitcoin.Random;

import java.util.Arrays;

//brq
public class HDSeed {
    /* renamed from: a */
    private byte[] f12180a;

    /* renamed from: b */
    private int f12181b;

    public HDSeed() {
    }

    public HDSeed(byte[] bArr, boolean... zArr) {
        mo42961a(bArr, zArr);
    }

    /* renamed from: a */
    public void mo42961a(byte[] bArr, boolean... zArr) {
        if (zArr.length == 0 || zArr[0]) {
            this.f12180a = DataTypeToolkit.bytesCopy(bArr);
        } else {
            this.f12180a = bArr;
        }
        mo42960a();
    }

    /* renamed from: a */
    public void mo42960a() {
        byte[] bArr = this.f12180a;
        if (bArr == null || bArr.length == 0) {
            this.f12181b = 0;
        } else {
            this.f12181b = Arrays.hashCode(bArr);
        }
    }

    /* renamed from: a */
    public static HDSeed m10442a(int... iArr) {
        int i = iArr.length != 0 ? iArr[0] : 32;
        if (i >= 32) {
            byte[] bArr = new byte[i];
            Random.m449a(bArr, i);
            return new HDSeed(bArr, false);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalidate seed len: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    /* renamed from: b */
    public boolean mo42962b() {
        byte[] bArr = this.f12180a;
        return bArr == null || bArr.length == 0;
    }

    /* renamed from: c */
    public byte[] mo42963c() {
        return this.f12180a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HDSeed)) {
            return false;
        }
        HDSeed HDSeed = (HDSeed) obj;
        if (this.f12181b != HDSeed.f12181b) {
            return false;
        }
        return Arrays.equals(this.f12180a, HDSeed.f12180a);
    }

    public int hashCode() {
        return this.f12181b;
    }
}
