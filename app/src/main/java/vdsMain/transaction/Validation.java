package vdsMain.transaction;

import vdsMain.VChainParam;
import vdsMain.VParams;

//bit
public class Validation {
    /* renamed from: a */
    public static int m9603a(VChainParam biq, long j, long j2) {
        VParams bjh = (VParams) biq.mo43961n();
        if (j != 0 && j2 >= 0 && j2 <= 23) {
            return (int) ((j - (j % ((long) bjh.f11732A))) + (((long) bjh.f11732A) * (j2 + 1)));
        }
        return 0;
    }

    /* renamed from: a */
    public static long m9604a(VChainParam biq, long j) {
        long a = biq.f11670Z.mo43715a(j);
        if (biq.f11671aa && a > 0 && a < biq.f11670Z.mo43714a()) {
            a = biq.f11670Z.mo43714a();
        }
        if (a == 0) {
            a = biq.f11673ac.mo43715a(j);
        }
        if (a < biq.f11672ab.mo43715a(j)) {
            a = biq.f11672ab.mo43715a(j);
        }
        if (a > 10000000) {
            return 10000000;
        }
        return a;
    }
}
