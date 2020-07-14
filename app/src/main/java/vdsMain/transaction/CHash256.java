package vdsMain.transaction;

import bitcoin.CSHA256;
import bitcoin.UInt256;

public class CHash256 {

    //f13028a
    public static int bytesLength = 32;

    //f13029b
    private CSHA256 csha256 = new CSHA256();

    //mo44127a
    public void Finalize(UInt256 hash) {
        byte[] buf = new byte[hash.width()];
        mo44128a(buf);
        hash.setData(buf);
    }

    /* renamed from: a */
    public void mo44128a(byte[] bArr) {
        if (bArr.length == bytesLength) {
            byte[] bArr2 = new byte[32];
            this.csha256.mo9463b(bArr2);
            this.csha256.Reset().writeBytes(bArr2, 32).mo9463b(bArr);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("hash buffer length ");
        sb.append(bArr.length);
        sb.append(" must be ");
        sb.append(bytesLength);
        throw new IllegalArgumentException(sb.toString());
    }

    //mo44129b
    public CHash256 writeAllBytes(byte[] bArr) {
        this.csha256.writeBytes(bArr, bArr.length);
        return this;
    }

    /* renamed from: a */
    public CHash256 mo44126a(byte[] bArr, int start, int length) {
        this.csha256.Write(bArr, start, length);
        return this;
    }
}
