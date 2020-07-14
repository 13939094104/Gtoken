package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnm
public class CreateOtcMessage extends ContractGroupMessage {

    /* renamed from: b */
    public String f11967b;

    /* renamed from: i */
    public String f11968i;

    /* renamed from: m */
    public long f11969m;

    /* renamed from: n */
    public long f11970n;

    public CreateOtcMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "createotc", i);
    }

    public CreateOtcMessage(@NonNull Wallet izVar, int i, String str, String str2, String str3, long j, long j2) {
        super(izVar, "createotc", i, str);
        this.f11967b = str2;
        this.f11968i = str3;
        this.f11969m = j;
        this.f11970n = j2;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeVariableString(this.f11967b);
        streamWriter.writeVariableString(this.f11968i);
        streamWriter.writeVariableString(String.valueOf(this.f11969m));
        streamWriter.writeVariableString(String.valueOf(this.f11970n));
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11967b = readVariableString();
        this.f11968i = readVariableString();
        this.f11969m = Long.valueOf(readVariableString()).longValue();
        this.f11970n = Long.valueOf(readVariableString()).longValue();
    }
}
