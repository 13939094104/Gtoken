package vdsMain;

import bitcoin.CPubKey;
import com.google.common.base.Ascii;
import com.vc.libcommon.exception.AddressFormatException;
import com.vtoken.vdsecology.vcash.def.ChainCode;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/* renamed from: g */
public class CExtPubKey extends SeriableData {

    /* renamed from: a */
    public byte f12730a;

    /* renamed from: b */
    public byte[] f12731b = new byte[4];

    /* renamed from: c */
    public long f12732c;

    //f12733d
    public ChainCode chainCode = new ChainCode();

    //f12734e
    public CPubKey cPubKey = new CPubKey();

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        byte[] bArr = new byte[74];
        mo43606a(bArr);
        streamWriter.writeVariableBytes(bArr);
    }

    public void onDecodeSerialData() {
        try {
            byte[] readVariableBytes = readVariableBytes();
            if (readVariableBytes.length == 74) {
                mo43607b(readVariableBytes);
            } else {
                throw new IOException("Invalid extended key size\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }

    }

    /* renamed from: a */
    public void mo43606a(byte[] bArr) {
        bArr[0] = this.f12730a;
        System.arraycopy(this.f12731b, 0, bArr, 0, 4);
        long j = this.f12732c;
        bArr[5] = (byte) ((int) ((j >> 24) & 255));
        bArr[6] = (byte) ((int) ((j >> 16) & 255));
        bArr[7] = (byte) ((int) ((j >> 8) & 255));
        bArr[8] = (byte) ((int) ((j >> 0) & 255));
        System.arraycopy(this.chainCode.data(), 0, bArr, 9, 32);
        if (this.cPubKey.checkAndGetTypeLength() == 33) {
            System.arraycopy(this.cPubKey.getByteArr(), 0, bArr, 41, 33);
            return;
        }
        throw new IllegalArgumentException(String.format(Locale.getDefault(), "Pubkey length %d must be 33", new Object[]{Integer.valueOf(this.cPubKey.checkAndGetTypeLength())}));
    }

    /* renamed from: b */
    public void mo43607b(byte[] bArr) throws AddressFormatException {
        this.f12730a = bArr[0];
        System.arraycopy(bArr, 1, this.f12731b, 0, 4);
        this.f12732c = (long) ((bArr[5] << Ascii.CAN) | (bArr[6] << 16) | (bArr[7] << 8) | bArr[8]);
        System.arraycopy(bArr, 9, this.chainCode.data(), 0, 32);
        byte[] bArr2 = new byte[74];
        System.arraycopy(bArr, 41, bArr2, 0, 33);
        this.cPubKey.Set(bArr2);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CExtPubKey)) {
            return false;
        }
        CExtPubKey gVar = (CExtPubKey) obj;
        return gVar.f12730a == this.f12730a && Arrays.equals(gVar.f12731b, this.f12731b) && gVar.f12732c == this.f12732c && gVar.chainCode.equals(this.chainCode) && gVar.cPubKey.equals(this.cPubKey);
    }
}
