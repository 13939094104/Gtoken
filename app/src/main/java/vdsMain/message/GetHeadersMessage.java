package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.Sha256Hash;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//bli
public class GetHeadersMessage extends VMessage {

    /* renamed from: a */
    protected List<byte[]> f11885a = null;

    /* renamed from: b */
    protected byte[] f11886b = null;

    public GetHeadersMessage(@NonNull Wallet izVar) {
        super(izVar, "getheaders");
    }

    public GetHeadersMessage(@NonNull Wallet izVar, List<byte[]> list, byte[] bArr) {
        super(izVar, "getheaders", 0, 0);
        if (list != null) {
            this.f11885a = list;
            this.f11886b = bArr;
            return;
        }
        throw new NullPointerException("param checkedBlockHashs is null");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        if ((mo42594n() & 4) == 0) {
            readVariableInt();
        }
        int b = readVariableInt().getIntValue();
        if (b > 0) {
            this.f11885a = new ArrayList(b);
            for (int i = 0; i < b; i++) {
                this.f11885a.add(readBytes(32));
            }
        }
        if (hasReadableDataLeft()) {
            this.f11886b = readBytes(32);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        if ((mo42594n() & 4) == 0) {
            streamWriter.writeCVarInt((long) this.mProtocalVersion);
        }
        List<byte[]> list = this.f11885a;
        if (list == null) {
            streamWriter.writeVariableInt(0);
        } else {
            streamWriter.writeVariableInt((long) list.size());
            for (byte[] write : this.f11885a) {
                streamWriter.write(write);
            }
        }
        byte[] bArr = this.f11886b;
        if (bArr != null) {
            streamWriter.write(bArr);
        } else {
            streamWriter.write(Sha256Hash.f214a.mo195a());
        }
    }
}
