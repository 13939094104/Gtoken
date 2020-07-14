package vdsMain.peer;

import generic.network.AddressInfo;

import java.net.InetAddress;
import java.util.List;

public class BitcoinDNSFinder {
    /* renamed from: a */
    public AddressInfo[] mo43126a(String[] strArr, int i, List<String> list) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        DnsDiscover gfVar = new DnsDiscover();
        gfVar.mo43651a(strArr);
        InetAddress[] a = gfVar.mo43652a(5000);
        if (a == null) {
            return null;
        }
        AddressInfo[] addressInfoArr = new AddressInfo[a.length];
        for (int i2 = 0; i2 < a.length; i2++) {
            addressInfoArr[i2] = new AddressInfo(a[i2], i);
        }
        return addressInfoArr;
    }
}