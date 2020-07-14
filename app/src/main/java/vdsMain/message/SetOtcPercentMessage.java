package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//boz
public class SetOtcPercentMessage extends ContractGroupMessage {

    /* renamed from: b */
    public long f11994b;

    public SetOtcPercentMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setotcp", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T(this.f11994b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11994b = readUInt32();
    }
}
