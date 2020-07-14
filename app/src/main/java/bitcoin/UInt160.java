package bitcoin;

public class UInt160 extends BaseBlob {
    public UInt160() {
        super(160);
    }

    public UInt160(byte[] bArr) {
        super(bArr);
        if (bArr.length != 20) {
            throw new IllegalArgumentException("Data length must be 160");
        }
    }

    public UInt160(byte[] bArr, int i) {
        super(160);
        if (bArr.length - i >= 20) {
            System.arraycopy(bArr, i, this.mData, 0, 20);
            updateHash();
            return;
        }
        throw new IllegalArgumentException("Data length must be 160");
    }

    public UInt160(BaseBlob baseBlob) {
        super(baseBlob);
        if (!(baseBlob instanceof UInt160)) {
            throw new IllegalArgumentException("Param src must BaseBlob or subclass.");
        }
    }
}
