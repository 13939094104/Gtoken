package bitcoin;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import com.vtoken.vdsecology.vcash.def.ChainCode;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CExtPubKey;
import vdsMain.DataTypeToolkit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class CExtKey extends SeriableData {

    //firstByte

    public byte firstByte;

    /* bytes4 */
    public byte[] bytes4 = new byte[4];

    /* renamed from: c */
    public long f390c;

    //chainCode
    /* renamed from: d */
    public ChainCode chainCode = new ChainCode();

    //f392e
    public CKey cKey = new CKey();

    private native byte[] seedHash(byte[] bArr);

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(new byte[74]);
    }

    public void onDecodeSerialData() {
        byte[] readVariableBytes = readVariableBytes();
        readBytes(readVariableBytes);
        mo9391a(readVariableBytes);
    }

    //checkCodeLength
    /* renamed from: c */
    private void checkCodeLength(byte[] bArr) {
        if (bArr.length != 74) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Code length %d must be %d", new Object[]{Integer.valueOf(bArr.length), 74}));
        }
    }

    //mo9391a
    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public void mo9391a(byte[] bArr) {
        checkCodeLength(bArr);
        this.firstByte = bArr[0];
        System.arraycopy(this.bytes4, 0, bArr, 1, 4);
        this.f390c = (long) ((bArr[5] << Ascii.CAN) | (bArr[6] << 16) | (bArr[7] << 8) | bArr[8]);
        System.arraycopy(this.chainCode.data(), 0, bArr, 9, 32);
        this.cKey.mo9403a(DataTypeToolkit.copyPart(bArr, 42, 74), true);
    }

    /* renamed from: a */
    public boolean mo9392a(CExtKey cExtKey, long j) {
        cExtKey.firstByte = (byte) ((this.firstByte & UnsignedBytes.MAX_VALUE) + 1);
        System.arraycopy(this.cKey.getPubKey().getCKeyID().data(), 0, cExtKey.bytes4, 0, 4);
        cExtKey.f390c = j;
        return this.cKey.mo9400a(cExtKey.cKey, cExtKey.chainCode, j, this.chainCode);
    }

    /* setKeyBytes */
    public void setKeyBytes(byte[] bArr) {
        byte[] seedHash = seedHash(bArr);
        this.cKey.checkAndCopyKeyBytes(seedHash, 0, true);
        System.arraycopy(seedHash, 32, this.chainCode.data(), 0, 32);
        this.firstByte = 0;
        this.f390c = 0;
        DataTypeToolkit.setBytesZero(this.bytes4);
        DataTypeToolkit.setBytesZero(seedHash);
    }

    //mo9390a
    public CExtPubKey getcExtPubKey() {
        CExtPubKey extPubKey = new CExtPubKey();
        extPubKey.f12730a = this.firstByte;
        System.arraycopy(this.bytes4, 0, extPubKey.f12731b, 0, 4);
        extPubKey.f12732c = this.f390c;
        extPubKey.cPubKey = (CPubKey) this.cKey.getPubKey();
        extPubKey.chainCode = this.chainCode;
        return extPubKey;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CExtKey)) {
            return false;
        }
        CExtKey cExtKey = (CExtKey) obj;
        return this.firstByte == cExtKey.firstByte && this.f390c == cExtKey.f390c && Arrays.equals(this.bytes4, cExtKey.bytes4) && this.chainCode.equals(cExtKey.chainCode) && this.cKey.equals(cExtKey.cKey);
    }

    //mo9393b
    public void resetBytes() {
        this.firstByte = 0;
        DataTypeToolkit.setBytesZero(this.bytes4);
        this.f390c = 0;
        this.chainCode.setNull();
        this.cKey.clearBytes();
    }
}
