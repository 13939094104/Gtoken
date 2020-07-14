package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.OutputStream;

public class DumpedStreamWriter extends SeriableData {
    public void onDecodeSerialData() {
    }

    public void writeSerialData(StreamWriter streamWriter) {
    }


    public DumpedStreamWriter() {
    }

    public DumpedStreamWriter(OutputStream outputStream) {
        super(outputStream);
    }
}
