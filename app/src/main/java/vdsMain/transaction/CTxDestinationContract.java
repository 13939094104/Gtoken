package vdsMain.transaction;

import bitcoin.BaseBlob;
import vdsMain.CTxDestination;
import vdsMain.CTxDestinationType;
import vdsMain.CTxDestinationUInt160;

//bjm
public class CTxDestinationContract extends CTxDestinationUInt160 {
    public CTxDestinationContract() {
    }

    public CTxDestinationContract(byte[] bArr) {
        super(bArr);
    }

    public CTxDestinationContract(BaseBlob baseBlob) {
        super(baseBlob);
    }

    /* renamed from: a */
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.CONTRACT;
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new CTxDestinationContract((BaseBlob) this);
    }
}