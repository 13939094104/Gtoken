package vdsMain.transaction;


import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.*;
import vdsMain.Collection;

import java.io.IOException;
import java.util.*;

//bqv
public abstract class IncrementalMerkleTree<T extends UInt256> extends SeriableData {

    //f12124b
    protected static volatile EmptyMerkleRoots emptyroots;

    /* renamed from: a */
    protected int f12125a = 0;

    //f12126c
    protected T left = null;

    //f12127d
    protected T right = null;

    //f12128e
    protected Vector<T> parents = new Vector<>();

    /* renamed from: f */
    protected MerkleUint256<T> f12129f;

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public abstract IncrementalMerkleTree mo42901d();

    public IncrementalMerkleTree(int i, @NonNull MerkleUint256<T> bqy) {
        this.f12125a = i;
        this.f12129f = bqy;
        mo43005f();
    }

    /* renamed from: f */
    private void mo43005f() {
        if (emptyroots == null) {
            emptyroots = new EmptyMerkleRoots(this.f12125a, this.f12129f);
        }
    }

    /* renamed from: a */
    public void mo42893a(int i) {
        this.f12125a = i;
    }

    /* renamed from: a */
    public long mo42890a() {
        int i = 0;
        int i2 = this.left != null ? 1 : 0;
        if (this.right != null) {
            i2++;
        }
        Iterator it = this.parents.iterator();
        while (it.hasNext()) {
            if (it.next() != null) {
                i2 += 1 << (i + 1);
            }
            i++;
        }
        return (long) i2;
    }

    /* renamed from: a */
    public void mo42894a(T t) {
        if (!mo42895a((long) this.f12125a)) {
            T t2 = this.left;
            if (t2 == null) {
                this.left = t;
                return;
            }
            T t3 = this.right;
            if (t3 == null) {
                this.right = t;
                return;
            }
            T a = this.f12129f.mo42919a(t2, t3, 0);
            this.left = t;
            this.right = null;
            int i = 0;
            while (i < this.f12125a) {
                if (i < this.parents.size()) {
                    T obj = this.parents.get(i);
                    if (obj != null) {
                        int i2 = i + 1;
                        a = this.f12129f.mo42919a(obj, a, (long) i2);
                        this.parents.set(i, null);
                        i = i2;
                    } else {
                        this.parents.set(i, a);
                        return;
                    }
                } else {
                    this.parents.add(a);
                    return;
                }
            }
            return;
        }
        throw new IllegalStateException("tree is full");
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f12129f.mo42920a(this.left, streamWriter);
        this.f12129f.mo42920a(this.right, streamWriter);
        this.f12129f.mo42921a((List<T>) this.parents, streamWriter);
        if (!m10365g()) {
            throw new IOException("wfcheck failed.");
        }
    }

    public void onDecodeSerialData() throws IOException {
        this.left = this.f12129f.mo42923b((SeriableData) this);
        this.right = this.f12129f.mo42923b((SeriableData) this);
        this.f12129f.mo42924b(this.parents, this);
        if (!m10365g()) {
            throw new IOException("wfcheck failed.");
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IncrementalMerkleTree)) {
            return false;
        }
        IncrementalMerkleTree bqv = (IncrementalMerkleTree) obj;
        return bqv.f12125a == this.f12125a && Collection.m11556b(this.parents, bqv.parents) && DataTypeToolkit.m11497a((Object) this.left, (Object) bqv.left) && DataTypeToolkit.m11497a((Object) this.right, (Object) bqv.right);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public MerklePath mo42891a(Deque<T> deque) {
        if (this.left != null) {
            PathFiller bqz = new PathFiller(this.f12125a, deque, this.f12129f);
            Vector vector = new Vector();
            Vector vector2 = new Vector();
            if (this.right != null) {
                vector2.add(Boolean.valueOf(true));
                vector.add(this.left);
            } else {
                vector2.add(Boolean.valueOf(false));
                vector.add(bqz.mo42926a(0));
            }
            Iterator it = this.parents.iterator();
            int i = 1;
            while (it.hasNext()) {
                Object next = it.next();
                if (next != null) {
                    vector2.add(Boolean.valueOf(true));
                    vector.add(next);
                } else {
                    vector2.add(Boolean.valueOf(false));
                    vector.add(bqz.mo42926a((long) i));
                }
                i++;
            }
            while (i < this.f12125a) {
                vector2.add(Boolean.valueOf(false));
                vector.add(bqz.mo42926a((long) i));
                i++;
            }
            Vector vector3 = new Vector();
            Iterator<T> it2 = vector.iterator();
            while (it2.hasNext()) {
                vector3.add(Util.m10408a(this.f12129f.mo42925b(it2.next())));
            }
            Collections.reverse(vector3);
            Collections.reverse(vector2);
            return new MerklePath(vector3, vector2);
        }
        throw new IllegalStateException("can't create an authentication path for the beginning of the tree");
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public T mo42896b(int i) {
        return (T) mo42892a(i, new ArrayDeque());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public T mo42892a(int i, Deque<T> deque) {
        PathFiller bqz = new PathFiller(this.f12125a, deque, this.f12129f);
        T t = this.left;
        if (t == null) {
            t = (T) bqz.mo42926a(0);
        }
        T t2 = this.right;
        if (t2 == null) {
            t2 = (T) bqz.mo42926a(0);
        }
        T a = this.f12129f.mo42919a(t, t2, 0);
        Iterator<T> it = this.parents.iterator();
        int i2 = 1;
        while (it.hasNext()) {
            T next = it.next();
            if (next != null) {
                a = this.f12129f.mo42919a(next, a, (long) i2);
            } else {
                long j = (long) i2;
                a = this.f12129f.mo42919a(a, (T) bqz.mo42926a(j), j);
            }
            i2++;
        }
        while (i2 < i) {
            long j2 = (long) i2;
            a = this.f12129f.mo42919a(a, (T) bqz.mo42926a(j2), j2);
            i2++;
        }
        return a;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public boolean mo42895a(long j) {
        if (this.left == null || this.right == null || ((long) this.parents.size()) != j - 1) {
            return false;
        }
        Iterator it = this.parents.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public int mo42898c(int i) {
        if (this.left == null) {
            if (i == 0) {
                return 0;
            }
            i--;
        }
        if (this.right == null) {
            if (i == 0) {
                return 0;
            }
            i--;
        }
        Iterator it = this.parents.iterator();
        int i2 = 1;
        while (it.hasNext()) {
            if (it.next() == null) {
                if (i == 0) {
                    return i2;
                }
                i--;
            }
            i2++;
        }
        return i2 + i;
    }

    /* renamed from: g */
    private boolean m10365g() {
        if (this.parents.size() >= this.f12125a) {
            Log.infoObject((Object) this, "tree has too many parents");
            return false;
        } else if (!this.parents.isEmpty() && this.parents.lastElement() == null) {
            Log.infoObject((Object) this, "tree has non-canonical representation of parent");
            return false;
        } else if (this.left == null && this.right != null) {
            Log.infoObject((Object) this, "tree has non-canonical representation; right should not exist");
            return false;
        } else if (this.left != null || this.parents.isEmpty()) {
            return true;
        } else {
            Log.infoObject((Object) this, "tree has non-canonical representation; parents should not be unempty");
            return false;
        }
    }

    public String toString() {
        return mo42899c();
    }

    /* renamed from: b */
    public String mo42897b() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format(Locale.getDefault(), "[size : %d]", new Object[]{Integer.valueOf(this.f12125a)}));
        if (this.left != null) {
            stringBuffer.append(String.format(Locale.getDefault(), "[left: %s]", new Object[]{this.left.toString()}));
        }
        if (this.right != null) {
            stringBuffer.append(String.format(Locale.getDefault(), "[right: %s]", new Object[]{this.right.toString()}));
        }
        stringBuffer.append(String.format(Locale.getDefault(), "[parents: (%d) ", new Object[]{Integer.valueOf(this.parents.size())}));
        Iterator it = this.parents.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next != null) {
                stringBuffer.append(next.toString());
                stringBuffer.append(" , ");
            } else {
                stringBuffer.append("null , ");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* renamed from: c */
    public String mo42899c() {
        try {
            return StringToolkit.bytesToString(serialToStream());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: e */
    public IncrementalMerkleTree clone() {
        IncrementalMerkleTree d = mo42901d();
        d.f12129f = this.f12129f;
        d.f12125a = this.f12125a;
        d.left = this.f12129f.mo42918a(this.left);
        d.right = this.f12129f.mo42918a(this.right);
        Iterator<T> it = this.parents.iterator();
        while (it.hasNext()) {
            d.parents.add(this.f12129f.mo42918a(it.next()));
        }
        return d;
    }
}

