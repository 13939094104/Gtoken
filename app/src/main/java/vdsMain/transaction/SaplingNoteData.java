package vdsMain.transaction;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bsd
public class SaplingNoteData extends SeriableData {

    //f12212a
    public List<SaplingWitness> witnesses = new Vector();

    //f12213b
    public int witnessHeight = -1;

    //f12214c
    public SaplingIncomingViewingKey incomingViewingKey = new SaplingIncomingViewingKey();

    //f12215d
    public UInt256 nullifier;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.incomingViewingKey.serialToStream(streamWriter);
        streamWriter.writeOptionalObject(this.nullifier);
        streamWriter.writeObjectList(this.witnesses);
        streamWriter.writeUInt32T((long) this.witnessHeight);
    }

    public void onDecodeSerialData() throws IOException {
        this.incomingViewingKey.decodeSerialStream((SeriableData) this);
        this.nullifier = UInt256.readOptional(this);
        readObjectList(this.witnesses, SaplingWitness.class);
        this.witnessHeight = (int) readUInt32();
    }

    /* renamed from: a */
    public byte[] mo43009a() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DumpedStreamWriter dumpedStreamWriter = new DumpedStreamWriter(byteArrayOutputStream);
        try {
            this.incomingViewingKey.serialToStream((StreamWriter) dumpedStreamWriter);
            dumpedStreamWriter.writeOptionalObject(this.nullifier);
            if (this.witnesses != null) {
                if (!this.witnesses.isEmpty()) {
                    dumpedStreamWriter.writeVariableInt((long) this.witnesses.size());
                    for (SaplingWitness saplingWitness : this.witnesses) {
                        dumpedStreamWriter.writeVariableBytes(saplingWitness.mo43030i());
                    }
                    dumpedStreamWriter.writeUInt32T((long) this.witnessHeight);
                    return byteArrayOutputStream.toByteArray();
                }
            }
            dumpedStreamWriter.writeVariableInt(0);
            dumpedStreamWriter.writeUInt32T((long) this.witnessHeight);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public void mo43008a(byte[] bArr) {
        try {
            DummySeriableData loVar = new DummySeriableData(new ByteBuffer(bArr));
            this.incomingViewingKey.decodeSerialStream((SeriableData) loVar);
            this.nullifier = UInt256.readOptional(loVar);
            int b = loVar.readVariableInt().getIntValue();
            if (this.witnesses != null) {
                this.witnesses.clear();
            }
            if (b != 0) {
                if (this.witnesses == null) {
                    this.witnesses = new Vector();
                }
                for (int i = 0; i < b; i++) {
                    SaplingWitness bsh = new SaplingWitness();
                    bsh.mo43028a(loVar.readVariableBytes());
                    this.witnesses.add(bsh);
                }
            }
            this.witnessHeight = (int) loVar.readUInt32();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingNoteData)) {
            return false;
        }
        SaplingNoteData bsd = (SaplingNoteData) obj;
        return this.witnessHeight == bsd.witnessHeight && this.incomingViewingKey.equals(bsd.incomingViewingKey) && Collection.m11556b(this.witnesses, bsd.witnesses);
    }
}
