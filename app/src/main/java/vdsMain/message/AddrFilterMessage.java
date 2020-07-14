package vdsMain.message;

import androidx.annotation.NonNull;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

//bkn
public abstract class AddrFilterMessage extends VMessage {

    /* renamed from: a */
    public List<CTxDestination> f11836a;

    public AddrFilterMessage(@NonNull Wallet izVar, String str, Collection<CTxDestination> collection) {
        super(izVar, str);
        this.f11836a = new ArrayList(collection);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeObjectList(this.f11836a);
    }

    public void onDecodeSerialData() throws IOException {
        int b = readVariableInt().getIntValue();
        if (b <= 0) {
            this.f11836a = null;
            return;
        }
        this.f11836a = new Vector(b);
        for (int i = 0; i < b; i++) {
            this.f11836a.add(CTxDestinationFactory.m910a((SeriableData) this));
        }
    }
}
