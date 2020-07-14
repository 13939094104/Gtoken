package crypto;

import vdsMain.KeyInfoT;

public class CCrypto {

    //f687a
    private static volatile CCrypto cCrypto;

    private native boolean GenPubKey(byte[] bArr);

    private native void init();

    public native long ComputeShareKey(byte[] bArr, byte[] bArr2);

    public native byte[] GenIdByPubKey(byte[] bArr, byte[] bArr2);

    public native byte[] Sign(byte[] bArr);

    public native boolean VerifySign(byte[] bArr, byte[] bArr2, byte[] bArr3);

    //m898a
    public static CCrypto getInstance() {
        if (cCrypto == null) {
            cCrypto = new CCrypto();
        }
        return cCrypto;
    }

    //mo18961a
    public boolean GeneratePubKey(KeyInfoT ieVar) {
        return GenPubKey(ieVar.getBytes());
    }

    protected CCrypto() {
        init();
    }
}