package vdsMain;

public class KeyInfoT {

    //f12880a
    protected byte[] bytes = new byte[65];

    public KeyInfoT(byte[] bArr) {
        if (bArr == null) {
            return;
        }
        if (bArr.length == 65) {
            System.arraycopy(bArr, 0, this.bytes, 0, 65);
            return;
        }
        throw new IllegalArgumentException("Invalidate pub key len");
    }

    public KeyInfoT() {
    }

    //mo43949a
    public byte[] getBytes() {
        return this.bytes;
    }
}

