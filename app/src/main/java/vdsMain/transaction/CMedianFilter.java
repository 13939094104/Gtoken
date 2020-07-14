package vdsMain.transaction;

import java.util.Collections;
import java.util.Locale;
import java.util.Vector;

public class CMedianFilter {

    /* renamed from: a */
    private Vector<Long> f13368a = new Vector<>();

    /* renamed from: b */
    private Vector<Long> f13369b = new Vector<>();

    /* renamed from: c */
    private int f13370c = 0;

    public CMedianFilter(int i, long j) {
        this.f13370c = i;
        this.f13368a.setSize(i);
        this.f13368a.add(Long.valueOf(j));
        this.f13369b = this.f13368a;
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public void mo44701a(Long l) {
        if (this.f13368a.size() == this.f13370c) {
            this.f13368a.remove(0);
        }
        this.f13368a.add(l);
        this.f13369b.clear();
        this.f13369b.addAll(this.f13368a);
        Collections.sort(this.f13369b);
    }

    /* renamed from: a */
    public long mo44700a() {
        int size = this.f13369b.size();
        if (size <= 0) {
            throw new IllegalStateException(String.format(Locale.getDefault(), "vSorted length %d must bigger than 0", new Object[]{Integer.valueOf(size)}));
        } else if (size == 1) {
            return ((Long) this.f13369b.get(size / 2)).longValue();
        } else {
            int i = size / 2;
            return (((Long) this.f13369b.get(i - 1)).longValue() + ((Long) this.f13369b.get(i)).longValue()) / 2;
        }
    }

    /* renamed from: b */
    public int mo44702b() {
        return this.f13368a.size();
    }

    /* renamed from: c */
    public Vector<Long> mo44703c() {
        return this.f13369b;
    }
}
