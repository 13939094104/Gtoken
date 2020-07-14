package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.keyid.CTxDestinationFactory;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//910 blp
public class GetAddrTxsMessage extends VMessage {

    //f12006a
    private CTxDestination des;

    //f12007b
    private UInt256 firstBlockhash;

    //f12008h
    private UInt256 lastBlockHash;

    public GetAddrTxsMessage(@NonNull Wallet izVar) {
        super(izVar, "getaddrtxs");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        CTxDestinationFactory.m914a(this.des, streamWriter);
        this.firstBlockhash.serialToStream(streamWriter);
        this.lastBlockHash.serialToStream(streamWriter);
    }

    //mo42616a
    public void setDes(CTxDestination cTxDestination) {
        this.des = cTxDestination;
    }

    //mo42615a
    public void setFirstBlockHash(UInt256 uInt256) {
        this.firstBlockhash = uInt256;
    }

    //mo42617b
    public void setLastBlockHash(UInt256 uInt256) {
        this.lastBlockHash = uInt256;
    }
}
