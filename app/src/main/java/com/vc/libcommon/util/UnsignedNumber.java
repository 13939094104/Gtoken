package com.vc.libcommon.util;

import vdsMain.UChar;

import java.io.IOException;

public abstract class UnsignedNumber {
    private native int compareValue(long j, long j2);

    /* renamed from: a */
    public abstract long mo18928a();

    public abstract int length();

    /* renamed from: a */
    public int mo18944a(Object obj) {
        long j;
        long a = mo18928a();
        if (obj instanceof UnsignedNumber) {
            j = ((UnsignedNumber) obj).mo18928a();
        } else if (obj instanceof Byte) {
            j = (long) UChar.m11567a(((Byte) obj).byteValue());
        } else if (obj instanceof Short) {
            j = (long) UShort.m864a(((Short) obj).shortValue());
        } else if (obj instanceof Integer) {
            j = UInt.m851a(((java.lang.Integer) obj).intValue());
        } else if (obj instanceof Integer) {
            j = (long) ((Integer) obj).getValue();
        } else if (obj instanceof Long) {
            j = ((Long) obj).longValue();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("didn't not support number type: ");
            sb.append(obj.getClass().getName());
            throw new ArithmeticException(sb.toString());
        }
        return compareValue(a, j);
    }

    /* renamed from: b */
    public boolean mo18945b(Object obj) {
        return mo18944a(obj) > 0;
    }

    /* renamed from: c */
    public boolean mo18946c(Object obj) {
        return mo18944a(obj) < 0;
    }

    public boolean equals(Object obj) {
        long a = mo18928a();
        boolean z = true;
        if (obj instanceof UChar) {
            if (a != ((UChar) obj).mo18928a()) {
                z = false;
            }
            return z;
        } else if (obj instanceof Byte) {
            if (a != ((long) UChar.m11567a(((Byte) obj).byteValue()))) {
                z = false;
            }
            return z;
        } else if (obj instanceof Short) {
            if (a != ((long) UShort.m864a(((Short) obj).shortValue()))) {
                z = false;
            }
            return z;
        } else if (obj instanceof UShort) {
            if (a != ((UShort) obj).mo18928a()) {
                z = false;
            }
            return z;
        } else if (obj instanceof Integer) {
            if (a != UInt.m851a(((java.lang.Integer) obj).intValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof Integer) {
            if (a != UInt.m851a(((Integer) obj).getValue())) {
                z = false;
            }
            return z;
        } else if (obj instanceof UInt) {
            if (a != ((UInt) obj).mo18928a()) {
                z = false;
            }
            return z;
        } else if (obj instanceof Long) {
            if (a != ((Long) obj).longValue()) {
                z = false;
            }
            return z;
        } else if (!(obj instanceof UInt64)) {
            return false;
        } else {
            if (a != ((long) ((byte) ((int) ((UInt64) obj).getValue())))) {
                z = false;
            }
            return z;
        }
    }

    /* renamed from: a */
    public void mo18929a(byte[] bArr) {
        int length = length();
        if (length() != bArr.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Data length ");
            sb.append(bArr.length);
            sb.append(" must be ");
            sb.append(length);
            try {
                throw new IOException(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}