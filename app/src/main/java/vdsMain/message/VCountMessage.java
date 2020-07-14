package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bmm
public class VCountMessage extends VMessage {

    /* renamed from: a */
    public int f11926a;

    /* renamed from: b */
    public int f11927b;

    public VCountMessage(@NonNull Wallet izVar) {
        super(izVar, "vcount");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.f11926a);
        streamWriter.writeUInt32T((long) this.f11927b);
    }

    public void onDecodeSerialData() {
        this.f11926a = (int) readUInt32();
        this.f11927b = (int) readUInt32();
    }
}
