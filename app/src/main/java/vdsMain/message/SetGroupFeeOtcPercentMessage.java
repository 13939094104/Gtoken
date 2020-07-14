package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bor
public class SetGroupFeeOtcPercentMessage extends ContractGroupMessage {

    /* renamed from: b */
    public long f11987b;

    /* renamed from: i */
    public long f11988i;

    public SetGroupFeeOtcPercentMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgfotcp", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T(this.f11987b);
        streamWriter.writeUInt32T(this.f11988i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11987b = readUInt32();
        this.f11988i = readUInt32();
    }
}
