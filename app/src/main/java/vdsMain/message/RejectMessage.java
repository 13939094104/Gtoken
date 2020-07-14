package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.block.CValidationState;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bmg
public class RejectMessage extends VMessage implements RejectMessageInterface {

    /* renamed from: a */
    private String f11911a = null;

    /* renamed from: b */
    private int f11912b = 0;

    /* renamed from: h */
    private String f11913h = null;

    /* renamed from: i */
    private byte[] f11914i = null;

    public RejectMessage(@NonNull Wallet izVar) {
        super(izVar, "reject");
    }

    public RejectMessage(@NonNull Wallet izVar, String str, int i, String str2) {
        super(izVar, "reject");
        this.f11911a = str;
        this.f11912b = i;
        this.f11913h = str2;
        m9895f();
    }

    public RejectMessage(Message kwVar, CValidationState atVar, UInt256 uInt256) {
        super(kwVar.getSelfWallet(), "reject");
        this.f11911a = kwVar.getTypeString();
        this.f11912b = atVar.mo41040a();
        if (uInt256 != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(atVar.f8249c);
            sb.append(", hash : ");
            sb.append(uInt256.hashString());
            this.f11913h = sb.toString();
        } else {
            this.f11913h = atVar.f8249c;
        }
        m9895f();
    }

    /* renamed from: f */
    private void m9895f() {
        String str = this.f11913h;
        if (str != null && str.length() > 111) {
            this.f11913h = this.f11913h.substring(0, 111);
        }
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.f11911a = readVariableString();
        this.f11912b = readBytes(1)[0];
        this.f11913h = readVariableString();
        if ("tx".equals(this.f11911a)) {
            this.f11914i = new UInt256(readHashString()).data();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableString(this.f11911a);
        streamWriter.write(new byte[]{(byte) this.f11912b});
        streamWriter.writeVariableString(this.f11913h);
    }

    /* renamed from: a */
    public String mo42583a() {
        return this.f11911a;
    }

    /* renamed from: b */
    public int mo42584b() {
        return this.f11912b;
    }

    /* renamed from: d */
    public String mo42585d() {
        return this.f11913h;
    }

    /* renamed from: e */
    public byte[] mo42586e() {
        return this.f11914i;
    }
}

