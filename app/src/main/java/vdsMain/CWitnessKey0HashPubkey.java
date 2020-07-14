package vdsMain;

import bitcoin.CPubKey;
import bitcoin.script.WitnessV0KeyHash;
import com.vc.libcommon.exception.AddressFormatException;

public class CWitnessKey0HashPubkey extends CPubKey {
    public CWitnessKey0HashPubkey() {
    }

    public CWitnessKey0HashPubkey(CPubkeyInterface knVar) throws AddressFormatException {
        super(knVar);
        if (!m14063p()) {
            throw new AddressFormatException("Witness key0 hash pubkey must be compressed.");
        }
    }

    /* renamed from: p */
    private boolean m14063p() {
        int c = getTypeLength();
        return c >= 20 || c <= 33;
    }

    /* renamed from: g */
    public PubkeyType mo210g() {
        return PubkeyType.WITNESS_V0_KEY_HASH;
    }

    /* renamed from: h */
    public CTxDestination getCKeyID() {
        if (m14063p()) {
            return new WitnessV0KeyHash(super.getCKeyID().data());
        }
        throw new IllegalArgumentException("Witness key0 hash pubkey must be compressed.");
    }
}