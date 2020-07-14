package vdsMain.db;


import crypto.CCrypto;
import vdsMain.Base64Decoder;
import vdsMain.KeyInfoT;

//bkj
public class Db {

    //f11831a
    private static byte[] pubKeyBytes;

    //f11832b
    private static byte[] base64Bytes;

    /* renamed from: c */
    private static byte[] f11833c;

    //f11834f
    protected static Object lock = new Object();

    //m9801a
    public static void initPubKeybytes() {
        synchronized (lock) {
            if (pubKeyBytes == null) {
                KeyInfoT keyInfoT = new KeyInfoT(new byte[65]);
                CCrypto.getInstance().GeneratePubKey(keyInfoT);
                pubKeyBytes = keyInfoT.getBytes();
            }
        }
    }

    /* renamed from: b */
    public static byte[] m9805b() {
        return pubKeyBytes;
    }

    /* renamed from: c */
    public static byte[] m9806c() {
        return base64Bytes;
    }

    /* renamed from: a */
    public static void m9803a(byte[] bArr) {
        synchronized (lock) {
            base64Bytes = bArr;
        }
    }

    //m9802a
    public static void initBase64Bytes(String str) {
        synchronized (lock) {
            if (str != null) {
                if (!str.isEmpty()) {
                    base64Bytes = Base64Decoder.strToByteArr(str);
                }
            }
            base64Bytes = null;
        }
    }

    /* renamed from: d */
    public static byte[] m9807d() {
        return f11833c;
    }

    /* renamed from: b */
    public static void m9804b(byte[] bArr) {
        synchronized (lock) {
            f11833c = bArr;
        }
    }
}
