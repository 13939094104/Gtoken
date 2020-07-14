package vdsMain;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//brc
public class Util {

    /* renamed from: a */
    public static Vector<Boolean> m10407a(Vector<Byte> vector) {
        if (vector == null) {
            return new Vector<>(0);
        }
        Vector<Boolean> vector2 = new Vector<>(vector.size() * 8);
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            byte byteValue = ((Byte) it.next()).byteValue();
            for (int i = 0; i < 8; i++) {
                if (((byte) ((byteValue >> (7 - i)) & 1)) != 0) {
                    vector2.add(Boolean.valueOf(true));
                } else {
                    vector2.add(Boolean.valueOf(false));
                }
            }
        }
        return vector2;
    }

    /* renamed from: a */
    public static Vector<Boolean> m10408a(byte[] bArr) {
        if (bArr == null) {
            return new Vector<>(0);
        }
        Vector<Boolean> vector = new Vector<>(bArr.length * 8);
        for (byte b : bArr) {
            for (int i = 0; i < 8; i++) {
                if (((byte) ((b >> (7 - i)) & 1)) != 0) {
                    vector.add(Boolean.valueOf(true));
                } else {
                    vector.add(Boolean.valueOf(false));
                }
            }
        }
        return vector;
    }

    /* renamed from: a */
    public static Vector<Byte> m10406a(List<Boolean> list) {
        byte[] bArr = new byte[((list.size() + 7) / 8)];
        Vector<Byte> vector = new Vector<>(bArr.length);
        Iterator iterator = list.iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            int i2 = i / 8;
            bArr[i2] = (byte) (((((Boolean) iterator.next()).booleanValue() ? 1 : 0) << (7 - (i % 8))) | bArr[i2]);
            i++;
        }
        for (byte valueOf : bArr) {
            vector.add(Byte.valueOf(valueOf));
        }
        return vector;
    }

    /* renamed from: b */
    public static long m10409b(List<Boolean> list) {
        if (list.size() <= 64) {
            long j = 0;
            int size = list.size();
            int i = 0;
            for (Boolean booleanValue : list) {
                if (booleanValue.booleanValue()) {
                    j |= 1 << ((size - 1) - i);
                }
                i++;
            }
            return j;
        }
        throw new IllegalArgumentException("boolean vector can't be larger than 64 bits");
    }
}
