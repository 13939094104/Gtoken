package vdsMain.transaction;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.List;

//bqy
public class MerkleUint256<T extends UInt256>  {

    /* renamed from: b */
    protected static MerkleUint256<PedersenHash> f12138b;

    /* renamed from: a */
    protected C3824a f12139a;

    /* renamed from: bqy$a */
    /* compiled from: MerkleUint256 */
    public enum C3824a {
        SHA256COMPRESS,
        PEDERSENHASH
    }

    public MerkleUint256(C3824a aVar) {
        this.f12139a = aVar;
    }

    /* renamed from: a */
    public T mo42919a(T t, T t2, long j) {
        if (this.f12139a == C3824a.SHA256COMPRESS) {
            return (T) SHA256Compress.m10405a((SHA256Compress) t, (SHA256Compress) t2, j);
        }
        if (this.f12139a == C3824a.PEDERSENHASH) {
            return (T) PedersenHash.m10448a((PedersenHash) t, (PedersenHash) t2, j);
        }
        return null;
    }

    /* renamed from: a */
    public T mo42916a() {
        if (this.f12139a == C3824a.SHA256COMPRESS) {
            return (T) SHA256Compress.m10404a();
        }
        if (this.f12139a == C3824a.PEDERSENHASH) {
            return (T) PedersenHash.m10447a();
        }
        return null;
    }

    /* renamed from: a */
    public T mo42918a(T t) {
        if (t == null) {
            return null;
        }
        if (this.f12139a == C3824a.SHA256COMPRESS) {
            return (T) new SHA256Compress((SHA256Compress) t);
        }
        if (this.f12139a == C3824a.PEDERSENHASH) {
            return (T) new PedersenHash((PedersenHash) t);
        }
        return null;
    }

    /* renamed from: a */
    public void mo42920a(T t, StreamWriter streamWriter) throws IOException {
        if (t == null) {
            streamWriter.write((byte) 0);
            return;
        }
        streamWriter.write((byte) 1);
        ((SeriableData) t).serialToStream(streamWriter);
    }

    /* renamed from: a */
    public void mo42921a(List<T> list, StreamWriter streamWriter) throws IOException {
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) list.size());
        for (T a : list) {
            mo42920a(a, streamWriter);
        }
    }

    /* renamed from: a */
    public T mo42917a(SeriableData seriableData) {
        T t = this.f12139a == C3824a.SHA256COMPRESS ? (T) new SHA256Compress() : this.f12139a == C3824a.PEDERSENHASH ? (T) new PedersenHash() : null;
        if (t == null) {
            return null;
        }
        t.decodeSerialStream(seriableData);
        return t;
    }

    /* renamed from: a */
    public void mo42922a(List<T> list, SeriableData seriableData) {
        list.clear();
        int b = seriableData.readVariableInt().getIntValue();
        if (b >= 1) {
            for (int i = 0; i < b; i++) {
                list.add(mo42917a(seriableData));
            }
        }
    }

    /* renamed from: b */
    public T mo42923b(SeriableData seriableData) {
        if (seriableData.readByte() == 0) {
            return null;
        }
        return mo42917a(seriableData);
    }

    /* renamed from: b */
    public void mo42924b(List<T> list, SeriableData seriableData) {
        list.clear();
        int b = seriableData.readVariableInt().getIntValue();
        if (b > 0) {
            for (int i = 0; i < b; i++) {
                list.add(mo42923b(seriableData));
            }
        }
    }

    /* renamed from: b */
    public byte[] mo42925b(T t) {
        if (this.f12139a == C3824a.SHA256COMPRESS) {
            return ((SHA256Compress) t).data();
        }
        if (this.f12139a == C3824a.PEDERSENHASH) {
            return ((PedersenHash) t).data();
        }
        return null;
    }

    /* renamed from: b */
    public static MerkleUint256<PedersenHash> m10387b() {
        if (f12138b == null) {
            f12138b = new MerkleUint256<>(C3824a.PEDERSENHASH);
        }
        return f12138b;
    }
}
