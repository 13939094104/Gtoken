package vdsMain;

import bitcoin.CKey;
import com.vc.libcommon.exception.AddressFormatException;

public class CWitnessPrivKey extends CKey {
    public CWitnessPrivKey() {
    }

    public CWitnessPrivKey(byte[] bArr) {
        super(bArr);
    }

    public CWitnessPrivKey(CKey cKey) {
        super(cKey);
    }

    /* renamed from: e */
    public CPubkeyInterface mo9410e() {
        try {
            return new CWitnessKey0HashPubkey(super.getPubKey());
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: h */
    public PrivateKeyType getPivateKeyType() {
        return PrivateKeyType.WITNESS_V0_KEY_HASH;
    }
}