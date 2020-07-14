package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//910 bmd
public class GetTxDataMessage extends VMessage {

    /* renamed from: a */
    public UInt256 f12020a;

    public GetTxDataMessage(@NonNull Wallet izVar, UInt256 uInt256) {
        super(izVar, "gettxdata");
        mo42627a(uInt256);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        if (this.f12020a == null) {
            this.f12020a = new UInt256();
        }
        this.f12020a.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12020a = new UInt256();
        this.f12020a.decodeSerialStream((SeriableData) this);
    }

    /* renamed from: a */
    public void mo42627a(UInt256 uInt256) {
        this.f12020a = uInt256;
    }
}
