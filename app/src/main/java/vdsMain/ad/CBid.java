package vdsMain.ad;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

//biz
public class CBid extends SeriableData {

    /* renamed from: a */
    private int f11713a;

    /* renamed from: b */
    private UInt256 f11714b;

    /* renamed from: c */
    private long f11715c;

    public void writeSerialData(StreamWriter streamWriter) {
    }

    /* renamed from: a */
    public int mo42485a() {
        return this.f11713a;
    }

    /* renamed from: b */
    public UInt256 mo42486b() {
        return this.f11714b;
    }

    /* renamed from: c */
    public long mo42487c() {
        return this.f11715c;
    }

    public void onDecodeSerialData() throws IOException {
        this.f11713a = readInt32();
        this.f11714b = readUint256();
        this.f11715c = readInt64();
    }
}
