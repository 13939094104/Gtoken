package vdsMain.message;

import java.math.BigInteger;

public interface VersionMessageInterface {
    //mo42595a
    long getServiceInt();

    //mo42597b
    long getTime();

    //mo41280c
    int getSelfProtocalVersion();

    //mo42598d
    BigInteger getNonce();

    //mo42600f
    AddressMessage getAddressFrom();

    //mo42601g
    String getSubVersion();

    //mo42602h
    long getHeight();

    /* renamed from: i */
    boolean mo42603i();
}
