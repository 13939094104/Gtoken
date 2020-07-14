package bitcoin;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.ByteBuffer;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UInt256 extends BaseBlob {
    public static final UInt256 gEmpty = new UInt256();

    private native long getCheapHashNative(byte[] bArr);

    private native long getHashNative(byte[] bArr, byte[] bArr2);

    public UInt256() {
        super(256);
    }

    public UInt256(int i) {
        super(i);
    }

    public UInt256(byte[] bArr) {
        super(bArr);
    }

    public UInt256(byte[] bArr, boolean z) {
        super(bArr, z);
        if (bArr.length != 32) {
            StringBuilder sb = new StringBuilder();
            sb.append("Data length ");
            sb.append(bArr.length);
            sb.append(" must be 32.");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public UInt256(BaseBlob baseBlob) {
        super(baseBlob);
    }

    public UInt256(String str) {
        super(256);
        setHex(str);
    }

    public UInt256 setHash(String str) {
        if (str == null || str.isEmpty()) {
            setNull();
        } else {
            super.setHash(str);
            if (this.mWidth != 32) {
                StringBuilder sb = new StringBuilder();
                sb.append("Hash data length ");
                sb.append(this.mWidth);
                sb.append(" must be 32");
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return this;
    }

    public static UInt256 fromHash(String str) {
        UInt256 uInt256 = new UInt256();
        uInt256.setHash(str);
        return uInt256;
    }

    public static UInt256 fromHex(String str) {
        UInt256 uInt256 = new UInt256();
        uInt256.setHex(str);
        return uInt256;
    }

    public long getCheapHash() {
        return getCheapHashNative(this.mData);
    }

    public long getHash(UInt256 uInt256) {
        return getHashNative(this.mData, uInt256.mData);
    }

    public static UInt256 uint256S(String str) {
        return new UInt256(str);
    }

    public void writeToBuffer(ByteBuffer byteBuffer) {
        byteBuffer.mo43633c(this.mData);
    }

    public void readFromBuffer(ByteBuffer gcVar) {
        gcVar.copyToBytesAndReadPosSynchronized(this.mData);
    }

    public void clear() {
        setNull();
    }

    public static final UInt256 empty() {
        return gEmpty;
    }

    public static UInt256 replace(UInt256 uInt256, UInt256 uInt2562) {
        if (uInt2562 != null) {
            if (uInt256 == null) {
                uInt256 = new UInt256((BaseBlob) uInt2562);
            } else {
                uInt256.set((BaseBlob) uInt2562);
            }
            return uInt256;
        } else if (uInt256 == null) {
            return new UInt256();
        } else {
            uInt256.setNull();
            return uInt256;
        }
    }

    public static void serialUInt256(UInt256 uInt256, OutputStream outputStream) throws IOException {
        if (uInt256 == null) {
            gEmpty.serialToStream(outputStream);
        } else {
            uInt256.serialToStream(outputStream);
        }
    }

    public static void serialUInt256(UInt256 uInt256, StreamWriter streamWriter) throws IOException {
        if (uInt256 == null) {
            gEmpty.serialToStream(streamWriter);
        } else {
            uInt256.serialToStream(streamWriter);
        }
    }

    public static UInt256 unSerialUInt256(UInt256 uInt256, SeriableData seriableData) throws IOException {
        if (uInt256 == null) {
            uInt256 = new UInt256();
        }
        uInt256.decodeSerialStream(seriableData);
        return uInt256;
    }

    public static UInt256 unSerialUInt256(UInt256 uInt256, byte[] bArr, int i) {
        if (uInt256 == null) {
            uInt256 = new UInt256();
        }
        uInt256.setData(bArr, i);
        return uInt256;
    }

    public static String hashString(UInt256 uInt256, String str) {
        return uInt256 == null ? str : uInt256.hashString();
    }

    public static UInt256 readOptional(SeriableData seriableData) throws IOException {
        if (seriableData.readByte() == 0) {
            return null;
        }
        UInt256 uInt256 = new UInt256();
        uInt256.decodeSerialStream(seriableData);
        return uInt256;
    }

    public static void readList(List<UInt256> list, SeriableData seriableData) throws IOException {
        int b = seriableData.readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            UInt256 uInt256 = new UInt256();
            uInt256.decodeSerialStream(seriableData);
            list.add(uInt256);
        }
    }
}
