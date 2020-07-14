package vdsMain.message;

import androidx.annotation.NonNull;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//blg
public class GetClueTopRecordMessage extends VMessage {

    /* renamed from: a */
    public CTxDestination f11882a;

    /* renamed from: b */
    public int f11883b;

    public GetClueTopRecordMessage(@NonNull Wallet izVar) {
        super(izVar, "gcluetoprec");
    }

    public GetClueTopRecordMessage(@NonNull Wallet izVar, CTxDestination oVar, int i) {
        super(izVar, "gcluetoprec");
        this.f11882a = oVar;
        this.f11883b = i;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        CTxDestinationFactory.m914a(this.f11882a, streamWriter);
        streamWriter.writeUInt32T((long) this.f11883b);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11882a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11883b = readInt32();
    }
}
