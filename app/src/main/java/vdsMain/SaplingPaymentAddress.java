package vdsMain;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

//bsf
public class SaplingPaymentAddress extends SeriableData implements CTxDestination {

    /* renamed from: a */
    public DiversifierT f12218a = new DiversifierT();

    /* renamed from: b */
    public UInt256 f12219b = new UInt256();

    /* renamed from: c */
    private int f12220c = 0;

    public SaplingPaymentAddress() {
        mo43016d();
    }

    public SaplingPaymentAddress(byte[] bArr, int i) {
        if (bArr != null) {
            if (bArr.length - i >= 43) {
                this.f12218a.mo42953a(bArr, i);
                this.f12219b.set(bArr, i + 11);
            } else {
                throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalidate sapling payment addr len: %d", new Object[]{Integer.valueOf(bArr.length - i)}));
            }
        }
        mo43016d();
    }

    public SaplingPaymentAddress(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof SaplingPaymentAddress) {
            mo43013a((SaplingPaymentAddress) seriableData);
        } else if (seriableData instanceof SaplingExtendedFullViewingKey) {
            mo43012a((SaplingExtendedFullViewingKey) seriableData);
        } else {
            mo43016d();
        }
    }

    public SaplingPaymentAddress(DiversifierT DiversifierT, UInt256 uInt256) {
        this.f12218a.mo42952a(DiversifierT);
        this.f12219b.set((BaseBlob) uInt256);
        mo43016d();
    }

    /* renamed from: a */
    public void mo43013a(SaplingPaymentAddress SaplingPaymentAddress) {
        if (SaplingPaymentAddress == null) {
            this.f12218a.mo42956c();
            this.f12219b.clear();
            this.f12220c = 0;
        } else {
            this.f12218a.mo42952a(SaplingPaymentAddress.f12218a);
            this.f12219b.set((BaseBlob) SaplingPaymentAddress.f12219b);
        }
        mo43016d();
    }

    /* renamed from: a */
    public void mo43014a(byte[] bArr, int i) {
        if (bArr == null) {
            this.f12218a.mo42956c();
            this.f12219b.clear();
        } else {
            this.f12218a.mo42953a(bArr, i);
            this.f12219b.set(bArr, i + 11);
        }
        mo43016d();
    }

    /* renamed from: a */
    public void mo43012a(SaplingExtendedFullViewingKey brv) {
        mo43013a(brv.mo42971a());
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f12218a.serialToStream(streamWriter);
        this.f12219b.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12218a.decodeSerialStream((SeriableData) this);
        this.f12219b.decodeSerialStream((SeriableData) this);
        mo43016d();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingPaymentAddress) || obj.hashCode() != this.f12220c) {
            return false;
        }
        SaplingPaymentAddress SaplingPaymentAddress = (SaplingPaymentAddress) obj;
        if (!SaplingPaymentAddress.f12219b.equals(this.f12219b) || !SaplingPaymentAddress.f12218a.equals(this.f12218a)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo43016d() {
        byte[] bArr = new byte[43];
        System.arraycopy(this.f12218a.mo42954a(), 0, bArr, 0, 11);
        System.arraycopy(this.f12219b.data(), 0, bArr, 11, 32);
        this.f12220c = Arrays.hashCode(bArr);
    }

    public int hashCode() {
        return this.f12220c;
    }

    public boolean isNull() {
        return this.f12218a.mo42957d() && this.f12219b.isNull();
    }

    /* renamed from: e */
    public void mo43017e() {
        this.f12218a.mo42956c();
        this.f12219b.clear();
    }

    //getCTxDestinationType
    public CTxDestinationType getCTxDestinationType() {
        return CTxDestinationType.ANONYMOUST_KEY;
    }

    public byte[] data() {
        byte[] bArr = new byte[43];
        m10500b(bArr, 0);
        return bArr;
    }

    /* renamed from: b */
    private void m10500b(byte[] bArr, int i) {
        System.arraycopy(this.f12218a.mo42954a(), 0, bArr, i, 11);
        System.arraycopy(this.f12219b.data(), 0, bArr, i + 11, 32);
    }

    /* renamed from: a */
    public void writeTypeAndData(StreamWriter streamWriter) throws IOException {
        streamWriter.write((byte) getCTxDestinationType().getValue());
        super.serialToStream(streamWriter);
    }

    /* renamed from: a */
    public void mo9424a(SeriableData seriableData, boolean z) throws IOException {
        if (z) {
            CTxDestinationType a = CTxDestinationType.getDesType(seriableData.readByte());
            if (a != CTxDestinationType.ANONYMOUST_KEY) {
                throw new IOException(String.format(Locale.getDefault(), "CTxDestination type %s not ANONYMOUST_KEY", new Object[]{a.name()}));
            }
        }
        super.decodeSerialStream(seriableData);
        mo43016d();
    }

    /* renamed from: c */
    public String getHash() {
        byte[] bArr = new byte[44];
        bArr[0] = (byte) getCTxDestinationType().getValue();
        m10500b(bArr, 1);
        return StringToolkit.bytesToString(bArr);
    }

    /* renamed from: b */
    public CTxDestination clone() {
        return new SaplingPaymentAddress(this);
    }
}
