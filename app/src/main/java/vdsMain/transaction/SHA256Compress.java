package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.CSHA256;
import bitcoin.UInt256;

//brb
public class SHA256Compress extends UInt256 {
    public SHA256Compress() {
    }

    public SHA256Compress(BaseBlob baseBlob) {
        super(baseBlob);
    }

    /* renamed from: a */
    public static SHA256Compress m10405a(SHA256Compress brb, SHA256Compress brb2, long j) {
        SHA256Compress brb3 = new SHA256Compress();
        CSHA256 csha256 = new CSHA256();
        csha256.mo9459a(brb.data());
        csha256.mo9459a(brb2.data());
        csha256.mo9464c(brb3.data());
        return brb3;
    }

    /* renamed from: a */
    public static SHA256Compress m10404a() {
        return new SHA256Compress();
    }
}
