package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//910 blq
public class GetAnonTxMessage extends VMessage {

    //f12009a
    private UInt256 startBlockHash;

    //f12010b
    private UInt256 endBlockHash;

    public GetAnonTxMessage(@NonNull Wallet izVar) {
        super(izVar, "getanonytx");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.startBlockHash.serialToStream(streamWriter);
        this.endBlockHash.serialToStream(streamWriter);
    }

    //mo42618a
    public void initStartAndEndBlockHash(@NonNull UInt256 uInt256, @NonNull UInt256 uInt2562) {
        this.startBlockHash = uInt256;
        this.endBlockHash = uInt2562;
    }
}
