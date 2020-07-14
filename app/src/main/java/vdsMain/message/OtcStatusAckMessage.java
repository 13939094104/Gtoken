package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bny
public class OtcStatusAckMessage extends ContractAckMessage {

    /* renamed from: b */
    public int f11977b;

    public OtcStatusAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "otcstatusack", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T((long) this.f11977b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11977b = (int) readUInt32();
    }
}
