package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DataTypeToolkit;

import java.io.IOException;
import java.util.Locale;

//bjo
public class ContractParam extends SeriableData {

    /* renamed from: a */
    public byte[] f11785a;

    /* renamed from: b */
    public long f11786b;

    /* renamed from: c */
    public long f11787c;

    /* renamed from: d */
    public C3812a f11788d = C3812a.UNKNOWN;

    /* renamed from: e */
    protected byte[] f11789e = new byte[20];

    /* renamed from: bjo$a */
    /* compiled from: ContractParam */
    public enum C3812a {
        UNKNOWN(0),
        CREATE(1),
        CALL(2);


        /* renamed from: d */
        private int f11794d;

        private C3812a(int i) {
            this.f11794d = i;
        }

        /* renamed from: a */
        public int mo42518a() {
            return this.f11794d;
        }

        /* renamed from: a */
        public static C3812a m9742a(int i) {
            switch (i) {
                case 0:
                    return UNKNOWN;
                case 1:
                    return CREATE;
                case 2:
                    return CALL;
                default:
                    return UNKNOWN;
            }
        }
    }

    public ContractParam() {
    }

    public ContractParam(byte[] bArr, long j, long j2, C3812a aVar, byte[] bArr2) {
        this.f11785a = bArr;
        this.f11786b = j;
        this.f11787c = j2;
        this.f11788d = aVar;
        mo42517a(bArr2);
    }

    /* renamed from: a */
    public void mo42517a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            DataTypeToolkit.setBytesZero(this.f11789e);
        } else if (bArr.length == 20) {
            System.arraycopy(bArr, 0, this.f11789e, 0, 20);
        } else {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalidate pubkeyhash len %d.", new Object[]{Integer.valueOf(bArr.length)}));
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        writeVariableBytes(this.f11785a);
        writeUInt64(this.f11786b);
        writeUInt64(this.f11787c);
        writeUInt8(this.f11788d.mo42518a());
        writeBytes(this.f11789e);
    }

    public void onDecodeSerialData() {
        this.f11785a = readVariableBytes();
        this.f11786b = readUInt64().longValue();
        this.f11787c = readUInt64().longValue();
        this.f11788d = C3812a.m9742a(readUInt8());
        readBytes(this.f11789e);
    }
}
