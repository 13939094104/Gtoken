package vdsMain;

import vdsMain.transaction.CMedianFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public final class TimeData {

    /* renamed from: a */
    public static long f143a;

    /* renamed from: b */
    private static Object f144b = new Object();

    /* renamed from: c */
    private static List<String> f145c = new ArrayList();

    /* renamed from: d */
    private static CMedianFilter f146d = new CMedianFilter(200, 0);

    /* renamed from: e */
    private static boolean f147e = false;

    /* renamed from: a */
    public static long m120a(long j) {
        return j >= 0 ? j : -j;
    }

    /* renamed from: a */
    public static long m119a() {
        return f143a;
    }

    /* renamed from: b */
    public static long m122b() {
        return getCurTime() + m119a();
    }

    //m123c
    public static long getCurTime() {
        return System.currentTimeMillis() / 1000;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0086, code lost:
        return;
     */
    /* renamed from: a */
    public static void m121a(String str, long j) {
        synchronized (f144b) {
            if (!f145c.contains(str)) {
                if (f145c.size() != 200) {
                    f145c.add(str);
                    f146d.mo44701a(Long.valueOf(j));
                    if (f146d.mo44702b() >= 5 && f146d.mo44702b() % 2 == 1) {
                        long a = f146d.mo44700a();
                        Vector c = f146d.mo44703c();
                        if (m120a(a) < 4200) {
                            f143a = a;
                        } else {
                            f143a = 0;
                            if (!f147e) {
                                boolean z = false;
                                Iterator it = c.iterator();
                                while (it.hasNext()) {
                                    long longValue = ((Long) it.next()).longValue();
                                    if (longValue != 0 && m120a(longValue) < 300) {
                                        z = true;
                                    }
                                }
                                if (!z) {
                                    f147e = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}