package com.vtoken.application.util;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

//afs
public class RandomUtils {
    private static AtomicInteger f4784a = new AtomicInteger(1);

    /* renamed from: a */
    public static String m4943a() {
        long currentTimeMillis = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("MSG_");
        sb.append(currentTimeMillis);
        return sb.toString();
    }

    /* renamed from: a */
    public static int m4942a(int i, int i2) {
        return new Random().nextInt((i2 - i) + 1) + i;
    }

    /* renamed from: b */
    public static int m4944b() {
        return m4942a(0, 2147483547);
    }
}
