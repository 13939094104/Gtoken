package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.UInt256;

import java.util.ArrayDeque;
import java.util.Deque;

//bqz
public class PathFiller<T extends UInt256> {

    /* renamed from: c */
    private static volatile EmptyMerkleRoots f12143c;

    /* renamed from: a */
    public Deque<T> f12144a = new ArrayDeque();

    /* renamed from: b */
    private int f12145b;

    /* renamed from: d */
    private MerkleUint256<T> f12146d;

    public PathFiller(int i, Deque<T> deque, @NonNull MerkleUint256<T> bqy) {
        this.f12145b = i;
        this.f12146d = bqy;
        m10398a();
        if (deque != null) {
            this.f12144a.addAll(deque);
        }
    }

    /* renamed from: a */
    private void m10398a() {
        if (f12143c == null) {
            f12143c = new EmptyMerkleRoots(this.f12145b, this.f12146d);
        }
    }

    /* renamed from: a */
    public T mo42926a(long j) {
        if (this.f12144a.size() > 0) {
            return this.f12144a.removeFirst();
        }
        return (T) f12143c.mo42888a(j);
    }
}