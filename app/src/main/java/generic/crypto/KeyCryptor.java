package generic.crypto;

import androidx.annotation.NonNull;
import java.util.Arrays;

import vdsMain.StringToolkit;
import vdsMain.wallet.Wallet;

public final class KeyCryptor {

    //915 f698a
    private byte[] bytes;

    /* renamed from: b */
    private Wallet wallet;

    private native byte[] convertPwd(String str);

    public native synchronized byte[] decrypt(byte[] bArr, String str);

    public native synchronized byte[] encrypt(byte[] bArr, String str);

    public KeyCryptor() {
    }

    public KeyCryptor(@NonNull Wallet wallet) {
        this.wallet = wallet;
    }

    /* renamed from: a */
    public synchronized void setPwd(CharSequence charSequence) {
        this.bytes = convertPwd(charSequence.toString());
        this.wallet.getPersonalDB().getSetting().insertOrUpdateData("pwd", StringToolkit.bytesToString(this.bytes));
    }

    /* renamed from: a */
    public byte[] getBytes() {
        return this.bytes;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0012, code lost:
        return r1;
     */
    //915 mo19027b
    public synchronized boolean checkPwd(CharSequence charSequence) {
        byte[] convertPwd = convertPwd(charSequence.toString());
        boolean z = false;
        if (this.bytes == null) {
            if (convertPwd == null) {
                z = true;
            }
            return z;
        } else if (convertPwd == null) {
            return false;
        } else {
            return Arrays.equals(this.bytes, convertPwd);
        }
    }

    /* renamed from: b */
    public boolean hasSetPwd() {
        byte[] bArr = this.bytes;
        return bArr != null && bArr.length != 0 && !Arrays.equals(convertPwd(""),bArr);
    }

    /* renamed from: c */
    public synchronized void initPwdBytes() {
        String pwd = this.wallet.getPersonalDB().getSetting().getMapValue("pwd", (String) null);
        if (pwd != null) {
            if (!pwd.isEmpty()) {
                this.bytes = StringToolkit.getBytes(pwd);
                return;
            }
        }
        this.bytes = null;
    }

    //915
    public synchronized void mo19024a(String str) {
        if (str != null) {
            if (!str.isEmpty()) {
                this.bytes = StringToolkit.getBytes(str);
                return;
            }
        }
        this.bytes = null;
    }
}
