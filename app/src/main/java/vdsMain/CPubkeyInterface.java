package vdsMain;

import com.vc.libcommon.exception.AddressFormatException;

/* renamed from: kn */
public interface CPubkeyInterface {
    void Set(byte[] bArr) throws AddressFormatException;

    //mo9438a
    void initFromOtherPubKey(CPubkeyInterface pubkeyInterface) throws AddressFormatException;

    //mo9442c
    int checkAndGetTypeLength();

    //mo9445e
    boolean checkLength();

    /* renamed from: g */
    PubkeyType mo210g();

    //mo211h
    CTxDestination getCKeyID();

    String hexString();

    //mo9454m
    byte[] getByteArr();

    /* renamed from: n */
    byte[] mo9455n();
}
