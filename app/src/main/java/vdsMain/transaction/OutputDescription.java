package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.ByteBuffer;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;

import java.io.IOException;
import java.util.Arrays;

//brj
public class OutputDescription extends SeriableData {

    //f12164a
    public UInt256 cv = new UInt256();

    //f12165b
    public UInt256 cm = new UInt256();

    //f12166c
    public UInt256 ephemeralKey = new UInt256();

    //f12167d
    public byte[] encCiphertext = new byte[580];

    //f12168e
    public byte[] outCiphertext = new byte[80];

    //f12169f
    public GrothProof zkproof = new GrothProof();

    public OutputDescription() {
    }

    public OutputDescription(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof OutputDescription) {
            mo42944a((OutputDescription) seriableData);
        }
    }

    /* renamed from: a */
    public void mo42944a(OutputDescription brj) {
        if (brj == null) {
            mo42943a();
            return;
        }
        this.cm.set((BaseBlob) brj.cm);
        this.cv.set((BaseBlob) brj.cv);
        this.ephemeralKey.set((BaseBlob) brj.ephemeralKey);
        byte[] bArr = brj.encCiphertext;
        byte[] bArr2 = this.encCiphertext;
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        byte[] bArr3 = brj.outCiphertext;
        byte[] bArr4 = this.outCiphertext;
        System.arraycopy(bArr3, 0, bArr4, 0, bArr4.length);
        this.zkproof.mo42937a(brj.zkproof);
    }

    /* renamed from: a */
    public void mo42943a() {
        this.cv.clear();
        this.cm.clear();
        this.ephemeralKey.clear();
        DataTypeToolkit.setBytesZero(this.encCiphertext);
        DataTypeToolkit.setBytesZero(this.outCiphertext);
        this.zkproof.mo42940c();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OutputDescription)) {
            return false;
        }
        OutputDescription brj = (OutputDescription) obj;
        return this.cv.equals(brj.cv) && this.cm.equals(brj.cm) && this.ephemeralKey.equals(brj.ephemeralKey) && Arrays.equals(this.encCiphertext, brj.encCiphertext) && Arrays.equals(this.outCiphertext, brj.outCiphertext) && this.zkproof.equals(brj.zkproof);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.cv.serialToStream(streamWriter);
        this.cm.serialToStream(streamWriter);
        this.ephemeralKey.serialToStream(streamWriter);
        streamWriter.write(this.encCiphertext);
        streamWriter.write(this.outCiphertext);
        this.zkproof.serialToStream(streamWriter);
    }

    public void onDecodeSerialData() {
        ByteBuffer tempInput = getTempInput();
        this.cv.decodeSerialStream(tempInput);
        this.cm.decodeSerialStream(tempInput);
        this.ephemeralKey.decodeSerialStream(tempInput);
        this.encCiphertext = readBytes(580);
        this.outCiphertext = readBytes(80);
        this.zkproof.decodeSerialStream(tempInput);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cv: ");
        stringBuffer.append(this.cv.toString());
        stringBuffer.append("\ncm: ");
        stringBuffer.append(this.cm.toString());
        stringBuffer.append("\nephemeralKey: ");
        stringBuffer.append(this.ephemeralKey.toString());
        stringBuffer.append("\nenc: ");
        stringBuffer.append(StringToolkit.bytesToString(this.encCiphertext));
        stringBuffer.append("\nout: ");
        stringBuffer.append(StringToolkit.bytesToString(this.outCiphertext));
        stringBuffer.append("\nproof: ");
        stringBuffer.append(this.zkproof.toString());
        return stringBuffer.toString();
    }
}
