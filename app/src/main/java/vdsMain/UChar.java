package vdsMain;

import com.google.common.primitives.UnsignedBytes;
import com.vc.libcommon.util.UInt;
import com.vc.libcommon.util.UInt64;
import com.vc.libcommon.util.UShort;
import com.vc.libcommon.util.UnsignedNumber;
import com.vc.libcommon.util.Integer;
import java.util.Locale;

public class UChar extends UnsignedNumber {

    /* renamed from: a */
    protected byte f12766a;

    /* renamed from: a */
    public static short m11567a(byte b) {
        return (short) (b & UnsignedBytes.MAX_VALUE);
    }

    public int length() {
        return 1;
    }

    /* renamed from: b */
    public short mo43696b() {
        return m11567a(this.f12766a);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj instanceof UChar) {
            if (this.f12766a != ((UChar) obj).f12766a) {
                z = false;
            }
            return z;
        } else if (obj instanceof Byte) {
            if (this.f12766a != ((Byte) obj).byteValue()) {
                z = false;
            }
            return z;
        } else if (obj instanceof Short) {
            short shortValue = ((Short) obj).shortValue();
            if (shortValue > 255) {
                return false;
            }
            if (this.f12766a != ((byte) shortValue)) {
                z = false;
            }
            return z;
        } else if (obj instanceof UShort) {
            if (this.f12766a != ((byte) ((UShort) obj).mo18942b())) {
                z = false;
            }
            return z;
        } else if (obj instanceof java.lang.Integer) {
            int intValue = ((java.lang.Integer) obj).intValue();
            if (intValue > 255) {
                return false;
            }
            if (this.f12766a != ((byte) intValue)) {
                z = false;
            }
            return z;
        } else if (obj instanceof Integer) {
            int a = ((Integer) obj).getValue();
            if (a > 255) {
                return false;
            }
            if (this.f12766a != ((byte) a)) {
                z = false;
            }
            return z;
        } else if (obj instanceof UInt) {
            if (this.f12766a != ((byte) ((UInt) obj).mo18933b())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Long) {
            long longValue = ((Long) obj).longValue();
            if (longValue > 255) {
                return false;
            }
            if (this.f12766a != ((byte) ((int) longValue))) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof UInt64)) {
            return false;
        } else {
            if (this.f12766a != ((byte) ((int) ((UInt64) obj).getValue()))) {
                z = false;
            }
            return z;
        }
    }

    /* renamed from: a */
    public long mo18928a() {
        return (long) m11567a(this.f12766a);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "%d", new Object[]{Short.valueOf(mo43696b())});
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        this.f12766a = bArr[0];
    }
}

