package vdsMain.transaction;

import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;

//bjw
public class SerializedSaplingNoteItem extends SeriableData {

    //f11813a
    public CTxDestination cTxDestination;

    //f11814b
    public long value;

    //f11815c
    public SaplingNoteData saplingNoteData;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        CTxDestinationFactory.m914a(this.cTxDestination, streamWriter);
        streamWriter.writeUInt64(this.value);
        streamWriter.writeVariableBytes(this.saplingNoteData.mo43009a());
    }

    public void onDecodeSerialData() throws IOException {
        this.cTxDestination = CTxDestinationFactory.m910a((SeriableData) this);
        this.value = readUInt64().longValue();
        this.saplingNoteData = new SaplingNoteData();
        this.saplingNoteData.mo43008a(readVariableBytes());
    }
}
