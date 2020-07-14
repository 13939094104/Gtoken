package bitcoin.account.hd;

import bitcoin.CExtKey;
import generic.exceptions.SeedWordsFormatException;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;

import java.util.Arrays;

public class HDSeed {

    /* renamed from: a */
    private byte[] bytes;

    /* renamed from: b */
    private int hashCode;

    /* renamed from: c */
    private CExtKey cExtKey;

    /* renamed from: bitcoin.account.hd.HDSeed$a */
    public enum InteralType {
        INTERNAL,
        EXTERNAL
    }

    private native boolean isValidate(byte[] bArr);

    private static native byte[] seedFromMnemonicWords(String str, byte[] bArr);

    private native byte[] seedToMasterSeed(byte[] bArr);

    private native String seedToMnemonicWords(byte[] bArr);

    protected HDSeed() {
        this.hashCode = 0;
    }

    public HDSeed(CharSequence charSequence) throws SeedWordsFormatException {
        this();
        checkMnemonicWords(charSequence.toString());
    }

    public HDSeed(byte[] bArr) {
        this.hashCode = 0;
        if (isValidate(bArr)) {
            this.bytes = DataTypeToolkit.bytesCopy(bArr);
            this.cExtKey = new CExtKey();
            byte[] seedToMasterSeed = seedToMasterSeed(bArr);
            this.cExtKey.setKeyBytes(seedToMasterSeed);
            this.hashCode = Arrays.hashCode(bArr);
            DataTypeToolkit.setBytesZero(seedToMasterSeed);
            DataTypeToolkit.setBytesZero(bArr);
            return;
        }
        throw new IllegalArgumentException("Illegal hd seed bytes.");
    }

    //m455b
    //获得新HDSeed
    public static HDSeed getNewHDSeed() {
        HDSeed hDSeed = new HDSeed();
        byte[] byteArr = new byte[64];
        hDSeed.bytes = seedFromMnemonicWords(null, byteArr);
        hDSeed.cExtKey = new CExtKey();
        hDSeed.cExtKey.setKeyBytes(byteArr);
        return hDSeed;
    }

    /* renamed from: a */
    public void checkMnemonicWords(String str) throws SeedWordsFormatException {
        if (!str.isEmpty()) {
            byte[] bArr = new byte[64];
            this.bytes = seedFromMnemonicWords(str, bArr);
            this.cExtKey = new CExtKey();
            this.cExtKey.setKeyBytes(bArr);
            this.hashCode = Arrays.hashCode(this.bytes);
            return;
        }
        throw new SeedWordsFormatException("Invalidate phrase words.");
    }

    /* renamed from: a */
    public static HDSeed getNewHDSeed(String[] strArr) throws SeedWordsFormatException {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < strArr.length) {
            stringBuffer.append(strArr[i]);
            i++;
            if (i < strArr.length) {
                stringBuffer.append(" ");
            }
        }
        return new HDSeed((CharSequence) stringBuffer.toString());
    }

    //mo9485a
    public String getMnemonicWords() {
        return seedToMnemonicWords(this.bytes);
    }


    //mo9487c
    public boolean getIsValidate() {
        return isValidate(this.bytes);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HDSeed)) {
            return false;
        }
        return Arrays.equals(this.bytes, ((HDSeed) obj).bytes);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return StringToolkit.bytesToString(this.bytes);
    }

    //mo9488d
    public void reset() {
        DataTypeToolkit.setBytesZero(this.bytes);
        this.cExtKey.resetBytes();
    }

    /* renamed from: e */
    public byte[] getBytes() {
        return this.bytes;
    }

    //mo9491f
    public CExtKey getExtKey() {
        return this.cExtKey;
    }

    //mo9484a
    public CExtKey getCExtKey(InteralType interalType) {
        CExtKey cExtKey = new CExtKey();
        CExtKey cExtKey2 = new CExtKey();
        this.cExtKey.mo9392a(cExtKey2, 2147483648L);
        cExtKey2.mo9392a(cExtKey, ((long) (interalType == InteralType.INTERNAL ? 1 : 0)) + 2147483648L);
        cExtKey2.resetBytes();
        return cExtKey;
    }
}