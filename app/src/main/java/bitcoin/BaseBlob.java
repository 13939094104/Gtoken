package bitcoin;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import net.bither.bitherj.utils.Utils;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;

public class BaseBlob extends SeriableData {
    public byte[] mData;
    protected int mHashCode;
    protected int mWidth;

    public BaseBlob(byte[] bArr) {
        this.mWidth = bArr.length;
        this.mData = new byte[bArr.length];
        System.arraycopy(bArr, 0, this.mData, 0, bArr.length);
        updateHash();
    }

    protected BaseBlob(byte[] bArr, boolean z) {
        this.mWidth = bArr.length;
        if (z) {
            this.mData = bArr;
        } else {
            int i = this.mWidth;
            this.mData = new byte[i];
            System.arraycopy(bArr, 0, this.mData, 0, i);
        }
        updateHash();
    }

    public BaseBlob(int i) {
        if (i >= 1) {
            this.mWidth = i / 8;
            this.mData = new byte[this.mWidth];
            updateHash();
            return;
        }
        throw new IllegalArgumentException("Bits must bigger than 0");
    }

    public BaseBlob(BaseBlob baseBlob) {
        super((SeriableData) baseBlob);
        this.mWidth = baseBlob.mWidth;
        this.mHashCode = baseBlob.mHashCode;
        int i = this.mWidth;
        this.mData = new byte[i];
        System.arraycopy(baseBlob.mData, 0, this.mData, 0, i);
    }

    public void clone(BaseBlob baseBlob) {
        int i = baseBlob.mWidth;
        int i2 = this.mWidth;
        if (i == i2) {
            byte[] bArr = baseBlob.mData;
            if (bArr.length == i2) {
                System.arraycopy(bArr, 0, this.mData, 0, i2);
                this.mHashCode = baseBlob.mHashCode;
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Src blob ");
            sb.append(baseBlob.getClass().getSimpleName());
            sb.append(" data length ");
            sb.append(baseBlob.mData.length);
            sb.append(" not equal with ");
            sb.append(this.mWidth);
            throw new IllegalArgumentException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Src blob ");
        sb2.append(baseBlob.getClass().getSimpleName());
        sb2.append(" width ");
        sb2.append(baseBlob.mWidth);
        sb2.append(" not equal with ");
        sb2.append(this.mWidth);
        throw new IllegalArgumentException(sb2.toString());
    }

    /* access modifiers changed from: protected */
    public Object clone() {
        try {
            return getClass().getConstructor(new Class[]{Byte[].class}).newInstance(new Object[]{this.mData});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNull() {
        byte[] bArr = this.mData;
        if (bArr == null) {
            return true;
        }
        for (byte b : bArr) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public void setNull() {
        DataTypeToolkit.setBytesZero(this.mData);
        updateHash();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BaseBlob) {
            if (obj.hashCode() != hashCode()) {
                return false;
            }
            BaseBlob baseBlob = (BaseBlob) obj;
            if (this.mWidth != baseBlob.mWidth) {
                return false;
            }
            return Arrays.equals(this.mData, baseBlob.mData);
        } else if (obj instanceof byte[]) {
            return Arrays.equals(this.mData, (byte[]) obj);
        } else if (!(obj instanceof String)) {
            return false;
        } else {
            return Arrays.equals(this.mData, StringToolkit.getBytes((String) obj));
        }
    }

    public int hashCode() {
        return this.mHashCode;
    }

    public void updateHash() {
        this.mHashCode = (this.mWidth * 31) + Arrays.hashCode(this.mData);
    }

    public String getHex() {
        byte[] bArr = this.mData;
        if (bArr != null) {
            return StringToolkit.m11527b(bArr);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Data for ");
        sb.append(getClass().getSimpleName());
        sb.append(" was null!");
        throw new NullPointerException(sb.toString());
    }

    public String hashString() {
        return Utils.m3445a(this.mData).toLowerCase();
    }

    public void setHex(String str) {
        if (str == null || str.isEmpty()) {
            DataTypeToolkit.setBytesZero(this.mData);
            return;
        }
        byte[] bArr = this.mData;
        if (bArr != null) {
            DataTypeToolkit.setBytesZero(bArr);
            char[] charArray = str.toLowerCase(Locale.US).toCharArray();
            int i = 0;
            int i2 = 0;
            while (charArray[i2] == ' ') {
                i2++;
            }
            if (charArray[i2] == '0' && charArray[i2 + 1] == 'x') {
                i2 += 2;
            }
            int i3 = i2;
            while (i3 < charArray.length && ((charArray[i3] >= '0' && charArray[i3] <= '9') || (charArray[i3] >= 'a' && charArray[i3] <= 'f'))) {
                i3++;
            }
            int i4 = i3 - 1;
            if (i4 - i2 > 0) {
                while (i4 >= i2) {
                    int i5 = i4 - 1;
                    this.mData[i] = (byte) Character.digit(charArray[i4], 16);
                    if (i5 >= i2) {
                        byte[] bArr2 = this.mData;
                        int i6 = i5 - 1;
                        bArr2[i] = (byte) (bArr2[i] | (((byte) Character.digit(charArray[i5], 16)) << 4));
                        i++;
                        i4 = i6;
                    } else {
                        i4 = i5;
                    }
                }
                updateHash();
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Data for ");
        sb.append(getClass().getSimpleName());
        sb.append(" was null!");
        throw new NullPointerException(sb.toString());
    }

    public BaseBlob setHash(String str) {
        this.mData = Utils.getReverseStringBytes(str);
        this.mWidth = this.mData.length;
        updateHash();
        return this;
    }

    public String toString() {
        return getHex();
    }

    public int width() {
        return this.mWidth;
    }

    public final byte[] data() {
        return this.mData;
    }

    public void setData(byte[] bArr) {
        if (bArr.length == this.mWidth) {
            System.arraycopy(bArr, 0, this.mData, 0, bArr.length);
            updateHash();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Data length ");
        sb.append(bArr.length);
        sb.append(" must same as blob length ");
        sb.append(this.mWidth);
        throw new IllegalArgumentException(sb.toString());
    }

    public void setData(byte[] bArr, int i) {
        int i2 = this.mWidth;
        if (i + i2 <= bArr.length) {
            System.arraycopy(bArr, i, this.mData, 0, i2);
            updateHash();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Out of array range --> from ");
        sb.append(i);
        sb.append(" , to ");
        sb.append(i + this.mWidth);
        sb.append(", array len ");
        sb.append(bArr.length);
        throw new IllegalArgumentException(sb.toString());
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.write(this.mData);
    }

    public void onDecodeSerialData() {
        this.mTempBuffer.copyToBytesAndReadPosSynchronized(this.mData);
        updateHash();
    }

//    public boolean isSmallerThan(BaseBlob baseBlob) {
//        return compareWith(baseBlob) < 0;
//    }

//    public int compareWith(BaseBlob baseBlob) {
//        return DataTypeToolkit.m11511d(this.mData, baseBlob.mData);
//    }
//
//    public int compare(BaseBlob baseBlob) {
//        return DataTypeToolkit.m11508c(this.mData, baseBlob.mData);
//    }

    /* renamed from: at */
    public byte mo9365at(int i) {
        return this.mData[i];
    }

    public int bits() {
        return this.mWidth * 8;
    }

    public void set(BaseBlob baseBlob) {
        if (baseBlob == null) {
            setNull();
            updateHash();
            return;
        }
        int i = this.mWidth;
        if (i == baseBlob.mWidth) {
            if (this.mData == null) {
                this.mData = new byte[i];
            }
            byte[] bArr = baseBlob.mData;
            if (bArr == null) {
                DataTypeToolkit.setBytesZero(this.mData);
            } else {
                System.arraycopy(bArr, 0, this.mData, 0, this.mWidth);
            }
            this.mHashCode = baseBlob.mHashCode;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Curent blob's width ");
        sb.append(this.mWidth);
        sb.append(" not same as destony ");
        sb.append(baseBlob.mWidth);
        throw new IllegalArgumentException(sb.toString());
    }

    public void set(byte[] bArr) {
        set(bArr, 0);
    }

    public void set(byte[] bArr, int i) {
        if (bArr == null) {
            setNull();
            updateHash();
            return;
        }
        byte[] bArr2 = this.mData;
        System.arraycopy(bArr, i, bArr2, 0, bArr2.length);
        updateHash();
    }

    public byte[] begin() {
        return this.mData;
    }
}
