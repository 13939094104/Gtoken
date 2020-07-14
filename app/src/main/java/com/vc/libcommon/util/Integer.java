package com.vc.libcommon.util;

public class Integer {

    /* renamed from: a */
    public static final Class<Integer> f669a = (Class<Integer>) int[].class.getComponentType();

    //f670b
    private int value;

    /* renamed from: b */
    public static int m846b(int i) {
        return i ^ (i >>> 32);
    }

    /* renamed from: a */
    public static String m845a(int i) {
        return java.lang.Integer.toString(i);
    }

    //mo18923a
    public int getValue() {
        return this.value;
    }

    public String toString() {
        return m845a(this.value);
    }

    public int hashCode() {
        return m846b(this.value);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Long)) {
            return false;
        }
        if (this.value == ((Long) obj).intValue()) {
            z = true;
        }
        return z;
    }

    public void set(int i) {
        this.value = i;
    }
}