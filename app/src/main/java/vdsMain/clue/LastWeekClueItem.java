package vdsMain.clue;

import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;
import java.math.BigInteger;

//bju
public class LastWeekClueItem extends SeriableData {

    /* renamed from: a */
    public CTxDestination f11808a;

    /* renamed from: b */
    public BigInteger f11809b;

    /* renamed from: c */
    public double f11810c = 0.0d;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11808a.writeTypeAndData(streamWriter);
        streamWriter.writeUInt64(this.f11809b);
        streamWriter.writeDouble(this.f11810c);
    }

    public void onDecodeSerialData() throws IOException {
        this.f11808a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11809b = readUInt64();
        this.f11810c = readDouble();
    }
}
