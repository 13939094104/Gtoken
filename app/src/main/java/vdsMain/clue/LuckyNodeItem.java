package vdsMain.clue;

import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;
import java.math.BigInteger;

//bjv
public class LuckyNodeItem extends SeriableData {

    /* renamed from: a */
    public CTxDestination f11811a;

    /* renamed from: b */
    public BigInteger f11812b;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11811a.writeTypeAndData(streamWriter);
        streamWriter.writeUInt64(this.f11812b);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11811a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11812b = readUInt64();
    }
}
