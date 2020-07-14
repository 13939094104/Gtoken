package vdsMain.message;

import androidx.annotation.NonNull;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bkw
public class ClueTopListRecordMessage extends VMessage {

    /* renamed from: a */
    public CTxDestination f11876a;

    /* renamed from: b */
    public int f11877b;

    /* renamed from: h */
    public int f11878h;

    public ClueTopListRecordMessage(@NonNull Wallet izVar) {
        super(izVar, "clurtoprec");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        CTxDestinationFactory.m914a(this.f11876a, streamWriter);
        streamWriter.writeUInt32T((long) this.f11877b);
        streamWriter.writeUInt32T((long) this.f11878h);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11876a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11877b = readInt32();
        this.f11878h = readInt32();
    }
}
