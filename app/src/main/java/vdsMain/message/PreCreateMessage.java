package vdsMain.message;

import androidx.annotation.NonNull;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bmf
public class PreCreateMessage extends VMessage {

    /* renamed from: a */
    public CTxDestination f11909a;

    /* renamed from: b */
    public CTxDestination f11910b;

    public PreCreateMessage(@NonNull Wallet izVar) {
        super(izVar, "precreatecl");
    }

    public PreCreateMessage(@NonNull Wallet izVar, @NonNull CTxDestination oVar, @NonNull CTxDestination oVar2) {
        super(izVar, "precreatecl");
        this.f11909a = oVar;
        this.f11910b = oVar2;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11909a.writeTypeAndData(streamWriter);
        this.f11910b.writeTypeAndData(streamWriter);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11909a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11910b = CTxDestinationFactory.m910a((SeriableData) this);
    }
}
