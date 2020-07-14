package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnr
public class IncreaseGroupMaxMemberMessage extends ContractGroupMessage {

    /* renamed from: b */
    protected long f11972b;

    public IncreaseGroupMaxMemberMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "incmax", i);
    }

    public IncreaseGroupMaxMemberMessage(@NonNull Wallet izVar, int i, String str, int i2) {
        super(izVar, "incmax", i, str);
        this.f11972b = (long) i2;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T(this.f11972b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11972b = readUInt32();
    }
}
