package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bng
public abstract class ContractMessage extends VMessage {

    /* renamed from: h */
    public int f11948h = 0;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo42608a() throws UnsupportedEncodingException;

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public abstract void mo42609b(StreamWriter streamWriter) throws IOException;

    public ContractMessage(@NonNull Wallet izVar, String str, int i) {
        super(izVar, str);
        this.f11948h = i;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public final void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.f11948h);
        mo42609b(streamWriter);
    }

    public final void onDecodeSerialData() throws UnsupportedEncodingException {
        this.f11948h = (int) readUInt32();
        mo42608a();
    }
}
