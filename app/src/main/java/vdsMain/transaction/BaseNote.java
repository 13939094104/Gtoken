package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

//brm
public class BaseNote extends SeriableData {

    /* renamed from: a */
    protected long f12176a = 0;

    public BaseNote() {
    }

    public BaseNote(long j) {
        this.f12176a = j;
    }

    public BaseNote(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof BaseNote) {
            this.f12176a = ((BaseNote) seriableData).f12176a;
        }
    }

    /* renamed from: a */
    public long mo42950a() {
        return this.f12176a;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt64(this.f12176a);
    }

    public void onDecodeSerialData() {
        this.f12176a = readUInt64().longValue();
    }
}
