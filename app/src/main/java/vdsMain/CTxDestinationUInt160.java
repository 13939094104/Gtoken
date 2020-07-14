package vdsMain;

import bitcoin.BaseBlob;
import bitcoin.UInt160;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

public abstract class CTxDestinationUInt160 extends UInt160 implements CTxDestination {
    //mo9422a
    public abstract CTxDestinationType getCTxDestinationType();

    /* renamed from: b */
    public abstract CTxDestination clone();

    public CTxDestinationUInt160() {
    }

    public CTxDestinationUInt160(byte[] bArr) {
        super(bArr);
    }

    public CTxDestinationUInt160(byte[] bArr, int i) {
        super(bArr, i);
    }

    public CTxDestinationUInt160(BaseBlob baseBlob) {
        super(baseBlob);
        if (!(baseBlob instanceof CTxDestinationUInt160)) {
            updateHash();
        }
    }

    /* renamed from: c */
    public String getHash() {
        byte[] bArr = new byte[21];
        bArr[0] = (byte) getCTxDestinationType().getValue();
        System.arraycopy(this.mData, 0, bArr, 1, 20);
        return StringToolkit.bytesToString(bArr);
    }

    public void updateHash() {
        this.mHashCode = CTxDestinationFactory.m908a((CTxDestination) this);
    }

    //mo9423a
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

    public boolean isNull() {
        return DataTypeToolkit.isZeroBytes(this.mData);
    }
}
