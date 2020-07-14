package vdsMain.message;

import androidx.annotation.NonNull;
import generic.network.AddressInfo;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;

//bko
public class AddrMessage extends VMessage {

    /* renamed from: a */
    private List<AddressInfo> f11837a = new ArrayList();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public AddrMessage(@NonNull Wallet izVar) {
        super(izVar, "addr");
    }

    public synchronized void clean() {
        this.f11837a.clear();
    }

    /* renamed from: a */
    public List<AddressInfo> mo42551a() {
        return this.f11837a;
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        int b = readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            readInt32();
            readInt64();
            byte[] bArr = new byte[16];
            readBytes(bArr);
            this.f11837a.add(new AddressInfo(Inet6Address.getByAddress(bArr), readUInt16()));
        }
    }
}
