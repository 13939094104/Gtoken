package vdsMain;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.message.ContractMessage;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnk
public class CreateGroupMessage extends ContractMessage {

    /* renamed from: a */
    public String f11959a;

    /* renamed from: b */
    public String f11960b;

    /* renamed from: i */
    public String f11961i;

    /* renamed from: m */
    public long f11962m;

    /* renamed from: n */
    public String f11963n;

    public CreateGroupMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "creategroup", i);
    }

    public CreateGroupMessage(@NonNull Wallet izVar, int i, @NonNull String str, @NonNull String str2, @NonNull String str3, long j, String str4) {
        super(izVar, "creategroup", i);
        mo42617a(str, str2, str3, j, str4);
    }

    /* renamed from: a */
    public void mo42617a(@NonNull String str, @NonNull String str2, @NonNull String str3, long j, String str4) {
        this.f11959a = str;
        this.f11960b = str2;
        this.f11961i = str3;
        this.f11962m = j;
        this.f11963n = str4;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableString(this.f11959a);
        streamWriter.writeVariableString(this.f11960b);
        streamWriter.writeVariableString(this.f11961i);
        streamWriter.writeUInt32T(this.f11962m);
        streamWriter.writeVariableString(this.f11963n);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        this.f11959a = readVariableString();
        this.f11960b = readVariableString();
        this.f11961i = readVariableString();
        this.f11962m = readUInt32();
        this.f11963n = readVariableString();
    }
}
