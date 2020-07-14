package vdsMain.transaction;

import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.StringToolkit;

import java.io.IOException;
import java.util.Arrays;

//brl
public class SpendDescription extends SeriableData {

    //f12170a
    public UInt256 cv = new UInt256();

    //f12171b
    public UInt256 anchor = new UInt256();

    //f12172c
    public UInt256 nullifier = new UInt256();

    //f12173d
    public UInt256 rk = new UInt256();

    //f12174e
    public GrothProof proof = new GrothProof();

    /* renamed from: f */
    public byte[] f12175f = new byte[64];

    public SpendDescription() {
    }

    public SpendDescription(SeriableData seriableData) {
        super(seriableData);
        if (seriableData instanceof SpendDescription) {
            SpendDescription brl = (SpendDescription) seriableData;
            this.cv.set((BaseBlob) brl.cv);
            this.anchor.set((BaseBlob) brl.anchor);
            this.nullifier.set((BaseBlob) brl.nullifier);
            this.rk.set((BaseBlob) brl.rk);
            this.proof.mo42937a(brl.proof);
            System.arraycopy(brl.f12175f, 0, this.f12175f, 0, 64);
        }
    }

    /* renamed from: a */
    public UInt256 mo42947a() {
        return this.nullifier;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SpendDescription)) {
            return false;
        }
        SpendDescription brl = (SpendDescription) obj;
        if (this.cv.equals(brl.cv) && this.anchor.equals(brl.anchor) && this.nullifier.equals(brl.nullifier) && this.rk.equals(brl.rk) && this.proof.equals(brl.proof)) {
            return Arrays.equals(this.f12175f, brl.f12175f);
        }
        return false;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.cv.serialToStream(streamWriter);
        this.anchor.serialToStream(streamWriter);
        this.nullifier.serialToStream(streamWriter);
        this.rk.serialToStream(streamWriter);
        this.proof.serialToStream(streamWriter);
        streamWriter.write(this.f12175f);
    }

    public void onDecodeSerialData() {
        this.cv.decodeSerialStream((SeriableData) this);
        this.anchor.decodeSerialStream((SeriableData) this);
        this.nullifier.decodeSerialStream((SeriableData) this);
        this.rk.decodeSerialStream((SeriableData) this);
        this.proof.decodeSerialStream((SeriableData) this);
        readBytes(this.f12175f);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("cv: ");
        stringBuffer.append(this.cv.toString());
        stringBuffer.append("\nanchor: ");
        stringBuffer.append(this.anchor.toString());
        stringBuffer.append("\nnullifier: ");
        stringBuffer.append(this.nullifier.toString());
        stringBuffer.append("\nrk: ");
        stringBuffer.append(this.rk.toString());
        stringBuffer.append("\nproof: ");
        stringBuffer.append(this.proof.toString());
        stringBuffer.append("\nauthSig: ");
        stringBuffer.append(StringToolkit.bytesToString(this.f12175f));
        return stringBuffer.toString();
    }
}