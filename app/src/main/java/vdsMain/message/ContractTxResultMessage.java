package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bni
public class ContractTxResultMessage extends VMessage {

    /* renamed from: a */
    private String f11954a;

    public ContractTxResultMessage(@NonNull Wallet izVar, String str) {
        super(izVar, "inqcontact");
        this.f11954a = str;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableString(this.f11954a);
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11954a = readVariableString();
    }
}