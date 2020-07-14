package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class  Message extends WalletSeriablData {

    //f13183c
    protected int mProtocalVersion = 0;

    //f13184d
    protected byte[] magicBytes;

    //f13185e
    protected String typeString = null;

    //f13186f
    protected byte[] checkSum = null;

    //f13187g
    protected byte[] bytes = null;

    /* access modifiers changed from: protected */
    //mo41220a
    //910 mo41251a
    public abstract void onEncodeSerialData(StreamWriter streamWriter) throws IOException;

    public Message(@NonNull Wallet izVar, String str) {
        super(izVar);
        this.typeString = str;
        ChainParams chainParams = mo44013w();
        this.mProtocalVersion = chainParams.protocalVersion;
        this.magicBytes = chainParams.magicBytes;
    }

    /* renamed from: a */
    public void mo44393a(MessageHeader messageHeader, ByteBuffer byteBuffer) {
        mo44396a(messageHeader.getMagicBytes(), messageHeader.getCommand(), messageHeader.getCheckSum(), byteBuffer);
    }

    /* renamed from: a */
    public void mo44396a(byte[] bArr, String str, byte[] bArr2, ByteBuffer byteBuffer) {
        this.magicBytes = bArr;
        this.typeString = str;
        this.checkSum = bArr2;
        this.bytes = null;
        mo44394a(byteBuffer);
    }

    //mo41280c
    public int getSelfProtocalVersion() {
        return this.mProtocalVersion;
    }

    //mo44398j
    public String getTypeString() {
        return this.typeString;
    }

    //mo44399k
    public byte[] getMagicBytes() {
        return this.magicBytes;
    }

    /* renamed from: a */
    public void setTypeString(String str) {
        this.typeString = str;
    }

    //mo44400l
    public int getBytesLength() {
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return 0;
        }
        return bArr.length;
    }

    /* renamed from: a */
    public void mo44394a(ByteBuffer byteBuffer) {
        decodeSerialStream(byteBuffer);
    }

    public final void writeSerialData(StreamWriter streamWriter) throws IOException {
        onEncodeSerialData(streamWriter);
    }

    public byte[] serialToStream() throws IOException {
        boolean z;
        if (this.bytes == null) {
            this.bytes = mo44401m();
            this.checkSum = null;
            z = true;
        } else {
            z = false;
        }
        byte[] bArr = this.bytes;
        if (bArr != null && z) {
            this.checkSum = mo42032a(bArr);
        }
        if (this.checkSum != null) {
            return mo44397a(getMagicBytes(), getTypeString(), this.checkSum, this.bytes);
        }
        return mo44397a(getMagicBytes(), getTypeString(), (byte[]) null, this.bytes);
    }

    /* renamed from: m */
    public final byte[] mo44401m() throws IOException {
        UnsafeByteArrayOutputStream outputStream = new UnsafeByteArrayOutputStream();
        writeSerialData(new DumpedStreamWriter(outputStream));
        return outputStream.toByteArray();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public byte[] mo44397a(byte[] bArr, String str, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4;
        int i;
        if (bArr3 != null) {
            bArr4 = new byte[(bArr3.length + 24)];
        } else {
            bArr4 = new byte[24];
        }
        int a = mo44390a(str, bArr4, mo44391a(bArr4, 0));
        if (bArr3 != null) {
            Utils.uint32ToByteArrayLE((long) bArr3.length, bArr4, a);
        } else {
            Utils.uint32ToByteArrayLE(0, bArr4, a);
        }
        int i2 = a + 4;
        if (bArr2 != null) {
            i = mo44392a(bArr2, bArr4, i2);
        } else {
            i = mo44392a(new byte[]{0, 0, 0, 0}, bArr4, i2);
        }
        if (bArr3 != null) {
            System.arraycopy(bArr3, 0, bArr4, i, bArr3.length);
        }
        return bArr4;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int mo44391a(byte[] bArr, int i) {
        return writeBytes(bArr, i, this.magicBytes);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int mo44390a(String str, byte[] bArr, int i) {
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (bytes != null) {
            if (bytes.length <= 12) {
                System.arraycopy(bytes, 0, bArr, i, bytes.length);
            } else {
                System.arraycopy(bytes, 0, bArr, i, 12);
            }
        }
        return i + 12;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int mo44392a(byte[] bArr, byte[] bArr2, int i) {
        if (bArr == null) {
            return i;
        }
        int length = bArr.length;
        if (length > 4) {
            length = 4;
        }
        System.arraycopy(bArr, 0, bArr2, i, length);
        return i + length;
    }

    /* renamed from: a */
    private byte[] mo42032a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return vdsMain.Utils.m13311a(Sha256Hash.m185a(Sha256Hash.m185a(bArr).mo195a()).mo195a(), 0, 4);
    }

    public void clean() {
        super.clean();
        this.bytes = null;
        this.checkSum = null;
    }
}