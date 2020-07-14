package vdsMain;

import java.util.Arrays;

public class Pair<K, V> {

    //f12764a
    //910 f12899a
    public K key;

    //f12765b
    //910 f12900b
    public V value;

    public Pair() {
    }

    public Pair(K k, V v2) {
        this.key = k;
        this.value = v2;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Pair)) {
            return false;
        }
        Pair gsVar = (Pair) obj;
        if (this.key.equals(gsVar.key) && this.value.equals(gsVar.value)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        byte[] bArr = new byte[8];
        K k = this.key;
        if (k != null) {
            int hashCode = k.hashCode();
            bArr[0] = (byte) (hashCode & 255);
            bArr[1] = (byte) ((hashCode >> 8) & 255);
            bArr[2] = (byte) ((hashCode >> 16) & 255);
            bArr[3] = (byte) ((hashCode >> 24) & 255);
        }
        V v2 = this.value;
        if (v2 != null) {
            int hashCode2 = v2.hashCode();
            bArr[4] = (byte) (hashCode2 & 255);
            bArr[5] = (byte) ((hashCode2 >> 8) & 255);
            bArr[6] = (byte) ((hashCode2 >> 16) & 255);
            bArr[7] = (byte) ((hashCode2 >> 24) & 255);
        }
        return Arrays.hashCode(bArr);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(getClass().getName());
        stringBuffer.append("<");
        if (this.key == null) {
            stringBuffer.append("[null] , ");
        } else {
            stringBuffer.append("[");
            stringBuffer.append(this.key.toString());
            stringBuffer.append("]");
        }
        if (this.value == null) {
            stringBuffer.append("[null] , ");
        } else {
            stringBuffer.append("[");
            stringBuffer.append(this.value.toString());
            stringBuffer.append("]");
        }
        stringBuffer.append(">");
        return stringBuffer.toString();
    }
}

