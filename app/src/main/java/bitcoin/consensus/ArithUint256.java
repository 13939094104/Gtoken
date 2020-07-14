package bitcoin.consensus;

import bitcoin.UInt256;
import net.bither.bitherj.utils.Utils;

import java.util.Arrays;

public class ArithUint256 {

    /* renamed from: a */
    protected int[] f412a;

    private native boolean[] SetCompactNative(long j, int[] iArr);

    private native void addEqualsNative(int[] iArr, int[] iArr2);

    private native boolean biggertIntEqualsNative(int[] iArr, long j);

    private native boolean biggertThanNative(int[] iArr, int[] iArr2);

    private native void devideEqualsNative(int[] iArr, int[] iArr2);

    private native boolean equalsUInt64Native(long j, int[] iArr);

    public ArithUint256() {
        this.f412a = new int[8];
    }

    public ArithUint256(long j) {
        this.f412a = new int[8];
        mo9501b(j);
    }

    public ArithUint256(int[] iArr, boolean z) {
        if (z) {
            this.f412a = iArr;
            return;
        }
        this.f412a = new int[8];
        System.arraycopy(iArr, 0, this.f412a, 0, iArr.length);
    }

    /* renamed from: a */
    public boolean[] mo9498a(long j) {
        return SetCompactNative(j, this.f412a);
    }

    /* renamed from: a */
    public static ArithUint256 m463a(UInt256 uInt256) {
        ArithUint256 arithUint256 = new ArithUint256();
        int i = 0;
        while (true) {
            int[] iArr = arithUint256.f412a;
            if (i >= iArr.length) {
                return arithUint256;
            }
            iArr[i] = (int) Utils.m3444a(uInt256.data(), i * 4);
            i++;
        }
    }

    //mo9496a
    public void copySetNChainWork(ArithUint256 arithUint256) {
        if (arithUint256 != this) {
            int[] iArr = arithUint256.f412a;
            int[] iArr2 = this.f412a;
            System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
        }
    }

    /* renamed from: b */
    public void mo9501b(long j) {
        int[] iArr = this.f412a;
        iArr[0] = ((int) j) & -1;
        iArr[1] = ((int) (j >> 32)) & -1;
        for (int i = 2; i < 8; i++) {
            this.f412a[i] = 0;
        }
    }

    /* renamed from: b */
    public void mo9502b(UInt256 uInt256) {
        byte[] data = uInt256.data();
        int i = 0;
        int i2 = 0;
        while (i < data.length) {
            int[] iArr = this.f412a;
            if (i2 >= iArr.length) {
                break;
            }
            iArr[i2] = (int) Utils.m3444a(data, i);
            i += 4;
            i2++;
        }
        while (true) {
            int[] iArr2 = this.f412a;
            if (i2 < iArr2.length) {
                int i3 = i2 + 1;
                iArr2[i2] = 0;
                i2 = i3;
            } else {
                return;
            }
        }
    }

    /* renamed from: a */
    public UInt256 mo9494a() {
        UInt256 uInt256 = new UInt256();
        byte[] data = uInt256.data();
        int i = 0;
        int i2 = 0;
        while (true) {
            int[] iArr = this.f412a;
            if (i >= iArr.length) {
                return uInt256;
            }
            Utils.uint32ToByteArrayLE((long) iArr[i], data, i2);
            i++;
            i2 += 4;
        }
    }

    /* renamed from: a */
    public void mo9497a(String str) {
        mo9502b(new UInt256(str));
    }

    public String toString() {
        return mo9500b();
    }

    /* renamed from: b */
    public String mo9500b() {
        return mo9494a().getHex();
    }

    /* renamed from: c */
    public ArithUint256 clone() {
        return mo9495a(this, false);
    }

    /* renamed from: a */
    public ArithUint256 mo9495a(ArithUint256 arithUint256, boolean z) {
        return new ArithUint256(arithUint256.f412a, z);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ArithUint256) {
            return Arrays.equals(((ArithUint256) obj).f412a, this.f412a);
        }
        if (obj instanceof Byte) {
            return m464d((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return m464d((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Integer) {
            return m464d((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Long) {
            return m464d(((Long) obj).longValue());
        }
        return false;
    }

    /* renamed from: b */
    public ArithUint256 mo9499b(ArithUint256 arithUint256) {
        return clone().mo9504c(arithUint256);
    }

    /* renamed from: c */
    public ArithUint256 mo9504c(ArithUint256 arithUint256) {
        addEqualsNative(this.f412a, arithUint256.f412a);
        return this;
    }

    /* renamed from: d */
    public ArithUint256 mo9507d(ArithUint256 arithUint256) {
        devideEqualsNative(this.f412a, arithUint256.f412a);
        return this;
    }

    /* renamed from: e */
    public ArithUint256 mo9509e(ArithUint256 arithUint256) {
        return clone().mo9507d(arithUint256);
    }

    /* renamed from: d */
    private boolean m464d(long j) {
        return equalsUInt64Native(j, this.f412a);
    }

    /* renamed from: d */
    public int[] mo9508d() {
        return this.f412a;
    }

    /* renamed from: f */
    public boolean mo9511f(ArithUint256 arithUint256) {
        return biggertThanNative(this.f412a, arithUint256.f412a);
    }

    /* renamed from: c */
    public boolean mo9505c(long j) {
        return biggertIntEqualsNative(this.f412a, j);
    }
}
