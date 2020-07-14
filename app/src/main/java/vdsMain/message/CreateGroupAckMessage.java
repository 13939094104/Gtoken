package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnj
public class CreateGroupAckMessage extends ContractAckMessage {

    /* renamed from: b */
    public byte[] f11955b;

    /* renamed from: i */
    public long f11956i;

    /* renamed from: m */
    public long f11957m;

    /* renamed from: n */
    public String f11958n;

    public CreateGroupAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "crtgroupack", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        if (this.f11945a == 0) {
            streamWriter.writeVariableBytes(this.f11955b);
            streamWriter.writeUInt32T(this.f11956i);
            streamWriter.writeUInt64(this.f11957m);
            return;
        }
        streamWriter.writeVariableString(this.f11958n);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        if (this.f11945a == 0) {
            this.f11955b = readVariableBytes();
            this.f11956i = readUInt32();
            this.f11957m = readInt64();
            return;
        }
        this.f11958n = readVariableString();
    }

    /* renamed from: d */
    public boolean mo42616d() {
        return this.f11945a == 0;
    }
}
