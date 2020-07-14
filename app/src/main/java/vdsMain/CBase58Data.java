package vdsMain;

import bitcoin.UInt256;
import generic.crypto.Base58;
import vdsMain.tool.Hash;

//bqs
public class CBase58Data {

    /* renamed from: a */
    protected byte[] f12120a;

    /* renamed from: b */
    protected byte[] f12121b;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42885a(byte[] bArr, byte[] bArr2, int i) {
        this.f12120a = DataTypeToolkit.copyPartBytes(bArr, 0, bArr.length);
        if (bArr2 != null) {
            this.f12121b = DataTypeToolkit.copyPartBytes(bArr2, 0, i);
        } else {
            this.f12121b = null;
        }
    }

    public String toString() {
        byte[] a = DataTypeToolkit.mergeArr(this.f12120a, this.f12121b);
        UInt256 a2 = Hash.m10362a(a);
        byte[] bArr = new byte[(a.length + 4)];
        System.arraycopy(a, 0, bArr, 0, a.length);
        System.arraycopy(a2.data(), 0, bArr, a.length, 4);
        return Base58.encodeToString(bArr);
    }

    /* renamed from: a */
    public int mo42884a(CBase58Data bqs) {
        int d = DataTypeToolkit.m11511d(this.f12120a, bqs.f12120a);
        if (d < 0) {
            return -1;
        }
        if (d > 0) {
            return 1;
        }
        int d2 = DataTypeToolkit.m11511d(this.f12121b, bqs.f12121b);
        if (d2 < 0) {
            return -1;
        }
        if (d2 > 0) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof CBase58Data)) {
            return false;
        }
        if (mo42884a((CBase58Data) obj) == 0) {
            z = true;
        }
        return z;
    }
}
