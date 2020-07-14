package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bov
public class SetGroupRuleMessage extends ContractGroupMessage {

    /* renamed from: b */
    public String f11990b;

    public SetGroupRuleMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "setgrule", i);
    }

    public SetGroupRuleMessage(@NonNull Wallet izVar, int i, String str, String str2) {
        super(izVar, "setgrule", i, str);
        this.f11990b = str2;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeVariableString(this.f11990b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11990b = readVariableString();
    }
}
