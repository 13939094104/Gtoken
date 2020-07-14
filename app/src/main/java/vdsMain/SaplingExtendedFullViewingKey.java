package vdsMain;


import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import zcash.RustZCash;

import java.io.IOException;
import java.util.Arrays;
//brv
public class SaplingExtendedFullViewingKey extends SeriableData {

    /* renamed from: a */
    public short f12186a;

    /* renamed from: b */
    public long f12187b;

    /* renamed from: c */
    public long f12188c;

    /* renamed from: d */
    public UInt256 f12189d = new UInt256();

    /* renamed from: e */
    public SaplingFullViewingKey f12190e = new SaplingFullViewingKey();

    /* renamed from: f */
    public UInt256 f12191f = new UInt256();

    /* renamed from: g */
    private int f12192g;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt8(this.f12186a);
        streamWriter.writeUInt32T(this.f12187b);
        streamWriter.writeUInt32T(this.f12188c);
        this.f12189d.serialToStream(streamWriter);
        this.f12190e.serialToStream(streamWriter);
        this.f12191f.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12186a = readUInt8();
        this.f12187b = readUInt32();
        this.f12188c = readUInt32();
        this.f12189d.decodeSerialStream((SeriableData) this);
        this.f12190e.decodeSerialStream((SeriableData) this);
        this.f12191f.decodeSerialStream((SeriableData) this);
        mo42973b();
    }

    /* renamed from: a */
    public Pair<DiversifierIndexT, SaplingPaymentAddress> mo42972a(DiversifierIndexT diversifierIndexT) {
        try {
            byte[] serialToStream = serialToStream();
            DiversifierIndexT bro2 = new DiversifierIndexT();
            byte[] bArr = new byte[43];
            if (!RustZCash.zip32_xfvk_address(serialToStream, diversifierIndexT.data(), bro2.data(), bArr)) {
                return null;
            }
            SaplingPaymentAddress saplingPaymentAddress = new SaplingPaymentAddress();
            saplingPaymentAddress.decodeSerialStream(bArr, 0);
            return new Pair<>(bro2, saplingPaymentAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public SaplingPaymentAddress mo42971a() {
        Pair a = mo42972a(new DiversifierIndexT());
        if (a != null) {
            return (SaplingPaymentAddress) a.value;
        }
        throw new NullPointerException("SaplingExtendedFullViewingKey::DefaultAddress(): No valid diversifiers out of 2^88!");
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingExtendedFullViewingKey)) {
            return false;
        }
        SaplingExtendedFullViewingKey brv = (SaplingExtendedFullViewingKey) obj;
        if (this.f12186a != brv.f12186a || this.f12187b != brv.f12187b || this.f12188c != brv.f12188c || !this.f12189d.equals(brv.f12189d) || !this.f12190e.equals(brv.f12190e) || !this.f12191f.equals(brv.f12191f)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42973b() {
        try {
            this.f12192g = Arrays.hashCode(serialToStream());
        } catch (Exception e) {
            this.f12192g = 0;
            e.printStackTrace();
        }
    }

    public int hashCode() {
        return this.f12192g;
    }
}