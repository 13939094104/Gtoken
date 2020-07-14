package vdsMain.contract;

import bitcoin.script.CScript;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

//bjn
public class CommuSpvStruct extends SeriableData {

    /* renamed from: a */
    public CScript f11782a;

    /* renamed from: b */
    public long f11783b;

    /* renamed from: c */
    public long f11784c;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        CScript cScript = this.f11782a;
        if (cScript == null) {
            new CScript().serialToStream(streamWriter);
        } else {
            cScript.serialToStream(streamWriter);
        }
        writeUInt32T(this.f11783b);
        writeUInt32T(this.f11784c);
    }

    public void onDecodeSerialData() {
        this.f11782a = new CScript();
        this.f11782a.decodeSerialStream(getTempInput());
        this.f11783b = readUInt32();
        this.f11784c = readUInt32();
    }
}
