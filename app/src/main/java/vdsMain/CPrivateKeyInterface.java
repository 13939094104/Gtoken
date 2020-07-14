package vdsMain;

import bitcoin.UInt256;

/* renamed from: km */
public interface CPrivateKeyInterface {
    //mo9404a
    byte[] signNativeByTransDataDefault(UInt256 uInt256);

    //mo9409d
    boolean IsCompressed();

    //mo9410e
    CPubkeyInterface getPubKey();

    //mo9412f
    byte[] getCopyBytes();

    //mo9413g
    void clearBytes();

    //mo9414h
    PrivateKeyType getPivateKeyType();
}
