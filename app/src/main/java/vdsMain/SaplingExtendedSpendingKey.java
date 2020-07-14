package vdsMain;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;
import zcash.RustZCash;

import java.io.IOException;
import java.util.Arrays;

//brw
public class SaplingExtendedSpendingKey extends SeriableData implements CPrivateKeyInterface {

    /* renamed from: a */
    public short f12193a;

    /* renamed from: b */
    public long f12194b;

    /* renamed from: c */
    public long f12195c;

    /* renamed from: d */
    public UInt256 f12196d = new UInt256();

    /* renamed from: e */
    public SaplingExpandedSpendingKey f12197e = new SaplingExpandedSpendingKey();

    /* renamed from: f */
    public UInt256 f12198f = new UInt256();

    /* renamed from: g */
    private int f12199g;

    /* renamed from: d */
    public boolean IsCompressed() {
        return false;
    }

    public SaplingExtendedSpendingKey() {
        mo42980c();
    }

    public SaplingExtendedSpendingKey(byte[] bArr) throws IOException {

        decodeSerialStream(bArr, 0);

    }

    public SaplingExtendedSpendingKey(byte[] bArr, int i) {
        try {
            decodeSerialStream(bArr, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.write((byte) this.f12193a);
        streamWriter.writeUInt32T(this.f12194b);
        streamWriter.writeUInt32T(this.f12195c);
        this.f12196d.serialToStream(streamWriter);
        this.f12197e.serialToStream(streamWriter);
        this.f12198f.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12193a = readUInt8();
        this.f12194b = readUInt32();
        this.f12195c = readUInt32();
        this.f12196d.decodeSerialStream((SeriableData) this);
        this.f12197e.decodeSerialStream((SeriableData) this);
        this.f12198f.decodeSerialStream((SeriableData) this);
        mo42980c();
    }

    /* renamed from: a */
    public static SaplingExtendedSpendingKey m10458a(HDSeed brq) {
        long j;
        byte[] c = brq.mo42963c();
        byte[] bArr = new byte[169];
        if (c == null) {
            j = 0;
        } else {
            j = (long) c.length;
        }
        RustZCash.zip32_xsk_master(c, j, bArr);
        try {
            SaplingExtendedSpendingKey brw = new SaplingExtendedSpendingKey();
            brw.decodeSerialStream(bArr, 0);
            return brw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public SaplingExtendedSpendingKey mo42977a(long j) throws IOException {
        byte[] bArr = new byte[169];
        RustZCash.zip32_xsk_derive(serialToStream(), j, bArr);
        SaplingExtendedSpendingKey brw = new SaplingExtendedSpendingKey();
        try {
            brw.decodeSerialStream(bArr, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brw;
    }

    /* renamed from: a */
    public SaplingExtendedFullViewingKey mo42976a() {
        SaplingExtendedFullViewingKey brv = new SaplingExtendedFullViewingKey();
        brv.f12186a = this.f12193a;
        brv.f12187b = this.f12194b;
        brv.f12188c = this.f12195c;
        brv.f12189d = this.f12196d;
        brv.f12190e = this.f12197e.mo42966a();
        brv.f12191f = this.f12198f;
        brv.mo42973b();
        return brv;
    }

    /* renamed from: b */
    public SaplingPaymentAddress mo42979b() {
        return mo42976a().mo42971a();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingExtendedSpendingKey)) {
            return false;
        }
        SaplingExtendedSpendingKey brw = (SaplingExtendedSpendingKey) obj;
        if (!(this.f12193a == brw.f12193a && this.f12194b == brw.f12194b && this.f12195c == brw.f12195c && this.f12196d.equals(brw.f12196d) && this.f12197e.equals(brw.f12197e) && this.f12198f == brw.f12198f)) {
            z = false;
        }
        return z;
    }

    /* renamed from: e */
    public CPubkeyInterface getPubKey() {
        return new SaplingPubKey(this.f12197e.mo42966a(), mo42979b(), false);
    }

    /* renamed from: a */
    public EncryptedPrivateKey mo42978a(@NonNull Wallet izVar, String str) {
        SaplingEncryptedKey brt = new SaplingEncryptedKey();
        izVar.encryptPrivateKeyByPwd(getCopyBytes(), (EncryptedPrivateKey) brt, str);
        return brt;
    }

    /* renamed from: f */
    public byte[] getCopyBytes() {
        try {
            return serialToStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public byte[] signNativeByTransDataDefault(UInt256 uInt256) {
        return new byte[0];
    }

    /* renamed from: g */
    public void clearBytes() {
        this.f12193a = 0;
        this.f12194b = 0;
        this.f12195c = 0;
        this.f12196d.clear();
        this.f12197e.mo42967b();
        this.f12198f.clear();
        mo42980c();
    }

    /* renamed from: h */
    public PrivateKeyType getPivateKeyType() {
        return PrivateKeyType.SAPLING_EXTENED_SPEND_KEY;
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void mo42980c() {
        byte[] f = getCopyBytes();
        if (f == null) {
            this.f12199g = 0;
        } else {
            this.f12199g = Arrays.hashCode(f);
        }
    }

    /* renamed from: a */
    public static SaplingExtendedSpendingKey m10459a(@NonNull HDSeed brq, Wallet wallet) throws AddressFormatException {
        SaplingExtendedSpendingKey a;
        if (!brq.mo42962b()) {
            VChainParam varChainParams = (VChainParam) wallet.getChainParams();
            try {
                SaplingExtendedSpendingKey a2 = m10458a(brq).mo42977a(2147483680L).mo42977a(varChainParams.mo42388u() | 2147483648L);
                VCCryptoKeyStore bin = (VCCryptoKeyStore) wallet.getSelfCWallet().getCKeyStore();
                int i = 0;
                do {
                    a = a2.mo42977a(((long) i) | 2147483648L);
                    i++;
                } while (bin.mo42928a(a.f12197e.mo42966a()));
                return a;
            } catch (IOException e) {
                throw new AddressFormatException((Throwable) e);
            }
        } else {
            throw new AddressFormatException("Failed to Generate ZHDSeed.");
        }
    }

    /* renamed from: i */
    public void mo42982i() {
        this.f12193a = 0;
        this.f12194b = 0;
        this.f12195c = 0;
        this.f12196d.setNull();
        this.f12197e.mo42967b();
        this.f12198f.clear();
    }

    /* renamed from: a */
    public static void m10460a(SaplingExtendedSpendingKey brw) {
        if (brw != null) {
            brw.mo42982i();
        }
    }
}
