package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.*;

//bqw
public abstract class IncrementalWitness<T extends UInt256> extends SeriableData {

    //f12130a
    protected IncrementalMerkleTree<T> tree;

    //f12131b
    protected Vector<T> filled = new Vector<>();

    //f12132c
    protected IncrementalMerkleTree<T> cursor;

    /* renamed from: d */
    protected int f12133d;

    /* renamed from: e */
    protected int f12134e;

    /* renamed from: f */
    protected MerkleUint256<T> f12135f;

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public abstract IncrementalMerkleTree mo42910d();

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public abstract IncrementalWitness mo42911e();

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public abstract Class<T> mo42913f();

    public IncrementalWitness(IncrementalWitness<T> incrementalWitness, @NonNull MerkleUint256<T> merkleUint256) {
        super((SeriableData) incrementalWitness);
        IncrementalMerkleTree<T> bqv = incrementalWitness.tree;
        if (bqv != null) {
            this.tree = bqv.clone();
        }
        if (!incrementalWitness.filled.isEmpty()) {
            Iterator<T> it = incrementalWitness.filled.iterator();
            while (it.hasNext()) {
                this.filled.add(merkleUint256.mo42918a(it.next()));
            }
        }
        IncrementalMerkleTree<T> bqv2 = incrementalWitness.cursor;
        if (bqv2 != null) {
            this.cursor = bqv2.clone();
        }
        this.f12133d = incrementalWitness.f12133d;
        this.f12134e = incrementalWitness.f12134e;
        this.f12135f = merkleUint256;
    }

    protected IncrementalWitness(int i, IncrementalMerkleTree<T> bqv, @NonNull MerkleUint256<T> bqy) {
        this.f12134e = i;
        if (bqv != null) {
            this.tree = bqv.clone();
        }
        this.f12135f = bqy;
    }

    public IncrementalWitness(int i, @NonNull MerkleUint256<T> bqy) {
        this.f12134e = i;
        this.f12135f = bqy;
        this.tree = mo42910d();
    }

    /* renamed from: a */
    public MerklePath mo42905a() {
        return this.tree.mo42891a(mo43029h());
    }

    /* renamed from: b */
    public long mo42907b() {
        return this.tree.mo42890a() - 1;
    }

    /* renamed from: c */
    public T mo42908c() {
        return this.tree.mo42892a(this.f12134e, mo43029h());
    }

    /* renamed from: a */
    public void mo42906a(T t) {
        IncrementalMerkleTree<T> bqv = this.cursor;
        if (bqv != null) {
            bqv.mo42894a(t);
            if (this.cursor.mo42895a((long) this.f12133d)) {
                this.filled.add(this.cursor.mo42896b(this.f12133d));
                this.cursor = null;
                return;
            }
            return;
        }
        this.f12133d = this.tree.mo42898c(this.filled.size());
        int i = this.f12133d;
        if (i >= this.f12134e) {
            throw new IllegalStateException(String.format(Locale.getDefault(), "tree is full: %d , %d", new Object[]{Integer.valueOf(this.f12133d), Integer.valueOf(this.f12134e)}));
        } else if (i == 0) {
            this.filled.add(t);
        } else {
            this.cursor = mo42910d();
            this.cursor.mo42894a(t);
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.tree.writeSerialData(streamWriter);
        streamWriter.writeObjectList(this.filled);
        streamWriter.writeOptionalObject(this.cursor);
        this.f12133d = this.tree.mo42898c(this.filled.size());
    }

    public void onDecodeSerialData() {
        this.tree.decodeSerialStream((SeriableData) this);
        this.f12135f.mo42922a((List<T>) this.filled, (SeriableData) this);
        this.cursor = (IncrementalMerkleTree) readOptionalObject(mo42913f());
        IncrementalMerkleTree<T> bqv = this.cursor;
        if (bqv != null) {
            bqv.mo42893a(this.f12134e);
        }
        this.f12133d = this.tree.mo42898c(this.filled.size());
    }

    /* renamed from: h */
    private Deque<T> mo43029h() {
        ArrayDeque arrayDeque = new ArrayDeque(this.filled);
        IncrementalMerkleTree<T> bqv = this.cursor;
        if (bqv != null) {
            arrayDeque.add(bqv.mo42896b(this.f12133d));
        }
        return arrayDeque;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("IncrementalWitness ---> ");
        stringBuffer.append(String.format(Locale.getDefault(), "[depth: %d]", new Object[]{Integer.valueOf(this.f12134e)}));
        stringBuffer.append(String.format(Locale.getDefault(), "[cursor_depth: %d]", new Object[]{Integer.valueOf(this.f12133d)}));
        stringBuffer.append("[tree: ");
        stringBuffer.append(this.tree.mo42897b());
        stringBuffer.append("]");
        stringBuffer.append(String.format(Locale.getDefault(), "[filled: (%d) ", new Object[]{Integer.valueOf(this.filled.size())}));
        Iterator it = this.filled.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next());
            stringBuffer.append(" , ");
        }
        stringBuffer.append("]");
        if (this.cursor != null) {
            stringBuffer.append("[cursor: ");
            stringBuffer.append(this.cursor.mo42897b());
            stringBuffer.append("]");
        }
        return stringBuffer.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return (obj instanceof IncrementalWitness) && ((IncrementalWitness) obj).f12134e == this.f12134e;
    }

    /* renamed from: g */
    public IncrementalWitness clone() {
        IncrementalWitness e = mo42911e();
        e.f12135f = this.f12135f;
        e.f12133d = this.f12133d;
        e.f12134e = this.f12134e;
        Iterator<T> it = this.filled.iterator();
        while (it.hasNext()) {
            e.filled.add(this.f12135f.mo42918a(it.next()));
        }
        IncrementalMerkleTree<T> bqv = this.tree;
        if (bqv != null) {
            e.tree = bqv.clone();
        }
        IncrementalMerkleTree<T> bqv2 = this.cursor;
        if (bqv2 != null) {
            e.cursor = bqv2.clone();
        }
        return e;
    }
}

