package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt160;
import bitcoin.script.CScript;
import vdsMain.CHash160;
import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;
import vdsMain.CTxDestinationUInt160;

public class CScriptID extends CTxDestinationUInt160 {
    public CScriptID() {
    }

    public CScriptID(byte[] bArr) {
        super(bArr);
    }

    public CScriptID(CScript cScript) {
        super((BaseBlob) CHash160.encodeToUInt160(cScript.copyToNewBytes()));
    }

    public CScriptID(Script script) {
        super((BaseBlob) CHash160.encodeToUInt160(script.getScriptChunksBytes()));
    }

    public CScriptID(UInt160 uInt160) {
        super((BaseBlob) uInt160);
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.SCRIPTID;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CScriptID) {
            return super.equals(obj);
        }
        return false;
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new CScriptID((UInt160) this);
    }
}