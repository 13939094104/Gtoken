package bitcoin.script;

import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;
import vdsMain.CTxDestinationUInt160;
import vdsMain.transaction.CScriptID;
import vdsMain.transaction.Script;

import java.util.Locale;

public class WitnessV0KeyHash extends CTxDestinationUInt160 {
    public WitnessV0KeyHash() {
    }

    public WitnessV0KeyHash(byte[] bArr) {
        super(bArr);
    }

    public WitnessV0KeyHash(CTxDestination cTxDestination) {
        if (cTxDestination != null) {
            CTxDestinationType a = cTxDestination.getCTxDestinationType();
            if (a == CTxDestinationType.KEYID || a == CTxDestinationType.WITNESS_V0_KEY_HASH) {
                set(cTxDestination.data());
                return;
            }
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Type of key %s must be %s", new Object[]{cTxDestination.getCTxDestinationType().name(), CTxDestinationType.KEYID}));
        }
    }

    /* renamed from: d */
    public CScriptID mo9569d() {
        return new CScriptID(Script.m10760a(this));
    }

    public boolean equals(Object obj) {
        if (obj instanceof WitnessV0KeyHash) {
            return super.equals(obj);
        }
        return false;
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.WITNESS_V0_KEY_HASH;
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new WitnessV0KeyHash((CTxDestination) this);
    }
}

