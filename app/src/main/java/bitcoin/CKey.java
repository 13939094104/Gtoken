package bitcoin;

import java.lang.reflect.Array;
import java.util.Arrays;

;
import vdsMain.CPrivateKeyInterface;
import vdsMain.CPubkeyInterface;
import vdsMain.DataTypeToolkit;
import vdsMain.PrivateKeyType;
import zcash.RandomBytes;

public class CKey implements CPrivateKeyInterface {

    //f393a
    protected boolean fValid = false;

    //f394b
    protected boolean fCompressed = false;

    //f395c
    protected byte[] privateKeyBytes = new byte[32];

    private native boolean DeriveStep2(byte[] bArr, byte[] bArr2);

    private native byte[] SignCompactNative(byte[] bArr, UInt256 uInt256, boolean z);

    private native byte[] SignNative(byte[] bArr, byte[] bArr2, boolean z, int i);

    private static native boolean setNewKey(byte[] bArr, byte[][] bArr2);

    /* access modifiers changed from: protected */
    public native boolean Check(byte[] bArr);

    /* access modifiers changed from: protected */
    public native int GetPubKey(byte[] bArr, byte[] bArr2, boolean z);

    public CKey() {
    }

    public CKey(byte[] bArr) {
        mo9401a(bArr);
    }

    public CKey(byte[] bArr, boolean z) {
        mo9403a(bArr, z);
    }

    public CKey(CKey cKey) {
        this.fValid = cKey.fValid;
        this.fCompressed = cKey.fCompressed;
        byte[] bArr = cKey.privateKeyBytes;
        byte[] bArr2 = this.privateKeyBytes;
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CKey)) {
            return false;
        }
        CKey cKey = (CKey) obj;
        if (!(this.fCompressed == cKey.fCompressed && mo9398a() == cKey.mo9398a() && Arrays.equals(this.privateKeyBytes, cKey.privateKeyBytes))) {
            z = false;
        }
        return z;
    }

    /* renamed from: a */
    public boolean mo9401a(byte[] bArr) {
        if (bArr.length == 32) {
            return mo9403a(bArr, false);
        }
        if (bArr.length != 33) {
            return false;
        }
        byte[] b = DataTypeToolkit.copyPart(bArr, 0, 32);
        boolean z = true;
        if (bArr[32] != 1) {
            z = false;
        }
        return mo9403a(b, z);
    }

    /* renamed from: a */
    public boolean checkAndCopyKeyBytes(byte[] bArr, int i, boolean z) {
        if (bArr.length - i < 32) {
            this.fValid = false;
            return false;
        }
        System.arraycopy(bArr, i, this.privateKeyBytes, 0, 32);
        this.fValid = Check(this.privateKeyBytes);
        this.fCompressed = z;
        if (!this.fValid) {
            DataTypeToolkit.setBytesZero(this.privateKeyBytes);
        }
        return this.fValid;
    }

    /* renamed from: a */
    public boolean mo9403a(byte[] bArr, boolean z) {
        if (bArr.length < 32) {
            this.fValid = false;
            return false;
        }
        this.fValid = Check(bArr);
        this.fCompressed = z;
        if (this.fValid) {
            System.arraycopy(bArr, 0, this.privateKeyBytes, 0, 32);
        }
        return this.fValid;
    }

    /* renamed from: a */
    public int mo9398a() {
        return this.fValid ? 32 : 0;
    }

    //mo9406b
    public byte[] getPrivateKeyBytes() {
        return this.privateKeyBytes;
    }

    //mo9408c
    public boolean getIsVaild() {
        return this.fValid;
    }

    /* renamed from: d */
    public boolean IsCompressed() {
        return this.fCompressed;
    }

    /* renamed from: a */
    public void mo9399a(boolean isCompressed) {
        do {
            RandomBytes.nativeRandomBufDefaultLength(this.privateKeyBytes);
        } while (!Check(this.privateKeyBytes));
        this.fValid = true;
        this.fCompressed = isCompressed;
    }

    //m388b
    public static CKey getCKeyFromBytes(byte[] bArr) {
        byte[][] bArr2 = (byte[][]) Array.newInstance(byte.class, new int[]{1, 1});
        if (!setNewKey(bArr, bArr2)) {
            return null;
        }
        CKey cKey = new CKey(bArr2[0]);
        cKey.fValid = true;
        return cKey;
    }

    //mo9410e
    public CPubkeyInterface getPubKey() {
        if (this.fValid) {
            CPubKey cPubKey = new CPubKey();
            int GetPubKey = GetPubKey(this.privateKeyBytes, cPubKey.getByteArr(), this.fCompressed);
            if (cPubKey.checkAndGetTypeLength() != GetPubKey) {
                StringBuilder sb = new StringBuilder();
                sb.append("Pubkey size ");
                sb.append(cPubKey.checkAndGetTypeLength());
                sb.append(" must be ");
                sb.append(GetPubKey);
                throw new IllegalStateException(sb.toString());
            } else if (cPubKey.isLengthGreaterZero()) {
                cPubKey.copyAndSetPartBytes();
                return cPubKey;
            } else {
                throw new IllegalStateException("Pubkey is invalidate!");
            }
        } else {
            throw new IllegalStateException("The key is invalid!");
        }
    }

    public CPubkeyInterface getUnCompressedPubKey() {
        if (this.fValid) {
            CPubKey cPubKey = new CPubKey();
            byte[] temArr=new byte[32];
            System.arraycopy(this.privateKeyBytes,0,temArr,0,32);
            int GetPubKey = GetPubKey(temArr, cPubKey.getByteArr(), false);
            if (cPubKey.checkAndGetTypeLength() != GetPubKey) {
                StringBuilder sb = new StringBuilder();
                sb.append("Pubkey size ");
                sb.append(cPubKey.checkAndGetTypeLength());
                sb.append(" must be ");
                sb.append(GetPubKey);
                throw new IllegalStateException(sb.toString());
            } else if (cPubKey.isLengthGreaterZero()) {
                cPubKey.copyAndSetPartBytes();
                return cPubKey;
            } else {
                throw new IllegalStateException("Pubkey is invalidate!");
            }
        } else {
            throw new IllegalStateException("The key is invalid!");
        }
    }

    //mo9412f
    public byte[] getCopyBytes() {
        byte[] bArr;
        if (this.fCompressed) {
            bArr = new byte[33];
            bArr[32] = 1;
        } else {
            bArr = new byte[32];
        }
        System.arraycopy(this.privateKeyBytes, 0, bArr, 0, 32);
        return bArr;
    }

    //mo9404a
    public byte[] signNativeByTransDataDefault(UInt256 uInt256) {
        return signNativeByTransactionData(uInt256, false, 0);
    }

    //mo9405a
    public byte[] signNativeByTransactionData(UInt256 uInt256, boolean z, int i) {
        if (!this.fValid) {
            return null;
        }
        return SignNative(this.privateKeyBytes, uInt256.data(), z, i);
    }

    //mo9407b
    public byte[] signCompactNativeHash(UInt256 hash) {
        if (!this.fValid) {
            return null;
        }
        return SignCompactNative(this.privateKeyBytes, hash, this.fCompressed);
    }

    /* renamed from: a */
    public boolean mo9400a(CKey cKey, UInt256 uInt256, long j, UInt256 uInt2562) {
        byte[] bArr;
        if (!getIsVaild()) {
            throw new IllegalStateException("CKey is invilidate!");
        } else if (IsCompressed()) {
            int i = (int) (j & -1);
            if ((i >> 31) == 0) {
                CPubKey cPubKey = (CPubKey) getPubKey();
                if (cPubKey.checkAndGetTypeLength() == 33) {
                    bArr = BIP32.m379a(uInt2562, i, cPubKey.getByteArr());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Pubkey size ");
                    sb.append(cPubKey.checkAndGetTypeLength());
                    sb.append(" must be ");
                    sb.append(33);
                    throw new IllegalStateException(sb.toString());
                }
            } else if (mo9398a() == 32) {
                bArr = BIP32.m378a(uInt2562, i, (byte) 0, this.privateKeyBytes);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("CKey size ");
                sb2.append(mo9398a());
                sb2.append(" must be 32");
                throw new IllegalStateException(sb2.toString());
            }
            System.arraycopy(bArr, 32, uInt256.data(), 0, 32);
            System.arraycopy(this.privateKeyBytes, 0, cKey.getPrivateKeyBytes(), 0, 32);
            boolean DeriveStep2 = DeriveStep2(cKey.getPrivateKeyBytes(), bArr);
            cKey.fCompressed = true;
            cKey.fValid = DeriveStep2;
            return DeriveStep2;
        } else {
            throw new IllegalStateException("CKey is not compressed!");
        }
    }

    //mo9413g
    public void clearBytes() {
        DataTypeToolkit.setBytesZero(this.privateKeyBytes);
    }

    /* renamed from: h */
    public PrivateKeyType getPivateKeyType() {
        return PrivateKeyType.BITCOIN;
    }
}
