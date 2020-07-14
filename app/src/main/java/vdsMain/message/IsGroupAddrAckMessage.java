package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.StringToolkit;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bns
public class IsGroupAddrAckMessage extends VMessage {

    /* renamed from: a */
    public String f11973a;

    /* renamed from: b */
    public boolean f11974b;

    public IsGroupAddrAckMessage(@NonNull Wallet izVar) {
        super(izVar, "isgpaddrack");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(StringToolkit.m11526a(this.f11973a));
        streamWriter.writeBoolean(this.f11974b);
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11973a = StringToolkit.bytesToString(readVariableBytes());
        this.f11974b = readBoolean();
    }
}
