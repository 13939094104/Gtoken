package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.StringToolkit;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnf
public abstract class ContractGroupMessage extends ContractMessage {

    /* renamed from: a */
    public byte[] f11947a;

    public ContractGroupMessage(@NonNull Wallet izVar, String str, int i) {
        super(izVar, str, i);
    }

    public ContractGroupMessage(@NonNull Wallet izVar, String str, int i, String str2) {
        super(izVar, str, i);
        mo42613b(str2);
    }

    /* renamed from: b */
    public void mo42613b(String str) {
        if (str == null || str.isEmpty()) {
            this.f11947a = null;
        } else {
            this.f11947a = StringToolkit.m11526a(str);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(this.f11947a);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        this.f11947a = readVariableBytes();
    }
}
