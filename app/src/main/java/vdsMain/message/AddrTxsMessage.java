package vdsMain.message;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.keyid.CTxDestinationFactory;
import generic.serialized.SeriableData;
import io.reactivex.annotations.NonNull;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//910 bky
public class AddrTxsMessage extends VMessage {

    //f11952a
    private CTxDestination des;

    /* renamed from: b */
    private UInt256 f11953b;

    /* renamed from: h */
    private UInt256 f11954h;

    /* renamed from: i */
    private Map<UInt256, List<UInt256>> f11955i = new HashMap();

    public AddrTxsMessage(@NonNull Wallet izVar) {
        super(izVar, "addrtxs");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        CTxDestinationFactory.m914a(this.des, streamWriter);
        this.f11953b.serialToStream(streamWriter);
        this.f11954h.serialToStream(streamWriter);
        streamWriter.writeVariableInt((long) this.f11955i.size());
        for (Map.Entry<UInt256, List<UInt256>> entry : this.f11955i.entrySet()) {
            ((UInt256) entry.getKey()).serialToStream(streamWriter);
            streamWriter.writeVariableInt((long) ((List) entry.getValue()).size());
            for (UInt256 serialToStream : (List<UInt256>) entry.getValue()) {
                serialToStream.serialToStream(streamWriter);
            }
        }
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.des = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11953b = new UInt256();
        this.f11953b.decodeSerialStream((SeriableData) this);
        this.f11954h = new UInt256();
        this.f11954h.decodeSerialStream((SeriableData) this);
        int b = readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            UInt256 uInt256 = new UInt256();
            uInt256.decodeSerialStream((SeriableData) this);
            int b2 = readVariableInt().getIntValue();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < b2; i2++) {
                UInt256 uInt2562 = new UInt256();
                uInt2562.decodeSerialStream((SeriableData) this);
                arrayList.add(uInt2562);
            }
            this.f11955i.put(uInt256, arrayList);
        }
    }

    //mo42598a
    public CTxDestination getDes() {
        return this.des;
    }

    /* renamed from: b */
    public Map<UInt256, List<UInt256>> mo42599b() {
        return this.f11955i;
    }
}
