package vdsMain.transaction;

import java.math.BigDecimal;

public class CAmount {
    /* renamed from: b */
    public static boolean m10853b(long j) {
        return j >= 0 && j <= 2100000000000000L;
    }

    //m10851a
    public static Double toDecimalSatoshiDouble(Long l) {
        return new BigDecimal(l).divide(new BigDecimal(100000000)).doubleValue();
    }



    //m10852b
    //915 m11402b
    public static String toDecimalSatoshiString(Long l) {
        return new BigDecimal(l).divide(new BigDecimal(100000000)).toString();
    }

    public static String toDecimalUsdtString(Long l) {
        return new BigDecimal(l).divide(new BigDecimal(1000000)).toString();
    }

    public static String toDecimalTokenString(String l,int tokenDecimal) {
        return new BigDecimal(l).divide(new BigDecimal(Math.pow(10,tokenDecimal))).toString();
    }

    public static String toDecimalEthString(String l) {
        return new BigDecimal(l).divide(new BigDecimal("1000000000000000000")).toString();
    }

    public static String toDecimalTokenLongString(Double d,int tokenDecimal) {
        return new BigDecimal(d).multiply(new BigDecimal(Math.pow(10,tokenDecimal))).setScale(0,BigDecimal.ROUND_HALF_UP).toString();
    }

    //m10850a
    public static long toSatoshiLong(Double d) {
        return new BigDecimal(d).multiply(new BigDecimal(100000000)).setScale(1, 2).longValue();
    }

    //m10849a
    public static long toSatoshiLong(long j) {
        return new BigDecimal(j).multiply(new BigDecimal(100000000)).setScale(1, 2).longValue();
    }
}
