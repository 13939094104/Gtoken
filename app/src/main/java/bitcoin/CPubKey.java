package bitcoin;

//import bitcoin.script.CScript;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
//import generic.crypto.Base58;
import generic.crypto.Base58;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import net.bither.bitherj.utils.Utils;

public class CPubKey extends SeriableData implements CPubkeyInterface {

    //f400b
    protected byte[] byteArr;

    /* renamed from: c */
    protected int f401c = 0;

    protected static native boolean IsFullyValid(byte[] bArr, int i);

    //m416b
    public static int GetLen(byte chHeader) {
        if (chHeader == 2 || chHeader == 3) {
            return 33;
        }
        return (chHeader == 4 || chHeader == 6 || chHeader == 7) ? 65 : 0;
    }

    public native boolean RecoverCompact(byte[] bArr, byte[] bArr2);

    /* access modifiers changed from: protected */
    public native boolean Verify(byte[] bArr, byte[] bArr2, byte[] bArr3, int i);

    //mo9442c
    //910 mo9442c
    public int getTypeLength() {
        return checkAndGetTypeLength(this.byteArr);
    }

    //mo9434a
    public int getBytesRequireLength() {
        return 65;
    }

    /* renamed from: a */
    public void mo9437a(CPubKey cPubKey) throws AddressFormatException {
    }

    public CPubKey() {
        init();
    }

    public CPubKey(byte[] bArr) throws AddressFormatException {
        Set(bArr);
    }

    public CPubKey(CPubkeyInterface pubkeyInterface) {
        if (pubkeyInterface instanceof CPubKey) {
            initFromOtherPubKey(pubkeyInterface);
            return;
        }
        try {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not convert %s to CPubkey", new Object[]{pubkeyInterface.getClass().getName()}));
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
    }

    /* renamed from: k */
    public CPubKey clone() {
        try {
            return new CPubKey((CPubkeyInterface) this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //mo9435a
    public int getTypeLength(byte b) {
        return GetLen(b);
    }

    //mo9436a
    public int checkAndGetTypeLength(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return 0;
        }
        return getTypeLength(bArr[0]);
    }

    //mo9440b
    public void init() {
        if (this.byteArr == null) {
            this.byteArr = new byte[getBytesRequireLength()];
        }
        this.byteArr[0] = -1;
        this.f401c = 0;
    }

    public void Set(byte[] bArr) throws AddressFormatException {
        int a = checkAndGetTypeLength(bArr);
        if (a > 0) {
            if (a <= 0 || a > bArr.length) {
                init();
            } else {
                byte[] bArr2 = this.byteArr;
                if (bArr2 == null || bArr2.length != a) {
                    this.byteArr = DataTypeToolkit.copyPartBytes(bArr, 0, a);
                } else {
                    System.arraycopy(bArr, 0, bArr2, 0, a);
                }
            }
            mo9453l();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Illagal data length: ");
        sb.append(a);
        sb.append(" --> ");
        sb.append(getClass().getName());
        try {
            throw new AddressFormatException(sb.toString());
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
    }

    //mo9438a
    public void initFromOtherPubKey(CPubkeyInterface pubkeyInterface) {
        if (pubkeyInterface instanceof CPubKey) {
            CPubKey cPubKey = (CPubKey) pubkeyInterface;
            if (cPubKey == null || cPubKey.byteArr == null) {
                init();
                return;
            }
            int c = pubkeyInterface.checkAndGetTypeLength();
            byte[] bArr = this.byteArr;
            if (bArr == null || bArr.length != c) {
                this.byteArr = new byte[c];
            }
            System.arraycopy(cPubKey.byteArr, 0, this.byteArr, 0, c);
            this.f401c = cPubKey.f401c;
            return;
        }
        try {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not convert %s to CPubkey", new Object[]{pubkeyInterface.getClass().toString()}));
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: l */
    public void mo9453l() {
        byte[] bArr = this.byteArr;
        if (bArr == null || bArr.length == 0) {
            this.f401c = 0;
        } else {
            this.f401c = Arrays.hashCode(bArr);
        }
    }

    /* renamed from: c */
    public int checkAndGetTypeLength() {
        return checkAndGetTypeLength(this.byteArr);
    }

    //mo9454m
    public byte[] getByteArr() {
        return this.byteArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CPubKey)) {
            return false;
        }
        CPubKey cPubKey = (CPubKey) obj;
        if (cPubKey.mo210g() != mo210g()) {
            return false;
        }
        return Arrays.equals(this.byteArr, cPubKey.byteArr);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        int c = checkAndGetTypeLength();
        streamWriter.writeVariableInt((long) c);
        byte[] bArr = this.byteArr;
        if (bArr != null) {
            streamWriter.write(bArr, 0, c);
        }
    }

    public void onDecodeSerialData() {
        int b = readVariableInt().getIntValue();
        if (b <= 65) {
            readBytes(this.byteArr);
        } else {
            while (true) {
                int i = b - 1;
                if (b <= 0) {
                    break;
                }
                readByte();
                b = i;
            }
            init();
        }
        mo9453l();
    }

    //mo211h
    public CTxDestination getCKeyID() {
        byte[] bArr = this.byteArr;
        if (bArr == null || bArr.length == 0) {
            return new CKeyID();
        }
        return new CKeyID(CHash160.encodeToUInt160(bArr));
    }

    /* renamed from: n */
    public byte[] mo9455n() {
        byte[] bArr = this.byteArr;
        if (bArr == null || bArr.length == 0) {
            return new byte[20];
        }
        return Utils.sha256hash160(bArr);
    }

    //mo9447f
    public boolean isLengthGreaterZero() {
        return checkAndGetTypeLength() > 0;
    }

    /* renamed from: c */
    public static boolean m417c(byte[] bArr) {
        return m418d(bArr);
    }

    /* renamed from: d */
    public static boolean m418d(byte[] bArr) {
        boolean z = false;
        if (bArr == null) {
            return false;
        }
        if (bArr.length > 0 && GetLen(bArr[0]) == bArr.length) {
            z = true;
        }
        return z;
    }

    //mo9445e
    public boolean checkLength() {
        return checkAndGetTypeLength() == 33;
    }

    /* renamed from: d */
    public boolean mo9444d() {
        if (!isLengthGreaterZero()) {
            return false;
        }
        byte[] bArr = this.byteArr;
        if (bArr.length >= 32) {
            return IsFullyValid(bArr, checkAndGetTypeLength());
        }
        return true;
    }

    //mo9439a
    public boolean verfiySignture(UInt256 uInt256, byte[] bArr) {
        if (!isLengthGreaterZero()) {
            return false;
        }
        return Verify(uInt256.data(), bArr, this.byteArr, checkAndGetTypeLength());
    }

    /* renamed from: b */
    public boolean mo9441b(UInt256 uInt256, byte[] bArr) {
        return RecoverCompact(uInt256.data(), bArr);
    }

    public String toString() {
        byte[] bArr = this.byteArr;
        return bArr != null ? Base58.encodeToString(bArr) : "";
    }

    public String hexString() {
        byte[] bArr = this.byteArr;
        return bArr != null ? StringToolkit.bytesToString(bArr) : "";
    }

    public int hashCode() {
        return this.f401c;
    }

    /* renamed from: g */
    public PubkeyType mo210g() {
        if (mo9444d()) {
            return PubkeyType.PUBKEY;
        }
        return PubkeyType.UNKNOWN;
    }

    //mo9456o
    public void copyAndSetPartBytes() {
        if (isLengthGreaterZero()) {
            int c = checkAndGetTypeLength();
            byte[] bArr = this.byteArr;
            if (c < bArr.length) {
                this.byteArr = DataTypeToolkit.copyPart(bArr, 0, c);
            }
        }
    }

    /* renamed from: i */
    public CScript mo9450i() {
        byte[] bArr = this.byteArr;
        if (bArr.length == 20 || bArr.length == 32 || bArr.length == 33 || bArr.length == 65) {
            return CScript.m484a(getCKeyID());
        }
        return null;
    }

   /* renamed from: j */
    public CScript mo9451j() {
        byte[] bArr = this.byteArr;
        if (bArr.length == 20 || bArr.length == 32 || bArr.length == 33) {
            return CScript.m484a(getCKeyID());
        }
        if (bArr.length == 65) {
            return CScript.m483a(this);
        }
        return null;
    }
}
