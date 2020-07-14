package vdsMain;

import java.util.Locale;

public class BytesArrayBuffer {

    //f12746a
    private byte[] bytes;

    /* renamed from: b */
    //f12747b
    private int writePos;

    /* renamed from: c */
    //f12748c
    private int arrLength;

    public BytesArrayBuffer() {
        this.arrLength = 96;
        this.bytes = new byte[this.arrLength];
    }

    public BytesArrayBuffer(int length) {
        if (length < 96) {
            this.arrLength = 96;
        } else {
            this.arrLength = length;
        }
        this.bytes = new byte[length];
    }

    /* renamed from: a */
    //mo43640a
    public int getWritePos() {
        return this.writePos;
    }

    /* renamed from: b */
    //mo43646b
    public synchronized byte[] copyToNewBytes() {
        if (this.writePos < 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[this.writePos];
        System.arraycopy(this.bytes, 0, bArr, 0, this.writePos);
        return bArr;
    }

    /* renamed from: a */
    //mo43641a
    public void writeOnByte(byte b) {
        if (this.writePos >= this.bytes.length) {
            //expansion();
            m11461f();
        }
        byte[] bArr = this.bytes;
        int i = this.writePos;
        this.writePos = i + 1;
        bArr[i] = b;
    }

    /* renamed from: a */
    //mo43642a
    public void writeAllByteArrayBufferToSelf(BytesArrayBuffer bytesArrayBuffer) {
        writeBytes(bytesArrayBuffer.bytes, 0, bytesArrayBuffer.writePos);
    }

    /* renamed from: f */
    //expansion
    private void m11461f() {
        byte[] bArr = this.bytes;
        byte[] bArr2 = new byte[(bArr.length + this.arrLength)];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        this.bytes = bArr2;
    }

    /* renamed from: a */
    //mo43643a
    public void writeAllBytes(byte[] bArr) {
        writeBytes(bArr, 0, bArr.length);
    }

    /* renamed from: a */
    //mo43644a
    public synchronized void writeBytes(byte[] bArr, int start, int end) {
        int writeLength = end - start;
        if (writeLength > 0) {
            if (this.bytes.length - this.writePos < writeLength) {
                int totalLength = this.writePos + writeLength;
                int needLength = (totalLength / this.arrLength) * this.arrLength;
                while (needLength < totalLength) {
                    needLength += this.arrLength;
                }
                byte[] newByteArr = new byte[needLength];
                if (this.writePos > 0) {
                    System.arraycopy(this.bytes, 0, newByteArr, 0, this.writePos);
                }
                this.bytes = newByteArr;
            }
            System.arraycopy(bArr, start, this.bytes, this.writePos, writeLength);
            this.writePos += writeLength;
        }
    }

    /* renamed from: c */
    //mo43647c
    public void resetArr() {
        checkAndResetArr();
    }

    //mo43648d
    public void checkAndResetArr() {
        int length = this.bytes.length;
        int i = this.arrLength;
        if (length > i) {
            this.bytes = new byte[i];
        }
        this.writePos = 0;
    }

    /* renamed from: b */
    //writeData(bArr,destPos,length)
    public synchronized void mo43645b(byte[] bArr, int i, int i2) {
        if (i + i2 <= this.writePos) {
            System.arraycopy(this.bytes, i, bArr, 0, i2);
        } else {
            throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Read (begin: %d, length: %d) end of buffer %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(this.writePos)}));
        }
    }

    /* renamed from: e */
    //mo43649e
    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BytesArrayBuffer)) {
            return false;
        }
        BytesArrayBuffer gdVar = (BytesArrayBuffer) obj;
        if (this.writePos != gdVar.writePos) {
            return false;
        }
        for (int i = 0; i < this.writePos; i++) {
            if (gdVar.bytes[i] != this.bytes[i]) {
                return false;
            }
        }
        return true;
    }
}
