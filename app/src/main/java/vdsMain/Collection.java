package vdsMain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Collection<T> {
    //mo43676a
    public void swap(java.util.Collection<T> collection, java.util.Collection<T> collection2) {
        Vector vector = new Vector(collection.size());
        vector.addAll(collection);
        collection.clear();
        collection.addAll(collection2);
        collection2.clear();
        collection2.addAll(vector);
        vector.clear();
    }

    /* renamed from: a */
    public static Object m11552a(List list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(list.size() - 1);
    }

    /* renamed from: a */
    public static void m11554a(List list, List list2) {
        if (list != list2) {
            list.clear();
            if (list2 != null) {
                list.addAll(list2);
            }
        }
    }

    //m11553a
    public static void resize(List list, int i) {
        if (!list.isEmpty() && i != list.size()) {
            if (i == 0) {
                list.clear();
            } else if (list instanceof Vector) {
                ((Vector) list).setSize(i);
            } else {
                list.removeAll(new ArrayList(list.subList(i, list.size())));
            }
        }
    }

    /* renamed from: b */
    public static boolean m11556b(java.util.Collection collection, java.util.Collection collection2) {
        if (collection == collection2) {
            return true;
        }
        if (collection == null || collection.isEmpty()) {
            return collection2 == null && collection2.isEmpty();
        }
        if (((collection2 == null || collection2.isEmpty()) && (collection != null || !collection.isEmpty())) || collection.size() != collection2.size()) {
            return false;
        }
        Iterator it = collection2.iterator();
        for (Object next : collection) {
            Object next2 = it.next();
            if (next == null) {
                if (next2 != null) {
                    return false;
                }
            } else if (next2 == null) {
                if (next != null) {
                    return false;
                }
            } else if (!next.equals(next2)) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: a */
    public static boolean m11555a(java.util.Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
