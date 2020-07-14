package vdsMain;

import java.util.Collection;
import java.util.Vector;

public class SparseArray<E> extends android.util.SparseArray<E> {
    /* renamed from: a */
    //mo43694a
    //915 mo44000a
    public boolean isNotNull(int i) {
        return get(i) != null;
    }

    /* renamed from: a */
    //mo43693a
    public Collection<E> getVector() {
        int size = size();
        Vector vector = new Vector(size);
        if (isZeroSize()) {
            return vector;
        }
        for (int i = 0; i < size; i++) {
            Object valueAt = valueAt(i);
            if (valueAt != null) {
                vector.add(valueAt);
            }
        }
        return vector;
    }

    /* renamed from: b */
    //mo43695b
    public boolean isZeroSize() {
        return size() == 0;
    }
}
