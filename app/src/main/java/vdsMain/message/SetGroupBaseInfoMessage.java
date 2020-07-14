package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bon
public class SetGroupBaseInfoMessage extends ContractGroupMessage {

    /* renamed from: b */
    public String f11981b;

    /* renamed from: i */
    public String f11982i;

    /* renamed from: m */
    public String f11983m;

    /* renamed from: n */
    public long f11984n;

    /* renamed from: o */
    public long f11985o;

    public SetGroupBaseInfoMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setbaseinfo", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        writeVariableString(this.f11981b);
        writeVariableString(this.f11982i);
        writeVariableString(this.f11983m);
        writeUInt32T(this.f11984n);
        writeUInt32T(this.f11985o);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11981b = readVariableString();
        this.f11982i = readVariableString();
        this.f11983m = readVariableString();
        this.f11984n = readUInt32();
        this.f11985o = readUInt32();
    }
}
