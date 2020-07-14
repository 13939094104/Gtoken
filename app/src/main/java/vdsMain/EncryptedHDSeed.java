package vdsMain;

import androidx.annotation.NonNull;
import bitcoin.account.hd.HDSeed;
import generic.crypto.Base58;
import generic.crypto.KeyCryptor;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

/* renamed from: ah */
public class EncryptedHDSeed extends SeriableData {

    //f5269a
    protected byte[] byteArr;

    public EncryptedHDSeed() {
    }

    //mo40757a
    public byte[] getBytes() {
        return this.byteArr;
    }

    public EncryptedHDSeed(@NonNull HDSeed hDSeed, @NonNull KeyCryptor keyCryptor, String str) {
        this.byteArr = keyCryptor.encrypt(hDSeed.getBytes(), str);
    }

    //mo40753a
    public HDSeed decryptBytesToHDSeed(KeyCryptor keyCryptor, String str) {
        return new HDSeed(keyCryptor.decrypt(this.byteArr, str));
    }

    //mo40754a
    public void updateBytesByNewPwd(HDSeed hDSeed, KeyCryptor keyCryptor, String newPwd) {
        if (hDSeed.getIsValidate()) {
            this.byteArr = keyCryptor.encrypt(hDSeed.getBytes(), newPwd);
            return;
        }
        throw new IllegalArgumentException("Invalidate hd seed bytes.");
    }

    //mo40755a
    public void checkAndUpdateNewPwd(KeyCryptor keyCryptor, String str, String str2) {
        HDSeed hdSeed = decryptBytesToHDSeed(keyCryptor, str);
        updateBytesByNewPwd(hdSeed, keyCryptor, str2);
        hdSeed.reset();
    }

    public String toString() {
        return Base58.encodeToString(this.byteArr);
    }

    /* renamed from: a */
    public void mo40756a(byte[] bArr) {
        this.byteArr = bArr;
    }

    /* renamed from: b */
    public boolean mo40758b() {
        byte[] bArr = this.byteArr;
        return bArr == null || bArr.length == 0 || DataTypeToolkit.isZeroBytes(bArr);
    }

    public void writeSerialData(StreamWriter streamWriter) {
        try {
            streamWriter.writeVariableBytes(this.byteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDecodeSerialData() {
        this.byteArr = readVariableBytes();
    }
}
