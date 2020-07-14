package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//blb
public class GetAdMessage extends VMessage {

    /* renamed from: a */
    private UInt256 f11880a;

    public GetAdMessage(@NonNull Wallet izVar) {
        super(izVar, "getad");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        if (this.f11880a == null) {
            this.f11880a = new UInt256();
        }
        this.f11880a.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f11880a = new UInt256();
        this.f11880a.decodeSerialStream(getTempInput());
    }

    /* renamed from: a */
    public UInt256 mo42562a() {
        return this.f11880a;
    }
}
