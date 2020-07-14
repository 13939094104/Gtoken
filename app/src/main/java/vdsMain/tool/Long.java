package vdsMain.tool;
import com.vc.libcommon.util.Integer;


public final class Long extends Number implements Comparable<Long> {

    /* renamed from: a */
    public static final Class<Long> f12762a = (Class<Long>) long[].class.getComponentType();

    /* renamed from: b */
    private long f12763b;

    /* renamed from: a */
    public static int m11558a(long j, long j2) {
        int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    /* renamed from: b */
    public static int m11560b(long j) {
        return (int) (j ^ (j >>> 32));
    }

    /* renamed from: a */
    public static String m11559a(long j) {
        return java.lang.Long.toString(j);
    }

    public Long(long j) {
        this.f12763b = j;
    }

    public byte byteValue() {
        return (byte) ((int) this.f12763b);
    }

    public short shortValue() {
        return (short) ((int) this.f12763b);
    }

    public int intValue() {
        return (int) this.f12763b;
    }

    public long longValue() {
        return this.f12763b;
    }

    public float floatValue() {
        return (float) this.f12763b;
    }

    public double doubleValue() {
        return (double) this.f12763b;
    }

    public String toString() {
        return m11559a(this.f12763b);
    }

    public int hashCode() {
        return m11560b(this.f12763b);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj instanceof Integer) {
            if (this.f12763b != ((long) ((java.lang.Integer) obj).intValue())) {
                z = false;
            }
            return z;
        }
        boolean z2 = obj instanceof Short;
        if (z2) {
            if (this.f12763b != ((long) ((Short) obj).shortValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Long) {
            if (this.f12763b != ((Long) obj).longValue()) {
                z = false;
            }
            return z;
        } else if (z2) {
            if (this.f12763b != ((long) ((Short) obj).shortValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Integer) {
            if (this.f12763b != ((long) ((java.lang.Integer) obj).intValue())) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof Long)) {
            return false;
        } else {
            if (this.f12763b != ((Long) obj).longValue()) {
                z = false;
            }
            return z;
        }
    }

    /* renamed from: a */
    public int compareTo(Long grVar) {
        return m11558a(this.f12763b, grVar.f12763b);
    }

    /* renamed from: c */
    public synchronized void mo43680c(long j) {
        this.f12763b += j;
    }

    /* renamed from: b */
    public synchronized void mo43678b(Long grVar) {
        this.f12763b -= grVar.f12763b;
    }
}
