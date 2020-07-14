package vdsMain;

import net.bither.bitherj.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
//abp
public class UnsafeByteArrayOutputStream extends ByteArrayOutputStream {
    public UnsafeByteArrayOutputStream() {
        super(32);
    }

    public void write(int i) {
        int i2 = this.count + 1;
        if (i2 > this.buf.length) {
            this.buf = net.bither.bitherj.utils.Utils.m3459d(this.buf, Math.max(this.buf.length << 1, i2));
        }
        this.buf[this.count] = (byte) i;
        this.count = i2;
    }

    public void write(byte[] bArr, int i, int i2) {
        if (i >= 0 && i <= bArr.length && i2 >= 0) {
            int i3 = i + i2;
            if (i3 <= bArr.length && i3 >= 0) {
                if (i2 != 0) {
                    int i4 = this.count + i2;
                    if (i4 > this.buf.length) {
                        this.buf = net.bither.bitherj.utils.Utils.m3459d(this.buf, Math.max(this.buf.length << 1, i4));
                    }
                    System.arraycopy(bArr, i, this.buf, this.count, i2);
                    this.count = i4;
                    return;
                }
                return;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.buf, 0, this.count);
    }

    public void reset() {
        this.count = 0;
    }

    public byte[] toByteArray() {
        return this.count == this.buf.length ? this.buf : Utils.m3459d(this.buf, this.count);
    }

    public int size() {
        return this.count;
    }
}
