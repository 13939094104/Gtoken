package generic.serialized;

import android.content.ContentValues;
import bitcoin.UInt256;
import bitcoin.VariableInteger;
import com.vc.libcommon.util.Size_t;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import vdsMain.*;
import android.util.Pair;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import vdsMain.Utils;

public abstract class SeriableData extends StreamWriter {
    protected OutputStream mOutStream = null;
    protected ByteBuffer mTempBuffer = null;

    private static native double int64ToDouble(long j);

    /* access modifiers changed from: protected */
    public abstract void onDecodeSerialData() throws IOException;

    /* access modifiers changed from: protected */
    public abstract void writeSerialData(StreamWriter streamWriter) throws IOException;

    public SeriableData() {

    }

    public SeriableData(OutputStream outputStream) {
        this.mOutStream = outputStream;
    }

    public SeriableData(ByteBuffer gcVar) {
        this.mTempBuffer = gcVar;
    }

    public void serialToStream(StreamWriter streamWriter) throws IOException {
        writeSerialData(streamWriter);
    }

    public void serialToStream(OutputStream outputStream) throws IOException {
        boolean z = outputStream == this.mOutStream;
        if (!z) {
            this.mOutStream = outputStream;
        }
        writeSerialData(this);
        if (!z) {
            this.mOutStream = null;
        }
    }

    public static void serialToDB(SeriableData seriableData, ContentValues contentValues, String str) throws IOException {
        if (seriableData != null) {
                contentValues.put(str, seriableData.serialToStream());
        }
    }

    public byte[] serialToStream() throws IOException {
        UnsafeByteArrayOutputStream unsafeByteArrayOutputStream = new UnsafeByteArrayOutputStream();
        serialToStream((OutputStream) unsafeByteArrayOutputStream);
        return unsafeByteArrayOutputStream.toByteArray();
    }

    public SeriableData(SeriableData seriableData) {
        if (seriableData != null) {
            this.mTempBuffer = seriableData.mTempBuffer;
            this.mOutStream = seriableData.mOutStream;
        }
    }

    public final void decodeHexString(String str) throws IOException {
        decodeSerialStream(StringToolkit.getBytes(str), 0);
    }

    public final long decodeSerialStream(byte[] bArr, int i) throws IOException {
        this.mTempBuffer = ByteBuffer.getNewByteBuffer(bArr, i);
        long readLength = 0;
        long availReadLength = (long) this.mTempBuffer.availReadLength();
        onDecodeSerialData();
        readLength = availReadLength - ((long) this.mTempBuffer.availReadLength());
        return readLength;
    }

    public final long decodeSerialStream(SeriableData seriableData)  {
        return decodeSerialStream(seriableData.getTempInput());
    }

    public final long decodeSerialStream(ByteBuffer byteBuffer)  {
        long a = (long) byteBuffer.availReadLength();
        this.mTempBuffer = byteBuffer;
        try {
            onDecodeSerialData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mTempBuffer = null;
        return a - ((long) byteBuffer.availReadLength());
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public byte[] readByteArray() {
        return readBytes(readVariableInt().getIntValue());
    }

    public byte[] read(int i, int i2) {
        ByteBuffer gcVar = this.mTempBuffer;
        if (gcVar == null) {
            return null;
        }
        return gcVar.interceptByteArr(i, i2);
    }

    public int currentReadPos() {
        ByteBuffer gcVar = this.mTempBuffer;
        if (gcVar == null) {
            return -1;
        }
        return gcVar.getReadPos();
    }

    public byte[] readBytes(int i) {
        byte[] bArr = new byte[i];
        this.mTempBuffer.checkAndReadToBytesFromZeroIndex(bArr);
        return bArr;
    }

    public void writeBytes(byte[] bArr, int i, int i2) throws IOException {
        if (i2 < 0) {
                this.mOutStream.write(bArr, i, bArr.length - i);
        } else {
                this.mOutStream.write(bArr, i, i2);
        }
    }

    public short readUInt8() {
        return DataTypeToolkit.m11491a(readByte());
    }

    public byte readByte() {
        byte[] bArr = new byte[1];
        this.mTempBuffer.checkAndReadToBytesFromZeroIndex(bArr);
        return bArr[0];
    }

    public int readBytes(byte[] bArr) {
        return this.mTempBuffer.copyToBytesAndReadPosSynchronized(bArr);
    }

    public static void writeUInt8(OutputStream outputStream, int i) throws IOException {
            outputStream.write(new byte[]{(byte) i});
    }

    public int readUInt16() {
        return Utils.getTwoBytesInt(this.mTempBuffer);
    }

    public int readUInt16LE() {
        return Utils.m13320f(this.mTempBuffer);
    }

    public int readInt32() {
        return Utils.m13313b(this.mTempBuffer);
    }

    public long readUInt32() {
        return (long) ((int) vdsMain.Utils.m13317c(this.mTempBuffer));
    }

    public long readInt64() {
        return Utils.m13318d(this.mTempBuffer);
    }

    public BigInteger readUInt64() {
            return new BigInteger(net.bither.bitherj.utils.Utils.reverseByteArr(readBytes(8)));
    }
//
    public double readDouble() {
        return int64ToDouble(readUInt64().longValue());
    }

    public String readVariableString() throws UnsupportedEncodingException {
        try {
            VariableInteger readVariableInt = readVariableInt();
            if (readVariableInt.getValue() == 0) {
                return "";
            }
            return new String(readBytes(readVariableInt.getIntValue()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw e2;
        } catch (IndexOutOfBoundsException e3) {
            throw e3;
        }
    }

    public String readVariableString(String str) throws UnsupportedEncodingException {
        try {
            VariableInteger readVariableInt = readVariableInt();
            if (readVariableInt.getValue() == 0) {
                return "";
            }
            return new String(readBytes(readVariableInt.getIntValue()), str);
        }catch (UnsupportedEncodingException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw e2;
        } catch (IndexOutOfBoundsException e3) {
            throw e3;
        }
    }

    public VariableInteger readVariableInt() {
        return new VariableInteger().readInteger(this.mTempBuffer);
    }

    public void writeObject(StreamWriter streamWriter) throws IOException {

            if (streamWriter instanceof CTxDestination) {
                CTxDestinationFactory.m914a((CTxDestination) streamWriter, (StreamWriter) this);
            } else
                if (streamWriter instanceof SeriableData) {
                ((SeriableData) streamWriter).serialToStream(this.mOutStream);
            } else {
                throw new IOException("Data was not instance of SeriableData");
            }
    }

    public SeriableData readOptionalObject(Class cls)  {
        if (readByte() == 0) {
            return null;
        }
        SeriableData seriableData = null;
        try {
            seriableData = (SeriableData) cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        seriableData.decodeSerialStream(this.mTempBuffer);
        return seriableData;
    }

    public SeriableData readOptionalObject(Class cls, Class cls2, Object... objArr) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        if (readByte() == 0) {
            return null;
        }
        SeriableData seriableData = (SeriableData) cls.getDeclaredConstructor(new Class[]{cls2}).newInstance(objArr);
        seriableData.decodeSerialStream(this.mTempBuffer);
        return seriableData;
    }

    public List readObjectList(List list, Class cls) throws IOException {
        if (list != null) {
            list.clear();
        } else {
            list = new Vector();
        }
        int b = readVariableInt().getIntValue();
        if (b < 1) {
            return list;
        }
        Constructor declaredConstructor = null;
        try {
            declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < b; i++) {
            SeriableData seriableData = null;
            try {
                seriableData = (SeriableData) declaredConstructor.newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            seriableData.decodeSerialStream(this.mTempBuffer);
            list.add(seriableData);
        }
        return list;
    }

    public void readOptionalObjectList(List list, Class cls) throws IOException {
        list.clear();
        int b = readVariableInt().getIntValue();
        try {
            Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            for (int i = 0; i < b; i++) {
                if (readByte() == 0) {
                    list.add(null);
                } else {
                    SeriableData seriableData = (SeriableData) declaredConstructor.newInstance(new Object[0]);
                    seriableData.decodeSerialStream(this.mTempBuffer);
                    list.add(seriableData);
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void readOptionalObjectList(List list, Class cls, Class cls2, Object obj) throws IOException {
        list.clear();
        int b = readVariableInt().getIntValue();
        try {
            Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[]{cls2});
            for (int i = 0; i < b; i++) {
                if (readByte() == 0) {
                    list.add(null);
                } else {
                    SeriableData seriableData = (SeriableData) declaredConstructor.newInstance(new Object[]{obj});
                    seriableData.decodeSerialStream(this.mTempBuffer);
                    list.add(seriableData);
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public Size_t readSizeT() {
        Size_t size_t = new Size_t(0);
        size_t.mo18929a(readBytes(size_t.length()));
        return size_t;
    }

    public byte[] readHash() {
        return readBytes(32);
    }

    public String readHashString() {
        return net.bither.bitherj.utils.Utils.m3445a(readHash());
    }

    public long getLeftReadableLength() {
        ByteBuffer gcVar = this.mTempBuffer;
        if (gcVar != null) {
            return (long) gcVar.availReadLength();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public boolean hasReadableDataLeft() {
        return getLeftReadableLength() > 0;
    }

    public ByteBuffer getTempInput() {
        return this.mTempBuffer;
    }

    public void clean() {
        this.mTempBuffer = null;
    }

    public static void writeUInt256Array(UInt256[] uInt256Arr, OutputStream outputStream) throws IOException {
        for (UInt256 serialToStream : uInt256Arr) {
            serialToStream.serialToStream(outputStream);
        }
    }

    public void readUInt256Array(UInt256[] uInt256Arr) throws IOException{
        for (UInt256 decodeSerialStream : uInt256Arr) {
            decodeSerialStream.decodeSerialStream(this.mTempBuffer);
        }
    }

    public UInt256 readUint256() throws IOException {
        UInt256 uInt256 = new UInt256();
        uInt256.decodeSerialStream(this);
        return uInt256;
    }

    public static void writeBytesArray(byte[][] bArr, OutputStream outputStream) throws IOException {
        for (byte[] write : bArr) {
            outputStream.write(write);
        }
    }

    public void readStringMap(HashMap<String, String> hashMap) {
        hashMap.clear();
        for (int b = readVariableInt().getIntValue(); b > 0; b--) {
            try {
                hashMap.put(readVariableString(), readVariableString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void readStringPairList(List<Pair<String, String>> list) {
        list.clear();
        for (int b = readVariableInt().getIntValue(); b > 0; b--) {
            try {
                list.add(new Pair(readVariableString(), readVariableString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] readVariableBytes() {
        int b = readVariableInt().getIntValue();
        if (b == 0) {
            return null;
        }
        return readBytes(b);
    }

    public void readBytesArray(byte[][] bArr) {
        for (byte[] a : bArr) {
            this.mTempBuffer.copyToBytesAndReadPosSynchronized(a);
        }
    }

    public String hexString() {
        try {
            return StringToolkit.bytesToString(serialToStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
