package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.StringToolkit;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bnt
public class IsGroupAddrMessage extends VMessage {

    /* renamed from: a */
    public String f11975a;

    public IsGroupAddrMessage(@NonNull Wallet izVar) {
        super(izVar, "isgpaddr");
    }

    public IsGroupAddrMessage(@NonNull Wallet izVar, String str) {
        super(izVar, "isgpaddr");
        this.f11975a = str;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(StringToolkit.m11526a(this.f11975a));
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11975a = StringToolkit.bytesToString(readVariableBytes());
    }
}
