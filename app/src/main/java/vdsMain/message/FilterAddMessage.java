package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bkx
public class FilterAddMessage extends VMessage {

    /* renamed from: a */
    private byte[] f11879a = null;

    public FilterAddMessage(@NonNull Wallet izVar) {
        super(izVar, "filteradd");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11879a = readByteArray();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeBytes(this.f11879a);
    }
}
