package bitcoin.script;

import bitcoin.BaseBlob;
import bitcoin.CSHA256;
import vdsMain.CMultisigPubkey;
import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;
import vdsMain.CTxDestinationUInt256;
import vdsMain.transaction.CScriptID;

public class WitnessV0ScriptHash extends CTxDestinationUInt256 {
    public WitnessV0ScriptHash() {
    }

    public WitnessV0ScriptHash(byte[] bArr) {
        super(bArr);
    }

    public WitnessV0ScriptHash(BaseBlob baseBlob) {
        if (baseBlob instanceof WitnessV0ScriptHash) {
            set(baseBlob);
        }
    }

    /* renamed from: d */
    public CScriptID mo9570d() {
        return new CScriptID(CScript.m484a((CTxDestination) this));
    }

    public WitnessV0ScriptHash(CScript cScript) {
        if (cScript != null) {
            new CSHA256().mo9459a(cScript.copyToNewBytes()).mo9463b(this.mData);
            updateHash();
        }
    }

    /* renamed from: a */
    public static WitnessV0ScriptHash m546a(byte[] bArr) {
        WitnessV0ScriptHash witnessV0ScriptHash = new WitnessV0ScriptHash();
        new CSHA256().mo9459a(bArr).mo9463b(witnessV0ScriptHash.mData);
        witnessV0ScriptHash.updateHash();
        return witnessV0ScriptHash;
    }

    /* renamed from: a */
    public static WitnessV0ScriptHash m545a(CMultisigPubkey nVar) {
        if (nVar.getTypeLength() < 23) {
            return null;
        }
        return new WitnessV0ScriptHash(nVar.mo9451j());
    }

    public boolean equals(Object obj) {
        if (obj instanceof WitnessV0ScriptHash) {
            return super.equals(obj);
        }
        return false;
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.WITNESS_V0_SCRIPT_HASH;
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new WitnessV0ScriptHash((BaseBlob) this);
    }
}
