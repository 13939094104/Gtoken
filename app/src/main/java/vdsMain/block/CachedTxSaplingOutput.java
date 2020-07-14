package vdsMain.block;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.transaction.OutputDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//bjs
public class CachedTxSaplingOutput extends SeriableData {

    /* renamed from: a */
    public UInt256 f11802a;

    /* renamed from: b */
    public List<UInt256> f11803b;

    public CachedTxSaplingOutput() {
        this.f11802a = new UInt256();
    }

    public CachedTxSaplingOutput(UInt256 uInt256, List<OutputDescription> list) {
        this();
        mo42526a(uInt256, list);
    }

    /* renamed from: a */
    public void mo42526a(UInt256 uInt256, List<OutputDescription> list) {
        this.f11802a.set((BaseBlob) uInt256);
        if (list == null || list.isEmpty()) {
            this.f11803b = null;
            return;
        }
        List<UInt256> list2 = this.f11803b;
        if (list2 == null) {
            this.f11803b = new ArrayList(list.size());
        } else {
            list2.clear();
        }
        for (OutputDescription brj : list) {
            this.f11803b.add(brj.cm);
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11802a.serialToStream(streamWriter);
        streamWriter.writeObjectList(this.f11803b);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11802a.decodeSerialStream((SeriableData) this);
        this.f11803b = new ArrayList();
        readObjectList(this.f11803b, UInt256.class);
        if (this.f11803b.isEmpty()) {
            this.f11803b = null;
        }
    }
}