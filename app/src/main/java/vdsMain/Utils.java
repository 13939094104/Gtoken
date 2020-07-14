package vdsMain;

import android.content.ContentValues;
import android.database.Cursor;
import bitcoin.CPubKey;
import bitcoin.CSHA256;
import bitcoin.UInt256;
import com.google.common.primitives.UnsignedBytes;
import vdsMain.transaction.COutPoint;
import vdsMain.transaction.Utxo;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;

//C3903md
public class Utils {

    //m13303a
    public static long readLongFromByteBuffer(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[4];
        byteBuffer.checkAndReadToBytesFromZeroIndex(bArr);
        return net.bither.bitherj.utils.Utils.m3444a(bArr, 0);
    }

    /* renamed from: a */
    public static int m13302a(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return (int) (((((long) bArr[i3 + 1]) & 255) << 24) | (((long) bArr[i]) & 255) | ((((long) bArr[i2]) & 255) << 8) | ((((long) bArr[i3]) & 255) << 16));
    }

    /* renamed from: b */
    public static int m13313b(ByteBuffer gcVar) {
        return m13302a(readFromByteBufferToBytes(gcVar, 4), 0);
    }

    /* renamed from: c */
    public static long m13317c(ByteBuffer gcVar) {
        return net.bither.bitherj.utils.Utils.m3444a(readFromByteBufferToBytes(gcVar, 4), 0);
    }

    /* renamed from: d */
    public static long m13318d(ByteBuffer gcVar) {
        return net.bither.bitherj.utils.Utils.m3452b(readFromByteBufferToBytes(gcVar, 8), 0);
    }

    /* renamed from: a */
    public static byte[] m13309a(int i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    /* renamed from: b */
    public static int m13314b(byte[] bArr, int i) {
        return (bArr[i + 1] & UnsignedBytes.MAX_VALUE) | ((bArr[i] & UnsignedBytes.MAX_VALUE) << 8);
    }

    //m13319e
    public static int getTwoBytesInt(ByteBuffer gcVar) {
        byte[] bytes = readFromByteBufferToBytes(gcVar, 2);
        short b1= (short) (bytes[1] & 0xff);
        short b2= (short) (((short)(bytes[0] & 0xff)) << 8);
        return b1 | b2;
    }

    /* renamed from: f */
    public static int m13320f(ByteBuffer gcVar) {
        byte[] a = readFromByteBufferToBytes(gcVar, 2);
        return (a[0] & UnsignedBytes.MAX_VALUE) | ((a[1] & UnsignedBytes.MAX_VALUE) << 8);
    }

    //m13310a
    public static byte[] readFromByteBufferToBytes(ByteBuffer byteBuffer, int i) {
        if (byteBuffer.availReadLength() < i) {
            return null;
        }
        byte[] bArr = new byte[i];
        byteBuffer.checkAndReadToBytesFromZeroIndex(bArr);
        return bArr;
    }

    //m13315b
    public static String readFromByteBufferToUTF8String(ByteBuffer byteBuffer, int readLength) {
        return readFromByteBufferToString(byteBuffer, readLength, "utf-8");
    }

    //m13305a
    public static String readFromByteBufferToString(ByteBuffer byteBuffer, int readLength, String str) {
        if (((long) byteBuffer.availReadLength()) < ((long) readLength)) {
            return null;
        }
        byte[] bArr = new byte[readLength];
        byteBuffer.checkAndReadToBytesFromZeroIndex(bArr);
        for (int length = bArr.length - 1; length > 0; length--) {
            if (bArr[length] != 0) {
                try {
                    return new String(bArr, 0, length + 1, str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /* renamed from: a */
    public static byte[] m13311a(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        if (i + i2 <= bArr.length) {
            System.arraycopy(bArr, i, bArr2, 0, i2);
        } else {
            System.arraycopy(bArr, i, bArr2, 0, bArr2.length - i);
        }
        return bArr2;
    }

    /* renamed from: a */
    public static boolean m13308a(byte[] bArr) {
        return CPubKey.m417c(bArr);
    }

    /* renamed from: b */
    public static boolean m13316b(byte[] bArr) {
        if (bArr.length < 9 || bArr.length > 73 || bArr[0] != 48 || bArr[1] != bArr.length - 3) {
            return false;
        }
        int b = DataTypeToolkit.m11503b(bArr[3]);
        int i = b + 5;
        if (i >= bArr.length) {
            return false;
        }
        int b2 = DataTypeToolkit.m11503b(bArr[i]);
        if (b + b2 + 7 != bArr.length || bArr[2] != 2 || b == 0 || (bArr[4] & 128) != 0) {
            return false;
        }
        if ((b > 1 && bArr[4] == 0 && (bArr[5] & 128) == 0) || bArr[b + 4] != 2 || b2 == 0) {
            return false;
        }
        int i2 = b + 6;
        if ((bArr[i2] & 128) != 0) {
            return false;
        }
        if (b2 > 1 && bArr[i2] == 0 && (bArr[b + 7] & 128) == 0) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    public static byte[] m13312a(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        CSHA256 csha256 = new CSHA256();
        csha256.Write(bArr, i, i2);
        csha256.Write(bArr2, i3, i4);
        byte[] bArr3 = new byte[32];
        csha256.mo9463b(bArr3);
        csha256.mo9459a(bArr3);
        csha256.mo9463b(bArr3);
        return bArr3;
    }

    /* renamed from: a */
    public static UInt256 m13304a(Cursor cursor, int i, boolean z) {
        String string = cursor.getString(i);
        if (string != null && !string.isEmpty()) {
            UInt256 uInt256 = new UInt256();
            uInt256.setHash(string);
            return uInt256;
        } else if (z) {
            return new UInt256();
        } else {
            return null;
        }
    }

    /* renamed from: a */
    public static final void m13307a(ContentValues contentValues, String str, UInt256 uInt256, String str2) {
        if (uInt256 == null) {
            contentValues.put(str, str2);
        } else {
            contentValues.put(str, uInt256.hashString());
        }
    }

    //m13306a
    public static List<COutPoint> getCoutPointUtxoFromUtxoList(List<Utxo> list) {
        if (list == null) {
            return null;
        }
        Vector vector = new Vector(list.size());
        for (Utxo utxo : list) {
            vector.add(utxo.getCOutPoint());
        }
        return vector;
    }
}
