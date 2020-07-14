package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bpb
public class SetStartOtcMessage extends ContractGroupMessage {

    /* renamed from: b */
    public boolean f11996b;

    public SetStartOtcMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setsotc", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeBoolean(this.f11996b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11996b = readBoolean();
    }
}
