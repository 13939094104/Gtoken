package generic.crypto;

import vdsMain.StringToolkit;

public class Base58 {
    public static native byte[] decodeChecked(String str);

    public static native String encode(byte[] bArr, int i, int i2);

    public static native String encodeChecked(byte[] bArr, int i, int i2);

    //m900a
    public static String encodeToString(byte[] bArr) {
        return encode(bArr, 0, bArr.length);
    }

    //m901b
    public static String encodeChecked(byte[] bArr) {
        String a= StringToolkit.bytesToString(bArr);
        return encodeChecked(bArr, 0, bArr.length);
    }
}
