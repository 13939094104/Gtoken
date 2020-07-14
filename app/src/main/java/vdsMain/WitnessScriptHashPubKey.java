package vdsMain;

import bitcoin.script.WitnessV0ScriptHash;
import vdsMain.transaction.CScriptID;

public class WitnessScriptHashPubKey extends CMultisigPubkey {
    /* renamed from: g */
    public PubkeyType mo210g() {
        return PubkeyType.WITNESS_SCRIPT_HASH;
    }

    /* renamed from: h */
    public CTxDestination getCKeyID() {
        if (this.byteArr == null || this.byteArr.length == 0) {
            return new CScriptID();
        }
        return new WitnessV0ScriptHash(mo9451j());
    }
}