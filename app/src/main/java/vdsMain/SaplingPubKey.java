package vdsMain;

import com.vc.libcommon.exception.AddressFormatException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import org.mozilla.classfile.ClassFileWriter;

import java.io.IOException;
import java.util.Locale;

//bsg
public class SaplingPubKey extends SeriableData implements CPubkeyInterface {

    /* renamed from: a */
    private SaplingFullViewingKey f12221a = new SaplingFullViewingKey();

    /* renamed from: b */
    private SaplingPaymentAddress f12222b = new SaplingPaymentAddress();

    /* renamed from: c */
    private int f12223c = 0;

    /* renamed from: c */
    public int checkAndGetTypeLength() {
        return 128;
    }

    /* renamed from: e */
    public boolean checkLength() {
        return false;
    }

    public SaplingPubKey() {
        mo43024d();
    }

    public SaplingPubKey(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof SaplingPubKey) {
            m10511a((SaplingPubKey) seriableData);
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.f12221a.serialToStream(streamWriter);
        this.f12222b.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        this.f12221a.decodeSerialStream((SeriableData) this);
        this.f12222b.decodeSerialStream((SeriableData) this);
        mo43024d();
    }

    public SaplingPubKey(SaplingFullViewingKey brx, SaplingPaymentAddress bsf, boolean z) {
        if (z) {
            brx.mo42984a(brx);
            bsf.mo43013a(bsf);
        } else {
            if (brx == null) {
                this.f12221a = new SaplingFullViewingKey();
            } else {
                this.f12221a = brx;
            }
            if (bsf == null) {
                this.f12222b = new SaplingPaymentAddress();
            } else {
                this.f12222b = bsf;
            }
        }
        mo43024d();
    }

    /* renamed from: a */
    public CPubkeyInterface clone() {
        return new SaplingPubKey(this);
    }

    /* renamed from: b */
    public void mo43022b() {
        this.f12221a.mo42986b();
        this.f12222b.mo43017e();
        mo43024d();
    }

    public void Set(byte[] bArr) throws AddressFormatException {
        mo43021a(bArr, 0);
    }

    /* renamed from: a */
    public void mo43021a(byte[] bArr, int i) throws AddressFormatException {
        if (bArr == null) {
            mo43022b();
        } else if (bArr.length - i >= 128) {
            this.f12221a.mo42985a(bArr, i);
            this.f12222b.mo43014a(bArr, i + 96);
            mo43024d();
        } else {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Data size must grater than %d", new Object[]{Short.valueOf(ClassFileWriter.ACC_TRANSIENT)}));
        }
    }

    /* renamed from: a */
    public void initFromOtherPubKey(CPubkeyInterface knVar) throws AddressFormatException {
        if (knVar instanceof SaplingPubKey) {
            m10511a((SaplingPubKey) knVar);
            return;
        }
        throw new AddressFormatException("Pubkey must be SaplingPubKey or it's children.");
    }

    /* renamed from: a */
    private void m10511a(SaplingPubKey SaplingPubKey) {
        this.f12221a.mo42984a(SaplingPubKey.f12221a);
        this.f12222b.mo43013a(SaplingPubKey.f12222b);
        this.f12223c = SaplingPubKey.f12223c;
    }

    /* renamed from: m */
    public byte[] getByteArr() {
        try {
            return serialToStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: h */
    public CTxDestination getCKeyID() {
        return new SaplingPaymentAddress(this.f12222b);
    }

    /* renamed from: g */
    public PubkeyType mo210g() {
        return PubkeyType.ANONYMOUS;
    }

    /* renamed from: n */
    public byte[] mo9455n() {
        return getCKeyID().data();
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo43024d() {
        this.f12223c = this.f12222b.hashCode();
    }

    /* renamed from: f */
    public SaplingFullViewingKey mo43025f() {
        return this.f12221a;
    }

    /* renamed from: i */
    public SaplingPaymentAddress mo43026i() {
        return this.f12222b;
    }

    public String hexString() {
        return StringToolkit.bytesToString(getByteArr());
    }
}