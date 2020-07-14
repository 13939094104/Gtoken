package vdsMain.transaction;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import bitcoin.UInt256;
import vdsMain.Collection;

//bqt
public class EmptyMerkleRoots<T extends UInt256> {

    /* renamed from: a */
    private ArrayList<T> f12122a = new ArrayList<>();

    /* renamed from: b */
    private int f12123b;

    public EmptyMerkleRoots(int i, @NonNull MerkleUint256<T> bqy) {
        this.f12123b = i;
        this.f12122a.add(bqy.mo42916a());
        for (int i2 = 1; i2 <= i; i2++) {
            int i3 = i2 - 1;
            T obj = this.f12122a.get(i3);
            this.f12122a.add(bqy.mo42919a(obj, obj, (long) i3));
        }
    }

    /* renamed from: a */
    public T mo42888a(long j) {
        return this.f12122a.get((int) j);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof EmptyMerkleRoots)) {
            return false;
        }
        EmptyMerkleRoots bqt = (EmptyMerkleRoots) obj;
        if (bqt.f12123b != this.f12123b) {
            return false;
        }
        return Collection.m11556b(this.f12122a, bqt.f12122a);
    }
}

