package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DiversifierT;
import vdsMain.SaplingFullViewingKey;
import zcash.RustZCash;

import java.io.IOException;

//bsc
public class SaplingNote extends BaseNote {

    /* renamed from: b */
    public DiversifierT f12209b;

    /* renamed from: c */
    public UInt256 f12210c;

    /* renamed from: d */
    public UInt256 f12211d;

    public SaplingNote(DiversifierT brp, UInt256 uInt256, long j, UInt256 uInt2562, boolean... zArr) {
        super(j);
        if (zArr.length == 0 || zArr[0]) {
            this.f12209b = new DiversifierT(brp);
            this.f12210c = new UInt256((BaseBlob) uInt256);
            this.f12211d = new UInt256((BaseBlob) uInt2562);
            return;
        }
        this.f12209b = brp;
        this.f12210c = uInt256;
        this.f12211d = uInt2562;
    }

    public SaplingNote() {
        this.f12209b = new DiversifierT();
        this.f12210c = new UInt256();
        this.f12211d = new UInt256();
    }

    public SaplingNote(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof SaplingNote) {
            SaplingNote bsc = (SaplingNote) seriableData;
            this.f12209b = new DiversifierT(bsc.f12209b);
            this.f12210c = new UInt256((BaseBlob) bsc.f12210c);
            this.f12211d = new UInt256((BaseBlob) bsc.f12211d);
            return;
        }
        this.f12209b = new DiversifierT();
        this.f12210c = new UInt256();
        this.f12211d = new UInt256();
    }

    /* renamed from: b */
    public UInt256 mo43007b() {
        UInt256 uInt256 = new UInt256();
        if (!RustZCash.sapling_compute_cm(this.f12209b.mo42954a(), this.f12210c.data(), this.f12176a, this.f12211d.data(), uInt256.data())) {
            return null;
        }
        uInt256.updateHash();
        return uInt256;
    }

    /* renamed from: a */
    public UInt256 mo43006a(SaplingFullViewingKey brx, long j) {
        SaplingFullViewingKey brx2 = brx;
        UInt256 uInt256 = brx2.f12200a;
        UInt256 uInt2562 = brx2.f12201b;
        UInt256 uInt2563 = new UInt256();
        if (!RustZCash.sapling_compute_nf(this.f12209b.mo42954a(), this.f12210c.data(), this.f12176a, this.f12211d.data(), uInt256.data(), uInt2562.data(), j, uInt2563.data())) {
            return null;
        }
        uInt2563.updateHash();
        return uInt2563;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        super.writeSerialData(streamWriter);
        this.f12209b.serialToStream(streamWriter);
        this.f12210c.serialToStream(streamWriter);
        this.f12211d.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        super.onDecodeSerialData();
        this.f12209b.decodeSerialStream((SeriableData) this);
        this.f12210c.decodeSerialStream((SeriableData) this);
        this.f12211d.decodeSerialStream((SeriableData) this);
    }
}
