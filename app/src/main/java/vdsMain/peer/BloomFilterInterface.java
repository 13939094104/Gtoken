package vdsMain.peer;

public interface BloomFilterInterface {
    //mo42032a
    boolean contains(byte[] bArr);

    //mo42033b
    void insert(byte[] vKey);
}
