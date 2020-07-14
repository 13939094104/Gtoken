package vdsMain.tool;

import bitcoin.UInt256;
import vdsMain.transaction.CHash256;

//bqu
public class Hash {
    public static UInt256 m10362a(byte[] bArr) {
        return m10363a(bArr, 0, bArr.length);
    }

    /* renamed from: a */
    public static UInt256 m10363a(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[1];
        UInt256 uInt256 = new UInt256();
        CHash256 jVar = new CHash256();
        if (bArr == null || i2 < 1) {
            jVar.writeAllBytes(bArr2);
        } else {
            jVar.mo44126a(bArr, i, i2);
        }
        jVar.Finalize(uInt256);
        return uInt256;
    }
}
