package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.OutputStream;

public class DummySeriableData extends SeriableData {
    @Deprecated
    public void onDecodeSerialData() {
    }

    @Deprecated
    public void writeSerialData(StreamWriter streamWriter) {
    }

    public DummySeriableData() {
    }

    public DummySeriableData(OutputStream outputStream) {
        super(outputStream);
    }

    public DummySeriableData(ByteBuffer gcVar) {
        super(gcVar);
    }
}