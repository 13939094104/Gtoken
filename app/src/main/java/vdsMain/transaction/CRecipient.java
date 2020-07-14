package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import bitcoin.script.CScript;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

//bjl
public class CRecipient extends SeriableData {

    /* renamed from: a */
    public CScript f11777a;

    /* renamed from: b */
    public short f11778b;

    /* renamed from: c */
    public long f11779c;

    /* renamed from: d */
    public UInt256 f11780d;

    /* renamed from: e */
    public boolean f11781e;

    public CRecipient() {
    }

    public CRecipient(CScript cScript, short s, long j, UInt256 uInt256, boolean z) {
        if (cScript != null) {
            this.f11777a = new CScript((SeriableData) cScript);
        }
        this.f11778b = s;
        this.f11779c = j;
        if (uInt256 != null) {
            this.f11780d = new UInt256((BaseBlob) uInt256);
        }
        this.f11781e = z;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        CScript cScript = this.f11777a;
        if (cScript == null) {
            new CScript().serialToStream(streamWriter);
        } else {
            cScript.serialToStream(streamWriter);
        }
        streamWriter.write((byte) this.f11778b);
        streamWriter.writeUInt64(this.f11779c);
        UInt256.serialUInt256(this.f11780d, streamWriter);
        writeBoolean(this.f11781e);
    }

    public void onDecodeSerialData() {
        this.f11777a = new CScript();
        this.f11777a.decodeSerialStream((SeriableData) this);
        this.f11778b = (short) readByte();
        this.f11779c = readUInt64().longValue();
        this.f11780d = new UInt256();
        this.f11780d.decodeSerialStream((SeriableData) this);
        this.f11781e = readBoolean();
    }
}
