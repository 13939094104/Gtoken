package zcash;

import bitcoin.UInt256;

public class NoteEncryption {
    protected static native byte[] AttemptSaplingEncDecryption(byte[] bArr, byte[] bArr2, byte[] bArr3);

    /* renamed from: a */
    public static byte[] m4834a(byte[] bArr, UInt256 uInt256, UInt256 uInt2562) {
        return AttemptSaplingEncDecryption(bArr, uInt256.data(), uInt2562.data());
    }
}
