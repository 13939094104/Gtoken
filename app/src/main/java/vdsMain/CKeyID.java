package vdsMain;

import bitcoin.BaseBlob;
import bitcoin.UInt160;
import bitcoin.script.WitnessV0KeyHash;

public class CKeyID extends CTxDestinationUInt160 {
    public CKeyID() {
    }

    public CKeyID(UInt160 uInt160) {
        super((BaseBlob) uInt160);
        if (uInt160 instanceof WitnessV0KeyHash) {
            updateHash();
        }
    }

    public CKeyID(byte[] bArr) {
        super(bArr);
    }

    public CKeyID(byte[] bArr, int i) {
        super(bArr, i);
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.KEYID;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CKeyID) {
            return super.equals(obj);
        }
        return false;
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new CKeyID((UInt160) this);
    }
}