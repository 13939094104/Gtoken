package vdsMain;

import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bqm
public class TagParaTxMonitor extends SeriableData {

    /* renamed from: a */
    public List<Pair<CTxDestination, Long>> f12088a;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        List<Pair<CTxDestination, Long>> list = this.f12088a;
        if (list == null || list.isEmpty()) {
            streamWriter.writeUInt64(0);
            return;
        }
        streamWriter.writeUInt64((long) this.f12088a.size());
        for (Pair gsVar : this.f12088a) {
            CTxDestinationFactory.m914a((CTxDestination) gsVar.key, streamWriter);
            streamWriter.writeUInt64(((Long) gsVar.value).longValue());
        }
    }

    public void onDecodeSerialData() throws IOException {
        long c = readVariableInt().getValue();
        if (c == 0) {
            this.f12088a = null;
            return;
        }
        this.f12088a = new Vector((int) c);
        for (long j = 0; j < c; j++) {
            Pair gsVar = new Pair();
            gsVar.key = CTxDestinationFactory.m910a((SeriableData) this);
            gsVar.value = Long.valueOf(readUInt64().longValue());
            this.f12088a.add(gsVar);
        }
    }
}
