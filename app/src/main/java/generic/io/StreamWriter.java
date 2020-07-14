package generic.io;

import android.util.Pair;
import bitcoin.UInt256;
import bitcoin.VariableInteger;
import com.vc.libcommon.util.Size_t;
import generic.keyid.CTxDestinationFactory;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.CTxDestination;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
//import net.bither.bitherj.utils.Utils;

/* renamed from: generic.io.StreamWriter */
public abstract class StreamWriter {
    public static final byte[] ARR_FALT = {0};
    public static final byte[] ARR_TRUE = {1};
    public static final byte[] DISCRIMINANT = {1};
    public static final byte[] DISCRIMINANT_NULL = {0};

    private static native long doubleToUint64(double d);

    public abstract void writeBytes(byte[] bArr, int i, int i2) throws IOException;

    public abstract void writeObject(StreamWriter streamWriter) throws IOException;

    public final void write(byte b) throws IOException {
        write(new byte[]{b}, 0, 1);
    }

    public final void write(byte[] bArr) throws IOException {
        writeBytes(bArr, 0, bArr.length);
    }

    public final void write(byte[] bArr, int i, int i2) throws IOException {
        writeBytes(bArr, i, i2);
    }

    public final void writeBytes(byte[] bArr, int i) throws IOException {
        writeBytes(bArr, 0, i);
    }

    public final void writeBytes(byte[] bArr) throws IOException {
        writeBytes(bArr, 0, bArr.length);
    }

    public final void writeUInt64(BigInteger bigInteger) throws IOException {
        writeBytes(Utils.m3451a(bigInteger));
    }

    public final void writeDouble(double d) throws IOException {
        writeUInt64(doubleToUint64(d));
    }

    public final void writeUInt64(long j) throws IOException {
        writeBytes(Utils.m3449a(j));
    }

    public final void writeVariableInt(long j) throws IOException {
        writeBytes(new VariableInteger(j).encodeNativeValue());
    }

    public final void writeCVarInt(long j) throws IOException {
        writeBytes(new com.vc.libcommon.util.VariableInteger((int) j).mo18948a());
    }

    public final void writeVariableBytes(byte[] bArr) throws IOException {
        if (bArr == null || bArr.length == 0) {
            writeVariableInt(0);
            return;
        }
        writeVariableInt((long) bArr.length);
        writeBytes(bArr);
    }

    public final int writeCollectionSize(Collection collection) throws IOException {
        int size = (collection == null || collection.isEmpty()) ? 0 : collection.size();
        writeVariableInt((long) size);
        return size;
    }

    public final void writeUInt8(int i) throws IOException {
        writeBytes(new byte[]{(byte) i}, 0, 1);
    }

    public final void writeUInt16(int i) throws IOException {
        writeBytes(vdsMain.Utils.m13309a(i));
    }

    public final void writeUInt32T(long j) throws IOException {
        byte[] bArr = new byte[4];
        Utils.uint32ToByteArrayLE(j, bArr, 0);
        writeBytes(bArr);
    }

    public static int writeUInt32T(long j, byte[] bArr, int i) {
        Utils.uint32ToByteArrayLE(j, bArr, i);
        return i + 4;
    }

    public static int writeBytes(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        System.arraycopy(bArr2, 0, bArr, i, i3);
        return i + i3;
    }

    public static int writeBytes(byte[] bArr, int i, byte[] bArr2) {
        System.arraycopy(bArr2, 0, bArr, i, bArr2.length);
        return i + bArr2.length;
    }

    public final void writeVariableString(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            writeVariableBytes(null);
        } else {
            try {
                writeVariableBytes(str.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public final void writeSizeT(Size_t size_t) throws IOException {
        writeBytes(size_t.mo18930b());
    }

    public final void writeSizeT(long j) throws IOException {
        writeBytes(Size_t.toCBytesNative(j));
    }

    public final void writeOptionalObject(StreamWriter streamWriter) throws IOException {
        if (streamWriter != null) {
            writeBytes(DISCRIMINANT);
            writeObject(streamWriter);
            return;
        }
        writeBytes(DISCRIMINANT_NULL);
    }

//    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<generic.io.StreamWriter>, for r3v0, types: [java.util.List, java.util.Collection, java.util.List<generic.io.StreamWriter>] */
    public final void writeOptionObjectList(List<StreamWriter> list) throws IOException {
        if (writeCollectionSize(list) >= 1) {
            for (StreamWriter writeOptionalObject : list) {
                writeOptionalObject(writeOptionalObject);
            }
        }
    }

    public final void writeObjectList(List list) throws IOException {
        if (list == null) {
            writeVariableInt(0);
            return;
        }
        writeVariableInt((long) list.size());
        for (Object next : list) {
            if (next instanceof CTxDestination) {
                CTxDestinationFactory.m914a((CTxDestination) next, this);
            } else if (next instanceof SeriableData) {
                ((SeriableData) next).serialToStream(this);
            }
        }
    }

    public final void writeHash(byte[] bArr) throws IOException {
        if (bArr.length >= 32) {
            writeBytes(bArr, 0, 32);
            return;
        }
        writeBytes(bArr);
        if (bArr.length < 32) {
            writeBytes(new byte[(32 - bArr.length)]);
        }
    }

    public final void writeHash(String str) throws IOException {
        writeHash(Utils.getReverseStringBytes(str));
    }

    public final void writeBoolean(boolean z) throws IOException {
        writeBytes(z ? ARR_TRUE : ARR_FALT);
    }

    public final void writeUInt256(UInt256 uInt256) throws IOException {
        if (uInt256 == null) {
            writeObject(UInt256.gEmpty);
        } else {
            writeObject(uInt256);
        }
    }

    public final void writeUInt256Array(UInt256[] uInt256Arr) throws IOException {
        for (UInt256 writeUInt256 : uInt256Arr) {
            writeUInt256(writeUInt256);
        }
    }

    public final void writeBytesArray(byte[][] bArr) throws IOException {
        for (byte[] writeBytes : bArr) {
            writeBytes(writeBytes);
        }
    }

    public void writeStringPairList(List<Pair<String, String>> list) throws IOException {
        if (list == null) {
            writeVariableInt(0);
            return;
        }
        writeVariableInt((long) list.size());
        for (Pair pair : list) {
            writeVariableString((String) pair.first);
            writeVariableString((String) pair.second);
        }
    }

    public void writeStringMap(HashMap<String, String> hashMap) throws IOException {
        if (hashMap == null) {
            writeVariableInt(0);
            return;
        }
        writeVariableInt((long) hashMap.size());
        for (Entry entry : hashMap.entrySet()) {
            writeVariableString((String) entry.getKey());
            writeVariableString((String) entry.getValue());
        }
    }
}
