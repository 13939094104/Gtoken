package vdsMain.message;


import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnd
public class ContractAckMessage extends ContractMessage {

    /* renamed from: a */
    public short f11945a;

    public ContractAckMessage(@NonNull Wallet izVar, String str, int i) {
        super(izVar, str, i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        streamWriter.writeBytes(new byte[]{(byte) this.f11945a});
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        this.f11945a = readUInt8();
    }

    /* renamed from: b */
    public boolean mo42610b() {
        return this.f11945a != 0;
    }
}
