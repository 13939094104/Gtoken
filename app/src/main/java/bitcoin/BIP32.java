package bitcoin;

public class BIP32 {
    private static native byte[] hash(byte[] bArr, int i, byte b, byte[] bArr2);

    private static native byte[] hash_(byte[] bArr, int i, byte[] bArr2);

    /* renamed from: a */
    public static final byte[] m379a(UInt256 uInt256, int i, byte[] bArr) {
        return hash_(uInt256.mData, i, bArr);
    }

    /* renamed from: a */
    public static final byte[] m378a(UInt256 uInt256, int i, byte b, byte[] bArr) {
        return hash(uInt256.mData, i, b, bArr);
    }
}
