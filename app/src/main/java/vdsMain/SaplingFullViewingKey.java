package vdsMain;


import bitcoin.BaseBlob;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import zcash.RustZCash;

import java.io.IOException;
import java.util.Arrays;

//brx
public class SaplingFullViewingKey extends SeriableData {

    /* renamed from: a */
    public UInt256 f12200a = new UInt256();

    /* renamed from: b */
    public UInt256 f12201b = new UInt256();

    /* renamed from: c */
    public UInt256 f12202c = new UInt256();

    /* renamed from: d */
    private int f12203d;

    public SaplingFullViewingKey() {
    }

    public SaplingFullViewingKey(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof SaplingFullViewingKey) {
            mo42984a((SaplingFullViewingKey) seriableData);
        }
    }

    /* renamed from: a */
    public void mo42984a(SaplingFullViewingKey SaplingFullViewingKey) {
        this.f12200a.set((BaseBlob) SaplingFullViewingKey.f12200a);
        this.f12201b.set((BaseBlob) SaplingFullViewingKey.f12201b);
        this.f12202c.set((BaseBlob) SaplingFullViewingKey.f12202c);
        this.f12203d = SaplingFullViewingKey.f12203d;
    }

    /* renamed from: a */
    public void mo42985a(byte[] bArr, int i) throws AddressFormatException {
        if (bArr == null) {
            mo42986b();
        } else if (bArr.length - i >= 96) {
            this.f12200a.set(bArr, i);
            this.f12201b.set(bArr, i + 32);
            this.f12202c.set(bArr, i + 64);
            mo42990e();
        } else {
            throw new AddressFormatException("Invalidate data length");
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt256(this.f12200a);
        streamWriter.writeUInt256(this.f12201b);
        streamWriter.writeUInt256(this.f12202c);
    }

    public void onDecodeSerialData() {
        this.f12200a.decodeSerialStream((SeriableData) this);
        this.f12201b.decodeSerialStream((SeriableData) this);
        this.f12202c.decodeSerialStream((SeriableData) this);
        mo42990e();
    }

    /* renamed from: a */
    public SaplingIncomingViewingKey mo42983a() {
        UInt256 uInt256 = new UInt256();
        RustZCash.crh_ivk(this.f12200a.data(), this.f12201b.data(), uInt256.data());
        return new SaplingIncomingViewingKey(uInt256);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingFullViewingKey)) {
            return false;
        }
        SaplingFullViewingKey SaplingFullViewingKey = (SaplingFullViewingKey) obj;
        if (!this.f12200a.equals(SaplingFullViewingKey.f12200a) || !this.f12201b.equals(SaplingFullViewingKey.f12201b) || !this.f12202c.equals(SaplingFullViewingKey.f12202c)) {
            z = false;
        }
        return z;
    }

    /* renamed from: b */
    public void mo42986b() {
        this.f12200a.clear();
        this.f12201b.clear();
        this.f12202c.clear();
        mo42990e();
    }

    /* renamed from: c */
    public SaplingFullViewingKey clone() {
        return new SaplingFullViewingKey(this);
    }

    /* renamed from: d */
    public byte[] mo42989d() {
        byte[] bArr = new byte[96];
        System.arraycopy(this.f12200a.data(), 0, bArr, 0, 32);
        System.arraycopy(this.f12201b.data(), 0, bArr, 32, 32);
        System.arraycopy(this.f12202c.data(), 0, bArr, 64, 32);
        return bArr;
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public void mo42990e() {
        this.f12203d = Arrays.hashCode(mo42989d());
    }

    public int hashCode() {
        return this.f12203d;
    }
}