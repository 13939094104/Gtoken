package vdsMain.transaction;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.DiversifierT;
import vdsMain.SaplingIncomingViewingKey;
import vdsMain.SaplingPaymentAddress;
import zcash.NoteEncryption;
import zcash.RustZCash;

import java.io.IOException;
import java.util.Locale;

//bse
public class SaplingNotePlaintext extends BaseNotePlaintext {

    /* renamed from: c */
    public DiversifierT f12216c = new DiversifierT();

    /* renamed from: d */
    public UInt256 f12217d = new UInt256();

    /* renamed from: a */
    public static SaplingNotePlaintext m10498a(byte[] bArr, UInt256 uInt256, UInt256 uInt2562, UInt256 uInt2563) {
        byte[] a = NoteEncryption.m4834a(bArr, uInt256, uInt2562);
        if (a == null) {
            return null;
        }
        SaplingNotePlaintext bse = new SaplingNotePlaintext();
        try {
            bse.decodeSerialStream(a, 0);
            UInt256 uInt2564 = new UInt256();
            if (!RustZCash.ivk_to_pkd(uInt256.begin(), bse.f12216c.mo42955b(), uInt2564.begin())) {
                return null;
            }
            UInt256 uInt2565 = new UInt256();
            if (!RustZCash.sapling_compute_cm(bse.f12216c.mo42955b(), uInt2564.begin(), bse.mo42951a(), bse.f12217d.begin(), uInt2565.begin())) {
                return null;
            }
            uInt2565.updateHash();
            if (!uInt2565.equals(uInt2563)) {
                return null;
            }
            return bse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public SaplingNote mo43011a(SaplingIncomingViewingKey brz) {
        SaplingPaymentAddress a = brz.mo42994a(this.f12216c);
        if (a == null) {
            return null;
        }
        SaplingNote bsc = new SaplingNote(this.f12216c, a.f12219b, this.f12177a, this.f12217d, new boolean[0]);
        return bsc;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        write((byte) 1);
        this.f12216c.serialToStream(streamWriter);
        writeUInt64(this.f12177a);
        this.f12217d.serialToStream(streamWriter);
        writeBytes(this.f12178b);
    }

    public void onDecodeSerialData() throws IOException {
        if (readByte() == 1) {
            this.f12216c.decodeSerialStream((SeriableData) this);
            this.f12177a = readUInt64().longValue();
            this.f12217d.decodeSerialStream((SeriableData) this);
            int readBytes = readBytes(this.f12178b);
            if (readBytes != 512) {
                throw new IOException(String.format(Locale.getDefault(), "There's not enougth bytes(%d) to read memo (%d)", new Object[]{Integer.valueOf(readBytes),512}));
            }
            return;
        }
        throw new IOException("lead byte of SaplingNotePlaintext is not recognized");
    }
}
