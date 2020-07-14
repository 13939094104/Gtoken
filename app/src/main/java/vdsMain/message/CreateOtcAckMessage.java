package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

//bnl
public class CreateOtcAckMessage extends ContractAckMessage {

    /* renamed from: b */
    public byte[] f11964b;

    /* renamed from: i */
    public long f11965i;

    /* renamed from: m */
    public long f11966m;

    public CreateOtcAckMessage(@NonNull Wallet izVar, int i) {
        super(izVar, "createotcack", i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42609b(StreamWriter streamWriter) throws IOException {
        super.mo42609b(streamWriter);
        streamWriter.writeVariableBytes(this.f11964b);
        streamWriter.writeUInt32T(this.f11965i);
        streamWriter.writeUInt32T(this.f11966m);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42608a() throws UnsupportedEncodingException {
        super.mo42608a();
        this.f11964b = readVariableBytes();
        this.f11965i = readUInt32();
        this.f11966m = readUInt32();
    }
}
