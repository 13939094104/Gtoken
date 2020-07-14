package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;

import java.io.IOException;
import java.util.Arrays;


//bri
public class GrothProof extends SeriableData {

    /* renamed from: a */
    protected byte[] f12163a = new byte[192];

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeBytes(this.f12163a);
    }

    public void onDecodeSerialData() {
        this.f12163a = readBytes(192);
    }

    /* renamed from: a */
    public byte[] mo42938a() {
        return this.f12163a;
    }

    /* renamed from: b */
    public byte[] mo42939b() {
        return this.f12163a;
    }

    /* renamed from: a */
    public void mo42937a(GrothProof bri) {
        if (bri != null) {
            System.arraycopy(bri.f12163a, 0, this.f12163a, 0, 192);
        } else {
            DataTypeToolkit.m11494a(this.f12163a, (byte) 0);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof GrothProof) {
            return Arrays.equals(this.f12163a, ((GrothProof) obj).f12163a);
        }
        return false;
    }

    /* renamed from: c */
    public void mo42940c() {
        DataTypeToolkit.setBytesZero(this.f12163a);
    }

    public String toString() {
        return StringToolkit.bytesToString(this.f12163a);
    }
}