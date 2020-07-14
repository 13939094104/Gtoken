package vdsMain;


import bitcoin.BaseBlob;
import bitcoin.UInt256;
import zcash.RustZCash;

//brz
public class SaplingIncomingViewingKey extends UInt256 {
    public SaplingIncomingViewingKey() {
    }

    public SaplingIncomingViewingKey(BaseBlob baseBlob) {
        super(baseBlob);
    }

    /* renamed from: a */
    public SaplingPaymentAddress mo42994a(DiversifierT diversifierT) {
        UInt256 uInt256 = new UInt256();
        if (!RustZCash.check_diversifier(diversifierT.mo42954a())) {
            return null;
        }
        RustZCash.ivk_to_pkd(data(), diversifierT.mo42954a(), uInt256.data());
        return new SaplingPaymentAddress(diversifierT, uInt256);
    }

    /* renamed from: a */
    public SaplingIncomingViewingKey clone() {
        return new SaplingIncomingViewingKey(this);
    }
}
