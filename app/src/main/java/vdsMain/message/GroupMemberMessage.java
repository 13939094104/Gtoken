package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnp
public class GroupMemberMessage  extends ContractGroupMessage {

    /* renamed from: b */
    String f11971b;

    public GroupMemberMessage(@NonNull Wallet izVar, String str, int i) {
        super(izVar, str, i);
    }

    public GroupMemberMessage(@NonNull Wallet izVar, String str, int i, String str2, String str3) {
        super(izVar, str, i, str2);
        this.f11971b = str3;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeVariableString(this.f11971b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11971b = readVariableString();
    }
}
