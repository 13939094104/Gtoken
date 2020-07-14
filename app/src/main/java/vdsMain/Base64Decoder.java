package vdsMain;

import org.apache.commons.io.IOUtils;
import org.spongycastle.pqc.math.linearalgebra.Matrix;

import java.io.*;

public class Base64Decoder extends FilterInputStream {

    /* renamed from: a */
    private static final char[] f12477a = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', Matrix.MATRIX_TYPE_RANDOM_LT, 'M', 'N', 'O', 'P', 'Q', Matrix.MATRIX_TYPE_RANDOM_REGULAR, 'S', 'T', Matrix.MATRIX_TYPE_RANDOM_UT, 'V', 'W', 'X', 'Y', Matrix.MATRIX_TYPE_ZERO, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', IOUtils.DIR_SEPARATOR_UNIX};

    /* renamed from: b */
    private static final int[] f12478b = new int[128];

    /* renamed from: c */
    private int f12479c;

    /* renamed from: d */
    private int f12480d;

    static {
        for (int i = 0; i < 64; i++) {
            f12478b[f12477a[i]] = i;
        }
    }

    public Base64Decoder(InputStream inputStream) {
        super(inputStream);
    }

    public int read() throws IOException {
        int read;
        do {
            read = this.in.read();
            if (read == -1) {
                return -1;
            }
        } while (Character.isWhitespace((char) read));
        this.f12479c++;
        if (read == 61) {
            return -1;
        }
        int i = f12478b[read];
        int i2 = (this.f12479c - 1) % 4;
        if (i2 == 0) {
            this.f12480d = i & 63;
            return read();
        } else if (i2 == 1) {
            int i3 = ((this.f12480d << 2) + (i >> 4)) & 255;
            this.f12480d = i & 15;
            return i3;
        } else if (i2 == 2) {
            int i4 = ((this.f12480d << 4) + (i >> 2)) & 255;
            this.f12480d = i & 3;
            return i4;
        } else if (i2 == 3) {
            return ((this.f12480d << 6) + i) & 255;
        } else {
            return -1;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr.length >= (i2 + i) - 1) {
            int i3 = 0;
            while (i3 < i2) {
                int read = read();
                if (read == -1 && i3 == 0) {
                    return -1;
                }
                if (read == -1) {
                    break;
                }
                bArr[i + i3] = (byte) read;
                i3++;
            }
            return i3;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The input buffer is too small: ");
        sb.append(i2);
        sb.append(" bytes requested starting at offset ");
        sb.append(i);
        sb.append(" while the buffer  is only ");
        sb.append(bArr.length);
        sb.append(" bytes long.");
        throw new IOException(sb.toString());
    }

    //m11070a
    public static byte[] strToByteArr(String str) {
        byte[] bArr;
        try {
            bArr = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            bArr = null;
        }
        Base64Decoder base64Decoder = new Base64Decoder(new ByteArrayInputStream(bArr));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) (((double) bArr.length) * 0.67d));
        try {
            byte[] bArr2 = new byte[4096];
            while (true) {
                int read = base64Decoder.read(bArr2);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr2, 0, read);
                } else {
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (IOException unused2) {
            return null;
        }
    }
}

