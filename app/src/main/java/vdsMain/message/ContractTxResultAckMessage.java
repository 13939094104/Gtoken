package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bnh
public class ContractTxResultAckMessage extends VMessage {

    /* renamed from: a */
    private short f11949a;

    /* renamed from: b */
    private String f11950b;

    /* renamed from: h */
    private String f11951h;

    /* renamed from: i */
    private String f11952i;

    /* renamed from: m */
    private String f11953m;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public ContractTxResultAckMessage(@NonNull Wallet izVar) {
        super(izVar, "iqconactack");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11949a = readUInt8();
        if (this.f11949a == 0) {
            this.f11950b = readVariableString();
            this.f11951h = readVariableString();
            this.f11952i = readVariableString();
            return;
        }
        this.f11950b = readVariableString();
        this.f11953m = readVariableString();
    }

    /* renamed from: a */
    public short mo42614a() {
        return this.f11949a;
    }

    /* renamed from: b */
    public String mo42615b() {
        return this.f11950b;
    }
}
