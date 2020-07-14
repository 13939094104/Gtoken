package vdsMain.model;

import bitcoin.UInt256;
import generic.exceptions.VerificationException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartialMerkleTree extends SeriableData {

    /* renamed from: a */
    private long f0a = 0;

    /* renamed from: b */
    private byte[] f1b = null;

    /* renamed from: c */
    private List<byte[]> f2c = new ArrayList();

    /* renamed from: aa$a */
    /* compiled from: PartialMerkleTree */
    static class C0001a {

        /* renamed from: a */
        public int f3a;

        /* renamed from: b */
        public int f4b;

        private C0001a() {
            this.f3a = 0;
            this.f4b = 0;
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.f0a);
        streamWriter.writeVariableInt((long) this.f2c.size());
        for (byte[] write : this.f2c) {
            streamWriter.write(write);
        }
        streamWriter.writeVariableInt((long) this.f1b.length);
        streamWriter.writeBytes(this.f1b);
    }

    public void onDecodeSerialData() {
        this.f0a = readUInt32();
        int b = readVariableInt().getIntValue();
        this.f2c = new ArrayList(b);
        for (int i = 0; i < b; i++) {
            this.f2c.add(readHash());
        }
        this.f1b = readBytes(readVariableInt().getIntValue());
    }

    /* renamed from: a */
    public List<UInt256> mo62a() throws VerificationException {
        ArrayList<byte[]> arrayList = new ArrayList<>();
        mo63a((List<byte[]>) arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (byte[] uInt256 : arrayList) {
            arrayList2.add(new UInt256(uInt256));
        }
        return arrayList2;
    }

    /* renamed from: a */
    private int m3a(int i) {
        return (int) (((this.f0a + ((long) (1 << i))) - 1) >> i);
    }

    /* renamed from: a */
    private byte[] m4a(int i, int i2, C0001a aVar, List<byte[]> list) throws VerificationException {
        int i3 = aVar.f3a;
        byte[] bArr = this.f1b;
        if (i3 < bArr.length * 8) {
            int i4 = aVar.f3a;
            aVar.f3a = i4 + 1;
            boolean e = Utils.m3460e(bArr, i4);
            if (i != 0 && e) {
                int i5 = i - 1;
                int i6 = i2 * 2;
                byte[] a = m4a(i5, i6, aVar, list);
                int i7 = i6 + 1;
                return vdsMain.Utils.m13312a(a, 0, 32, i7 < m3a(i5) ? m4a(i5, i7, aVar, list) : a, 0, 32);
            } else if (aVar.f4b < this.f2c.size()) {
                if (i == 0 && e) {
                    list.add(this.f2c.get(aVar.f4b));
                }
                List<byte[]> list2 = this.f2c;
                int i8 = aVar.f4b;
                aVar.f4b = i8 + 1;
                return (byte[]) list2.get(i8);
            } else {
                throw new VerificationException("CPartialMerkleTree overflowed its hash array");
            }
        } else {
            throw new VerificationException("CPartialMerkleTree overflowed its bits array");
        }
    }

    /* renamed from: a */
    public byte[] mo63a(List<byte[]> list) throws VerificationException {
        list.clear();
        long j = this.f0a;
        if (j == 0) {
            throw new VerificationException("Got a CPartialMerkleTree with 0 transactions");
        } else if (j > 133333) {
            throw new VerificationException("Got a CPartialMerkleTree with more transactions than is possible");
        } else if (((long) this.f2c.size()) > this.f0a) {
            throw new VerificationException("Got a CPartialMerkleTree with more hashes than transactions");
        } else if (this.f1b.length * 8 >= this.f2c.size()) {
            int i = 0;
            while (m3a(i) > 1) {
                i++;
            }
            C0001a aVar = new C0001a();
            byte[] a = m4a(i, 0, aVar, list);
            if ((aVar.f3a + 7) / 8 == this.f1b.length && aVar.f4b == this.f2c.size()) {
                return a;
            }
            throw new VerificationException("Got a CPartialMerkleTree that didn't need all the data it provided");
        } else {
            throw new VerificationException("Got a CPartialMerkleTree with fewer matched bits than hashes");
        }
    }
}

