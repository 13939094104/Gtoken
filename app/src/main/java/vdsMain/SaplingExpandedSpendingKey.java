package vdsMain;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import zcash.RustZCash;

import java.io.IOException;
import java.util.Arrays;

//bru
public class SaplingExpandedSpendingKey extends SeriableData {

    /* renamed from: a */
    public UInt256 f12182a = new UInt256();

    /* renamed from: b */
    public UInt256 f12183b = new UInt256();

    /* renamed from: c */
    public UInt256 f12184c = new UInt256();

    /* renamed from: d */
    private int f12185d;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f12182a.serialToStream(streamWriter);
        this.f12183b.serialToStream(streamWriter);
        this.f12184c.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12182a.decodeSerialStream((SeriableData) this);
        this.f12183b.decodeSerialStream((SeriableData) this);
        this.f12184c.decodeSerialStream((SeriableData) this);
        mo42969d();
    }

    /* renamed from: a */
    public SaplingFullViewingKey mo42966a() {
        SaplingFullViewingKey brx = new SaplingFullViewingKey();
        brx.f12202c.set((BaseBlob) this.f12184c);
        RustZCash.ask_to_ak(this.f12182a.data(), brx.f12200a.data());
        brx.f12200a.updateHash();
        RustZCash.nsk_to_nk(this.f12183b.data(), brx.f12201b.data());
        brx.f12201b.updateHash();
        brx.mo42990e();
        return brx;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingExpandedSpendingKey)) {
            return false;
        }
        SaplingExpandedSpendingKey bru = (SaplingExpandedSpendingKey) obj;
        if (!this.f12182a.equals(bru.f12182a) || !this.f12183b.equals(bru.f12183b) || !this.f12184c.equals(bru.f12184c)) {
            z = false;
        }
        return z;
    }

    /* renamed from: b */
    public void mo42967b() {
        this.f12182a.clear();
        this.f12183b.clear();
        this.f12184c.clear();
        mo42969d();
    }

    public void clean() {
        super.clean();
        mo42967b();
    }

    /* renamed from: c */
    public byte[] mo42968c() {
        byte[] bArr = new byte[96];
        System.arraycopy(this.f12182a.data(), 0, bArr, 0, 32);
        System.arraycopy(this.f12183b.data(), 0, bArr, 32, 32);
        System.arraycopy(this.f12184c.data(), 0, bArr, 64, 32);
        return bArr;
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo42969d() {
        this.f12185d = Arrays.hashCode(mo42968c());
    }
}
