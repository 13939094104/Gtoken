package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.CNoDestination;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.clue.Clue;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bku
public class ClueMessage extends VMessage {

    /* renamed from: a */
    public Clue f11860a;

    public ClueMessage(@NonNull Wallet izVar) {
        super(izVar, "cl");
    }

    /* renamed from: a */
    public boolean mo42560a() {
        if (this.f11860a.f11804a == null || this.f11860a.f11804a.isNull() || this.f11860a.f11805b == null || (this.f11860a.f11805b instanceof CNoDestination)) {
            return true;
        }
        if ((this.f11860a.f11807d == null || (this.f11860a.f11807d instanceof CNoDestination)) && (this.f11860a.f11806c == null || (this.f11860a.f11806c instanceof CNoDestination))) {
            if (getSelfWallet().checkAndGetBlockInfo(this.f11860a.f11804a) == null) {
                return true;
            }
        } else if (this.f11860a.f11807d == null || (this.f11860a.f11807d instanceof CNoDestination) || this.f11860a.f11806c == null || (this.f11860a.f11806c instanceof CNoDestination)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11860a.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        if (this.f11860a == null) {
            this.f11860a = new Clue();
        }
        this.f11860a.decodeSerialStream((SeriableData) this);
    }
}
