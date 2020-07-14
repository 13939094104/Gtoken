package vdsMain;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import org.spongycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

//abo
public class Sha256Hash implements Serializable, Comparable {

    /* renamed from: a */
    public static final Sha256Hash f214a = new Sha256Hash(new byte[32]);

    /* renamed from: b */
    private byte[] f215b;

    public Sha256Hash(byte[] bArr) {
        Preconditions.checkArgument(bArr.length == 32);
        this.f215b = bArr;
    }

    /* renamed from: a */
    public static Sha256Hash m185a(byte[] bArr) {
        try {
            return new Sha256Hash(MessageDigest.getInstance(McElieceCCA2KeyGenParameterSpec.SHA256).digest(bArr));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Sha256Hash)) {
            return false;
        }
        return Arrays.equals(this.f215b, ((Sha256Hash) obj).f215b);
    }

    public int hashCode() {
        byte[] bArr = this.f215b;
        return ((bArr[28] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | (bArr[31] & UnsignedBytes.MAX_VALUE) | ((bArr[30] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[29] & UnsignedBytes.MAX_VALUE) << 16);
    }

    public String toString() {
        return StringToolkit.bytesToString(this.f215b);
    }

    /* renamed from: a */
    public byte[] mo195a() {
        return this.f215b;
    }

    public int compareTo(Object obj) {
        Preconditions.checkArgument(obj instanceof Sha256Hash);
        int hashCode = hashCode();
        int hashCode2 = ((Sha256Hash) obj).hashCode();
        if (hashCode > hashCode2) {
            return 1;
        }
        return hashCode == hashCode2 ? 0 : -1;
    }
}
