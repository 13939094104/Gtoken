package vdsMain;

import bitcoin.CSHA256;
import bitcoin.UInt160;
import zcash.crypto.CRIPEMD160;

/* renamed from: i */
public class CHash160 {

    //f12876a
    private CSHA256 csha256 = new CSHA256();

    /* renamed from: a */
    public void mo43939a(byte[] bArr) {
        if (bArr.length == 20) {
            byte[] bArr2 = new byte[32];
            this.csha256.mo9463b(bArr2);
            CRIPEMD160 cripemd160 = new CRIPEMD160();
            CSHA256 csha256 = this.csha256;
            cripemd160.mo40493a(bArr2, 32).mo40494b(bArr);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hash length ");
        sb.append(bArr.length);
        sb.append(" must be ");
        sb.append(20);
        throw new IllegalArgumentException(sb.toString());
    }

    //mo43938a
    public CHash160 writeBytes(byte[] bArr, int i) {
        this.csha256.writeBytes(bArr, i);
        return this;
    }

    //m12109b
    public static UInt160 encodeToUInt160(byte[] bArr) {
        byte[] bArr2 = new byte[1];
        UInt160 uInt160 = new UInt160();
        CHash160 cHash160 = new CHash160();
        if (bArr.length == 0) {
            cHash160.writeBytes(bArr2, 1);
        } else {
            cHash160.writeBytes(bArr, bArr.length);
        }
        cHash160.mo43939a(uInt160.data());
        uInt160.updateHash();
        return uInt160;
    }
}
