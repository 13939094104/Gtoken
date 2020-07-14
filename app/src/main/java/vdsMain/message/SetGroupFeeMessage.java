package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bop
public class SetGroupFeeMessage extends ContractGroupMessage {

    /* renamed from: b */
    public long f11986b;

    public SetGroupFeeMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgfee", i);
    }

    public SetGroupFeeMessage(@NonNull Wallet izVar, int i, String str, long j) {
        super(izVar, "setgfee", i, str);
        this.f11986b = j;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T(this.f11986b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11986b = readUInt32();
    }
}
