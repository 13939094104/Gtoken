package vdsMain;


import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Arrays;

//brp
public class DiversifierT extends SeriableData {

    /* renamed from: a */
    private byte[] f12179a = new byte[11];

    public DiversifierT() {
    }

    public DiversifierT(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof DiversifierT) {
            System.arraycopy(((DiversifierT) seriableData).f12179a, 0, this.f12179a, 0, 11);
        }
    }

    /* renamed from: a */
    public byte[] mo42954a() {
        return this.f12179a;
    }

    /* renamed from: a */
    public void mo42952a(DiversifierT DiversifierT) {
        if (DiversifierT == null) {
            DataTypeToolkit.setBytesZero(this.f12179a);
            return;
        }
        byte[] bArr = DiversifierT.f12179a;
        byte[] bArr2 = this.f12179a;
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
    }

    /* renamed from: a */
    public void mo42953a(byte[] bArr, int i) {
        if (bArr == null) {
            DataTypeToolkit.setBytesZero(this.f12179a);
        } else if (bArr.length - i >= 11) {
            System.arraycopy(bArr, i, this.f12179a, 0, 11);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate DiversifierT buffer len: ");
            sb.append(bArr.length);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeBytes(this.f12179a);
    }

    public void onDecodeSerialData() throws IOException {
        int readBytes = readBytes(this.f12179a);
        if (readBytes < 11) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate DiversifierT buffer len: ");
            sb.append(readBytes);
            throw new IOException(sb.toString());
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DiversifierT)) {
            return false;
        }
        return Arrays.equals(this.f12179a, ((DiversifierT) obj).f12179a);
    }

    /* renamed from: b */
    public byte[] mo42955b() {
        return this.f12179a;
    }

    /* renamed from: c */
    public void mo42956c() {
        DataTypeToolkit.setBytesZero(this.f12179a);
    }

    public int hashCode() {
        return Arrays.hashCode(this.f12179a);
    }

    /* renamed from: d */
    public boolean mo42957d() {
        return DataTypeToolkit.isZeroBytes(this.f12179a);
    }
}
