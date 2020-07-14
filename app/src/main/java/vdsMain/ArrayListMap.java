package vdsMain;

import androidx.annotation.Nullable;

import java.util.*;

public class ArrayListMap<K, V> {

    //f12756a
    //915 f13404a
    private HashMap<K, V> hashMap = new HashMap<>();

    //f12757b
    private List<V> valueList = new ArrayList();

    //mo43655a
    public synchronized V getValueSynchronized(@Nullable K k) {
        return this.hashMap.get(k);
    }

    //mo43657a
    public boolean addKeyValue(K k, V v2) {
        return addKeyValueSynchronized(k, v2);
    }

    //mo43660b
    //910 mo43723b
    //915 mo43965b
    public synchronized boolean addKeyValueSynchronized(K k, V v2) {
        if (this.hashMap.containsKey(k)) {
            return false;
        }
        this.hashMap.put(k, v2);
        this.valueList.add(v2);
        return true;
    }

    //910 mo43722b
    //mo43659b
    public boolean hasKey(K k) {
        return this.hashMap.containsKey(k);
    }

    //mo43662c
    //915 mo43967c
    public synchronized V removeAndGet(K k) {
        V remove;
        remove = this.hashMap.remove(k);
        if (remove != null) {
            this.valueList.remove(remove);
        }
        return remove;
    }

    //mo43656a
    //910 mo43719a
    public List<V> getValueList() {
        return this.valueList;
    }

    //mo43665f
//    public List<V> getValueList() {
//        return this.valueList;
//    }

    //mo43658b
    //910 mo43721b
    public boolean isValueListEmpty() {
        return this.valueList.isEmpty();
    }

    //mo43661c
    public int getValueSize() {
        return this.valueList.size();
    }

    //mo43663d
    public synchronized void clear() {
        this.hashMap.clear();
        this.valueList.clear();
    }

    //mo43664e
    public List<K> getKeyList() {
        return new ArrayList(this.hashMap.keySet());
    }


    //915 mo43971g
    public Set<Map.Entry<K, V>> getEntrySet() {
        return this.hashMap.entrySet();
    }

}
