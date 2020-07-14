package bitcoin;

import com.google.common.primitives.UnsignedBytes;

import vdsMain.ByteBuffer;

public class VariableInteger {

    //f405a
    private long value = 0;

    private native byte[] encodeNative(long j);

    public static native int getLengthNative(long j);

    private native long parseBytes(byte[] bArr, int i, int i2);

    public VariableInteger() {
    }

    public VariableInteger(int i) {
        this.value = (long) i;
    }

    public VariableInteger(long j) {
        this.value = j;
    }

    //mo9480a
    public VariableInteger readInteger(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[8];
        byteBuffer.checkAndReadToBytes(bArr, 0, 1);
        short b=bArr[0];
        b = (short) (b & 0xff);
        this.value = 0;
        if (b < 253) {
            this.value = (long) b;
        } else if (b == 253) {
            byteBuffer.checkAndReadToBytes(bArr, 0, 2);
            this.value = parseBytes(bArr, 0, 2);
        } else if (b == 254) {
            byteBuffer.checkAndReadToBytes(bArr, 0, 4);
            this.value = parseBytes(bArr, 0, 4);
        } else {
            byteBuffer.checkAndReadToBytes(bArr, 0, 8);
            this.value = parseBytes(bArr, 0, 8);
        }
        return this;
    }

    //mo9481a
    public byte[] encodeNativeValue() {
        return encodeNative(this.value);
    }

    //mo9482b
    //910 mo9482b
    public int getIntValue() {
        return (int) this.value;
    }

    //mo9483c
    public long getValue() {
        return this.value;
    }
}
