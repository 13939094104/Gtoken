package vdsMain.clue;

import bitcoin.UInt256;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;

//bjt
public class Clue extends SeriableData {

    /* renamed from: a */
    public UInt256 f11804a = new UInt256();

    /* renamed from: b */
    public CTxDestination f11805b;

    /* renamed from: c */
    public CTxDestination f11806c;

    /* renamed from: d */
    public CTxDestination f11807d;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11804a.serialToStream(streamWriter);
        this.f11805b.writeTypeAndData(streamWriter);
        this.f11806c.writeTypeAndData(streamWriter);
        this.f11807d.writeTypeAndData(streamWriter);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11804a.decodeSerialStream((SeriableData) this);
        this.f11805b = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11806c = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11807d = CTxDestinationFactory.m910a((SeriableData) this);
    }
}
