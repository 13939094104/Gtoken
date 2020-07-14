package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//blp
public class GetTxsMessage extends VMessage {
    public List<UInt256> f11890a;

    public GetTxsMessage(@NonNull Wallet izVar) {
        super(izVar, "gettxs");
    }

    public GetTxsMessage(@NonNull Wallet izVar, List<UInt256> list) {
        super(izVar, "gettxs");
        mo42569a(list);
    }

    /* renamed from: a */
    public void mo42569a(List<UInt256> list) {
        this.f11890a = null;
        if (list != null) {
            this.f11890a = new Vector(list);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        List<UInt256> list = this.f11890a;
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) this.f11890a.size());
        for (UInt256 serialToStream : this.f11890a) {
            serialToStream.serialToStream(streamWriter);
        }
    }

    public void onDecodeSerialData() {
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            this.f11890a = null;
            return;
        }
        this.f11890a = new Vector(b);
        for (int i = 0; i < b; i++) {
            UInt256 uInt256 = new UInt256();
            uInt256.decodeSerialStream((SeriableData) this);
            this.f11890a.add(uInt256);
        }
    }
}