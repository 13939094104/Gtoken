package vdsMain;

import com.google.common.base.Ascii;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedBytes;
import org.mozilla.classfile.ClassFileWriter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* renamed from: gg */
public final class DataTypeToolkit {
    /* renamed from: a */
    public static int m11489a(int i, int i2, boolean z) {
        return z ? i | i2 : i & (~i2);
    }

    /* renamed from: a */
    public static int m11490a(boolean z) {
        return z ? 1 : 0;
    }

    /* renamed from: a */
    public static short m11491a(byte b) {
        return b >= 0 ? (short) b : (short) (b + 256);
    }

    //m11495a
    //915 m12047a
    public static boolean bitAndNotZero(int i, int i2) {
        return (i & i2) != 0;
    }

    /* renamed from: a */
    public static boolean m11496a(long j) {
        return j != 0;
    }

    /* renamed from: b */
    public static int m11503b(byte b) {
        return b & UnsignedBytes.MAX_VALUE;
    }

    /* renamed from: b */
    public static boolean m11505b(int i, int i2) {
        return (i & 255) == (i2 & 255);
    }

    /* renamed from: a */
    public static void m11494a(byte[] bArr, byte b) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = b;
        }
    }

    //m11498a
    public static boolean isZeroBytes(byte[] bArr) {
        for (byte b : bArr) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    //m11504b
    public static void setBytesZero(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = 0;
        }
    }

    //m11509c
    public static byte[] bytesCopy(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    //m11501a
    public static byte[] copyPartBytes(byte[] bArr, int srcStartPos, int copyLength) {
        if (copyLength <= 0) {
            return null;
        }
        byte[] bArr2 = new byte[copyLength];
        System.arraycopy(bArr, srcStartPos, bArr2, 0, copyLength);
        return bArr2;
    }

    //m11502a
    public static byte[] mergeArr(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0) {
            if (bArr2 == null || bArr2.length == 0) {
                return null;
            }
            byte[] bArr3 = new byte[bArr2.length];
            System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
            return bArr3;
        } else if (bArr2 == null || bArr2.length == 0) {
            byte[] bArr4 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr4, 0, bArr.length);
            return bArr4;
        } else {
            byte[] bArr5 = new byte[(bArr.length + bArr2.length)];
            System.arraycopy(bArr, 0, bArr5, 0, bArr.length);
            System.arraycopy(bArr2, 0, bArr5, bArr.length, bArr2.length);
            return bArr5;
        }
    }

    /* renamed from: a */
    public static byte[] m11500a(byte[] bArr, int i) {
        return copyPart(bArr, i, bArr.length - i);
    }

    //m11507b
    public static byte[] copyPart(byte[] bArr, int srcPos, int length) {
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, srcPos, bArr2, 0, length);
        return bArr2;
    }

    /* renamed from: b */
    public static boolean m11506b(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length < bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

//    /* renamed from: f */
    private static int m11514f(byte[] bArr) {
        int i = 0;
        for (byte a : bArr) {
            i += vdsMain.gz.Utils.m11590a(a);
        }
        return i;
    }

//    /* renamed from: c */
//    public static int m11508c(byte[] bArr, byte[] bArr2) {
//        int length = bArr.length;
//        if (length < bArr2.length) {
//            length = bArr2.length;
//        }
//        if (bArr == null) {
//            return bArr2 == null ? 0 : -1;
//        }
//        if (bArr2 == null) {
//            return bArr == null ? 0 : 1;
//        }
//        for (int i = 0; i < length; i++) {
//            if (length >= bArr.length) {
//                return Utils.m11590a(bArr2[i]) * -256;
//            }
//            if (length >= bArr2.length) {
//                return Utils.m11590a(bArr[i]) * ClassFileWriter.ACC_NATIVE;
//            }
//            short a = Utils.m11590a(bArr[i]);
//            short a2 = Utils.m11590a(bArr2[i]);
//            if (a < a2) {
//                return -1;
//            }
//            if (a > a2) {
//                return 1;
//            }
//        }
//        return 0;
//    }

//    /* renamed from: d */
    public static int m11511d(byte[] bArr, byte[] bArr2) {
        int length = bArr.length;
        if (length < bArr2.length) {
            length = bArr2.length;
        }
        if (bArr == null) {
            if (bArr2 == null) {
                return 0;
            }
            return -m11514f(bArr2);
        } else if (bArr2 != null) {
            for (int i = 0; i < length; i++) {
                if (length >= bArr.length) {
                    return vdsMain.gz.Utils.m11590a(bArr2[i]) * -256;
                }
                if (length >= bArr2.length) {
                    return vdsMain.gz.Utils.m11590a(bArr[i]) * ClassFileWriter.ACC_NATIVE;
                }
                short a =vdsMain.gz.Utils.m11590a(bArr[i]);
                short a2 = vdsMain.gz.Utils.m11590a(bArr2[i]);
                if (a != a2) {
                    return (a - a2) * 256;
                }
            }
            return 0;
        } else if (bArr == null) {
            return 0;
        } else {
            return m11514f(bArr);
        }
    }

    /* renamed from: a */
    public static byte[] m11499a(int i) {
        byte[] bArr = new byte[4];
        IntegerToBytes(i, bArr);
        return bArr;
    }

    public static byte[] getBytesAndReverse(int i) {
        byte[] bArr = new byte[4];
        IntegerToBytesReverse(i, bArr);
        return bArr;
    }

    public static byte[] trimZeroBytes(byte[] bytes) {
        List<Byte> byteList=new ArrayList<>();
        for (int i=0;i< bytes.length;i++) {
            Byte b=bytes[i];
            if(i==0||i==(bytes.length-1)){
                if(b!=(byte)0){
                    byteList.add(b);
                }
            }else {
                byteList.add(b);
            }
        }
        return Bytes.toArray(byteList);
    }


    //m11492a
    public static void IntegerToBytes(int i, byte[] bArr) {
        bArr[0] = (byte) ((i >> 24) & 255);
        bArr[1] = (byte) ((i >> 16) & 255);
        bArr[2] = (byte) ((i >> 8) & 255);
        bArr[3] = (byte) (i & 255);
    }

    public static void IntegerToBytesReverse(int i, byte[] bArr) {
        bArr[3] = (byte) ((i >> 24) & 255);
        bArr[2] = (byte) ((i >> 16) & 255);
        bArr[1] = (byte) ((i >> 8) & 255);
        bArr[0] = (byte) (i & 255);
    }

    /* renamed from: d */
    public static int m11510d(byte[] bArr) {
        return (bArr[3] & UnsignedBytes.MAX_VALUE) | ((bArr[0] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | ((bArr[1] & UnsignedBytes.MAX_VALUE) << 16) | ((bArr[2] & UnsignedBytes.MAX_VALUE) << 8);
    }

    /* renamed from: a */
    public static void m11493a(long j, byte[] bArr) {
        bArr[0] = (byte) ((int) ((j >> 56) & 255));
        bArr[1] = (byte) ((int) ((j >> 48) & 255));
        bArr[2] = (byte) ((int) ((j >> 40) & 255));
        bArr[3] = (byte) ((int) ((j >> 32) & 255));
        bArr[4] = (byte) ((int) ((j >> 24) & 255));
        bArr[5] = (byte) ((int) ((j >> 16) & 255));
        bArr[6] = (byte) ((int) ((j >> 8) & 255));
        bArr[7] = (byte) ((int) (j & 255));
    }

    /* renamed from: e */
    public static long m11512e(byte[] bArr) {
        return ((long) ((bArr[0] & UnsignedBytes.MAX_VALUE) << 56)) | ((long) ((bArr[1] & UnsignedBytes.MAX_VALUE) << 48)) | ((long) ((bArr[2] & UnsignedBytes.MAX_VALUE) << 40)) | ((long) ((bArr[3] & UnsignedBytes.MAX_VALUE) << 32)) | ((long) ((bArr[4] & UnsignedBytes.MAX_VALUE) << Ascii.CAN)) | ((long) ((bArr[5] & UnsignedBytes.MAX_VALUE) << 16)) | ((long) ((bArr[6] & UnsignedBytes.MAX_VALUE) << 8)) | ((long) (bArr[7] & UnsignedBytes.MAX_VALUE));
    }

    /* renamed from: a */
    public static boolean m11497a(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        if (obj2 == null) {
            return false;
        }
        return obj.equals(obj2);
    }

    /* renamed from: e */
    public static boolean m11513e(byte[] bArr, byte[] bArr2) {
        if (bArr2.length < bArr.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }
}
