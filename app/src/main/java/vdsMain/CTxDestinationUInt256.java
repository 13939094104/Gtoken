package vdsMain;

import bitcoin.UInt256;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

public abstract class CTxDestinationUInt256 extends UInt256 implements CTxDestination {
    /* renamed from: a */
    public abstract CTxDestinationType getCTxDestinationType();

    /* renamed from: b */
    public abstract CTxDestination clone();

    public CTxDestinationUInt256() {
    }

    public CTxDestinationUInt256(byte[] bArr) {
        super(bArr);
    }

    public void updateHash() {
        this.mHashCode = CTxDestinationFactory.m908a((CTxDestination) this);
    }

    /* renamed from: c */
    public String getHash() {
        byte[] bArr = new byte[33];
        bArr[0] = (byte) getCTxDestinationType().getValue();
        System.arraycopy(this.mData, 0, bArr, 1, 20);
        return StringToolkit.bytesToString(bArr);
    }

    /* renamed from: a */
    public void writeTypeAndData(StreamWriter streamWriter) throws IOException {
        streamWriter.write((byte) getCTxDestinationType().getValue());
        streamWriter.write(this.mData);
    }

    /* renamed from: a */
    public void mo9424a(SeriableData seriableData, boolean z) {
        if (z) {
            seriableData.readByte();
        }
        seriableData.readBytes(this.mData);
        updateHash();
    }
}