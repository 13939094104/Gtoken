package vdsMain;

import bitcoin.CKey;
import com.vc.libcommon.exception.AddressFormatException;

public class WitnessScriptPrivKey extends CKey {
    public WitnessScriptPrivKey() {
    }

    public WitnessScriptPrivKey(byte[] bArr) {
        super(bArr);
    }

    public WitnessScriptPrivKey(CKey cKey) {
        super(cKey);
    }

    /* renamed from: e */
    public CPubkeyInterface mo9410e() {
        try {
            return new WitnessKey0HashScriptPubkey(super.getPubKey());
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: h */
    public PrivateKeyType getPivateKeyType() {
        return PrivateKeyType.WITNESS_V0_KEY_HASH_SCRIPT;
    }
}