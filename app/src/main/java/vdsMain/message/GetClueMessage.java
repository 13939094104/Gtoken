package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.transaction.Script;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//blf
public class GetClueMessage extends VMessage {

    /* renamed from: a */
    public CTxDestination f11881a;

    public GetClueMessage(@NonNull Wallet izVar) {
        super(izVar, "getcl");
    }

    public GetClueMessage(@NonNull Wallet izVar, CTxDestination oVar) {
        super(izVar, "getcl");
        this.f11881a = oVar;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        Script.getLockedSignScript(this.f11881a).serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        Script crVar = new Script();
        crVar.decodeSerialStream((SeriableData) this);
        this.f11881a = crVar.mo43169j();
    }
}
