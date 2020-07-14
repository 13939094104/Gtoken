package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import zcash.RustZCash;

//brs
public class PedersenHash extends UInt256 {
    public PedersenHash() {
    }

    public PedersenHash(BaseBlob baseBlob) {
        super(baseBlob);
    }

    /* renamed from: a */
    public static PedersenHash m10448a(PedersenHash brs, PedersenHash brs2, long j) {
        PedersenHash brs3 = new PedersenHash();
        RustZCash.merkle_hash(j, brs.data(), brs2.data(), brs3.data());
        return brs3;
    }

    /* renamed from: a */
    public static PedersenHash m10447a() {
        PedersenHash brs = new PedersenHash();
        RustZCash.tree_uncommitted(brs.data());
        return brs;
    }
}