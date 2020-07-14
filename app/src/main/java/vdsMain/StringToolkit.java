package vdsMain;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.util.Locale;

import com.orhanobut.logger.Logger;
import com.vc.libcommon.exception.DummyException;
import kotlin.UInt;
import org.apache.commons.io.IOUtils;
import org.spongycastle.pqc.math.linearalgebra.Matrix;

/* renamed from: gj */
public class StringToolkit {

    /* renamed from: a */
    protected static final char[] f12755a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    //m11528b
    public static boolean checkIsNull(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static String m11650a(String... strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Thread.currentThread().toString());
        stringBuffer.append(m11523a((Throwable) (strArr.length > 0 ? new DummyException(strArr[0]) : new DummyException())));
        //Log.info("stack", stringBuffer.toString());
        return stringBuffer.toString();
    }

    //910 m11648a
    public static String m11523a(Throwable th) {
        if (th == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder sb = new StringBuilder();
        sb.append(th.getClass().getName());
        sb.append(": ");
        sb.append(th.getMessage());
        stringBuffer.append(sb.toString());
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace != null) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("\nat ");
                    sb2.append(stackTraceElement.getClassName());
                    sb2.append(".");
                    sb2.append(stackTraceElement.getMethodName());
                    sb2.append("(");
                    sb2.append(stackTraceElement.getFileName());
                    sb2.append(":");
                    sb2.append(stackTraceElement.getLineNumber());
                    sb2.append(")");
                    stringBuffer.append(sb2.toString());
                }
            }
        }
        return stringBuffer.toString();
    }

    //m11522a
    public static int getCharSequenceLength(CharSequence charSequence) {
        if (charSequence == null) {
            return 0;
        }
        return charSequence.length();
    }

    /* isNull */
    public static boolean isNull(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    /* renamed from: a */
    public static boolean m11525a(String str, String str2) {
        if (str == null || str.isEmpty()) {
            if (str2 == null || str2.isEmpty()) {
                return true;
            }
            return false;
        } else if (str2 == null || str2.isEmpty()) {
            return false;
        } else {
            return str.equals(str2);
        }
    }

    //m11524a
    public static String bytesToString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            short s=bArr[i];
            short b = (short) (s & 0xff);
            int i2 = i * 2;
            char[] cArr2 = f12755a;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & Ascii.SI];
        }
        return new String(cArr).toUpperCase(Locale.US);
    }

    //915 m12078a
    public static byte[] getBytes(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            int digit = Character.digit(str.charAt(i), 16);
            int digit2 = Character.digit(str.charAt(i + 1), 16);
            if (digit2 == -1 || digit == -1) {
                return null;
            }
            bArr[i / 2] = (byte) ((digit << 4) + digit2);
        }
        return bArr;
    }

    /* renamed from: b */
    public static String m11527b(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        char[] cArr = new char[(bArr.length * 2)];
        int i = 0;
        int length = bArr.length;
        while (true) {
            length--;
            if (i >= bArr.length) {
                return new String(cArr).toUpperCase(Locale.US);
            }
            short b=bArr[length];
            b = (short) (b & 0xff);
            int i2 = i * 2;
            char[] cArr2 = f12755a;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & Ascii.SI];
            i++;
        }
    }

    /* renamed from: a */
    public static byte[] m11526a(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            int digit = Character.digit(str.charAt(i), 16);
            int digit2 = Character.digit(str.charAt(i + 1), 16);
            if (digit2 == -1 || digit == -1) {
                return null;
            }
            bArr[i / 2] = (byte) ((digit << 4) + digit2);
        }
        return bArr;
    }

    //m11529c
    public static String getBase64FromBytes(byte[] bArr) {
        char[] cArr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', Matrix.MATRIX_TYPE_RANDOM_LT, 'M', 'N', 'O', 'P', 'Q', Matrix.MATRIX_TYPE_RANDOM_REGULAR, 'S', 'T', Matrix.MATRIX_TYPE_RANDOM_UT, 'V', 'W', 'X', 'Y', Matrix.MATRIX_TYPE_ZERO, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', IOUtils.DIR_SEPARATOR_UNIX};
        StringBuffer stringBuffer = new StringBuffer(((bArr.length + 2) / 3) * 4);
        int i = 0;
        char c = 0;
        int i2 = 0;
        while (i < bArr.length) {
            int i3 = i + 1;
            short b=bArr[i];
            b = (short) (b & 0xff);
            switch (c) {
                case 0:
                    stringBuffer.append(cArr[b >> 2]);
                    i2 = (b & 3) << 4;
                    c = 1;
                    break;
                case 1:
                    stringBuffer.append(cArr[(b >> 4) | i2]);
                    i2 = (b & Ascii.SI) << 2;
                    c = 2;
                    break;
                case 2:
                    stringBuffer.append(cArr[(b >> 6) | i2]);
                    stringBuffer.append(cArr[b & 63]);
                    c = 0;
                    break;
            }
            i = i3;
        }
        if (c > 0) {
            stringBuffer.append(cArr[i2]);
            stringBuffer.append('=');
            if (c == 1) {
                stringBuffer.append('=');
            }
        }
        return stringBuffer.toString();
    }
}
