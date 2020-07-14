package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//boy
public class SetOtcPercentCurrencyMessage extends ContractGroupMessage {

    /* renamed from: b */
    private long f11991b;

    /* renamed from: i */
    private String f11992i;

    /* renamed from: m */
    private String f11993m;

    public SetOtcPercentCurrencyMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setotcc", i);
    }

    public SetOtcPercentCurrencyMessage(@NonNull Wallet izVar, int i, String str, int i2, String str2, String str3) {
        super(izVar, "setotcc", i, str);
        this.f11991b = (long) i2;
        this.f11992i = str2;
        this.f11993m = str3;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeUInt32T(this.f11991b);
        streamWriter.writeVariableString(this.f11992i);
        streamWriter.writeVariableString(this.f11993m);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11991b = readUInt32();
        this.f11992i = readVariableString();
        this.f11993m = readVariableString();
    }
}
