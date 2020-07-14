package vdsMain;

import bitcoin.CPubKey;
import bitcoin.script.WitnessV0KeyHash;
import com.vc.libcommon.exception.AddressFormatException;

public class WitnessKey0HashScriptPubkey extends CPubKey {
    public WitnessKey0HashScriptPubkey() {
    }

    public WitnessKey0HashScriptPubkey(CPubkeyInterface knVar) throws AddressFormatException {
        super(knVar);
        if (!m189p()) {
            throw new AddressFormatException("Witness key0 hash pubkey must be compressed.");
        }
    }

    /* renamed from: p */
    private boolean m189p() {
        int c = getTypeLength();
        return c >= 20 || c <= 33;
    }

    /* renamed from: g */
    public PubkeyType mo210g() {
        return PubkeyType.WITNESS_V0_KEY_HASH_SCRIPT;
    }

    /* renamed from: h */
    public CTxDestination getCKeyID() {
        if (m189p()) {
            return new WitnessV0KeyHash(super.getCKeyID().data()).mo9569d();
        }
        throw new IllegalArgumentException("Witness key0 hash pubkey must be compressed.");
    }
}
