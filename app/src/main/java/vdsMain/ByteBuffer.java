package vdsMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Locale;

/* renamed from: gc */
public class ByteBuffer {

    //f12736a
    private byte[] bytes;

    //f12737b
    private int readPos;

    /* renamed from: c */
    private int writePos;

    /* renamed from: d */
    private int f12739d;

    //f12740e
    private int bytesMaxLength;

    //f12741f
    private int maxReadable;

    /* renamed from: g */
    private int marketReadLen;

    /* renamed from: h */
    private int f12743h;

    /* renamed from: i */
    private int f12744i;

    //f12745j
    private java.nio.ByteBuffer nioByteBuffer;

    public ByteBuffer() {
        this(1024);
    }

    public ByteBuffer(int i) {
        this.bytes = null;
        this.readPos = 0;
        this.writePos = 0;
        this.f12739d = 512;
        this.bytesMaxLength = 0;
        this.maxReadable = -1;
        this.marketReadLen = -1;
        this.f12743h = -1;
        this.f12744i = -1;
        this.bytes = new byte[i];
        this.bytesMaxLength = i;
        this.f12739d = i;
        if (this.f12739d < 1) {
            this.f12739d = 512;
        }
    }

    public ByteBuffer(byte[] bArr) {
        this.bytes = null;
        this.readPos = 0;
        this.writePos = 0;
        this.f12739d = 512;
        this.bytesMaxLength = 0;
        this.maxReadable = -1;
        this.marketReadLen = -1;
        this.f12743h = -1;
        this.f12744i = -1;
        this.bytes = bArr;
        this.bytesMaxLength = bArr.length;
        this.f12739d = bArr.length;
        this.writePos = bArr.length;
        if (this.f12739d < 1) {
            this.f12739d = 512;
        }
    }

    public ByteBuffer(byte[] bArr, int i) {
        this.bytes = null;
        this.readPos = 0;
        this.writePos = 0;
        this.f12739d = 512;
        this.bytesMaxLength = 0;
        this.maxReadable = -1;
        this.marketReadLen = -1;
        this.f12743h = -1;
        this.f12744i = -1;
        this.bytes = bArr;
        this.readPos = i;
        this.writePos = bArr.length;
        this.bytesMaxLength = bArr.length;
        this.f12739d = bArr.length;
        if (this.f12739d < 1) {
            this.f12739d = 512;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0026, code lost:
        return;
     */
    //mo43621a
    public synchronized void computeMaxReadableAndMarketReadLen(int readLength) {
        if (readLength < 1) {
            this.maxReadable = -1;
            this.marketReadLen = -1;
            return;
        }
        this.maxReadable = readLength;
        if (this.maxReadable > this.writePos - this.readPos) {
            this.maxReadable = this.writePos - this.readPos;
        }
        if (readLength <= 0) {
            this.marketReadLen = -1;
        } else {
            this.marketReadLen = 0;
        }
    }

    //mo43617a
    public int availReadLength() {
        int marketReadLen = this.marketReadLen;
        if (marketReadLen >= 0) {
            int maxReadable = this.maxReadable;
            if (maxReadable >= 0) {
                return maxReadable - marketReadLen;
            }
        }
        int i3 = this.writePos - this.readPos;
        byte[] bArr = this.bytes;
        if (i3 > bArr.length) {
            i3 = bArr.length;
        }
        int i4 = this.maxReadable;
        if (i4 > -1 && i3 > i4) {
            i3 = i4;
        }
        return i3;
    }

    //mo43630b
    public boolean hasAvailableReadData() {
        return availReadLength() > 0;
    }

    /* renamed from: c */
    public int writeable() {
        int i = this.writePos;
        byte[] bArr = this.bytes;
        if (i >= bArr.length) {
            return 0;
        }
        return bArr.length - i;
    }

    //mo43624a
    public synchronized void checkAndReadToBytes(byte[] bArr, int desStartPos, int needReadLength) {
        try {
            if (availReadLength() >= needReadLength) {
                System.arraycopy(this.bytes, this.readPos, bArr, desStartPos, needReadLength);
                addReadPosAndMarketReadLen(needReadLength);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("There's only ");
                sb.append(availReadLength());
                sb.append(" bytes can read, but you want to read ");
                sb.append(needReadLength);
                sb.append(" bytes.");
                throw new IOException(sb.toString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //mo43620a
    public synchronized int copyToBytesAndReadPosSynchronized(byte[] bArr) {
        return copyToBytesAndAddReadPos(bArr);
    }

    //m11434e
    private int copyToBytesAndAddReadPos(byte[] bArr) {
        int availReadLength = availReadLength();
        if (availReadLength < 1) {
            return -1;
        }
        if (availReadLength >= bArr.length) {
            availReadLength = bArr.length;
        }
        System.arraycopy(this.bytes, this.readPos, bArr, 0, availReadLength);
        addReadPosAndMarketReadLen(availReadLength);
        return availReadLength;
    }

    //m11433d
    private void addReadPosAndMarketReadLen(int i) {
        this.readPos += i;
        int i2 = this.marketReadLen;
        if (i2 >= 0) {
            this.marketReadLen = i2 + i;
        }
    }

    //m11436i
    private java.nio.ByteBuffer getOrNewNioByteBuffer() {
        if (this.nioByteBuffer == null) {
            this.nioByteBuffer = java.nio.ByteBuffer.allocate(2048);
        }
        return this.nioByteBuffer;
    }

    //mo43619a
    public int writeToBytesAndGetLength(SocketChannel socketChannel) throws IOException {
        int read;
        if (socketChannel.isBlocking()) {
            return mo43618a(socketChannel.socket().getInputStream());
        }
        synchronized (this) {
            m11437j();
            if (writeable() < 1) {
                m11435e(this.bytes.length + this.f12739d);
            }
            this.nioByteBuffer = getOrNewNioByteBuffer();
            this.nioByteBuffer.clear();
            read = socketChannel.read(this.nioByteBuffer);
            if (read > 0) {
                m11431c(this.nioByteBuffer.array(), 0, read);
            }
        }
        return read;
    }

    /* renamed from: a */
    public synchronized int mo43618a(InputStream inputStream) throws IOException {
        int read;
        if (this.readPos >= this.bytes.length / 2) {
            m11437j();
        }
        int a = availReadLength();
        if (a < 1) {
            m11435e(this.f12739d);
        }
        read = inputStream.read(this.bytes, this.writePos, a);
        if (read > 0) {
            this.writePos += read;
        }
        return read;
    }

    /* renamed from: j */
    private void m11437j() {
        if (this.writePos >= this.bytes.length) {
            m11438k();
            if (writeable() <= 0) {
                m11435e(this.bytes.length + this.f12739d);
            }
        }
    }

    //mo43628b
    public void checkAndReadToBytesFromZeroIndex(byte[] bArr) {
        checkAndReadToBytes(bArr, 0, bArr.length);
    }

    /* renamed from: c */
    public void mo43633c(byte[] bArr) {
        mo43629b(bArr, 0, bArr.length);
    }

    /* renamed from: b */
    public synchronized void mo43629b(byte[] bArr, int i, int i2) {
        m11431c(bArr, i, i2);
    }

    /* renamed from: c */
    private void m11431c(byte[] bArr, int i, int length) {
        if (writeable() < length) {
            m11435e(this.writePos + this.f12739d);
        }
        System.arraycopy(bArr, i, this.bytes, this.writePos, length);
        this.writePos += length;
    }

    //mo43627b
    public void receiveData(SocketChannel socketChannel) {
        try {
            if (!socketChannel.isBlocking()) {
                synchronized (this) {
                    int write = socketChannel.write(java.nio.ByteBuffer.wrap(this.bytes, this.readPos, availReadLength()));
                    if (write > 0) {
                        this.readPos += write;
                    }
                }
                return;
            }
            Socket socket = socketChannel.socket();
            if (socket != null) {
                getOutputSteam(socket);
                return;
            }
            throw new IOException("Socket channel was not opened...");
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }


    /* renamed from: a */
    public void getOutputSteam(Socket socket) throws IOException {
        mo43622a(socket.getOutputStream());
    }

    /* renamed from: a */
    public synchronized void mo43622a(OutputStream outputStream) throws IOException {
        int i = this.writePos - this.readPos;
        if (i > 0) {
            outputStream.write(this.bytes, this.readPos, i);
            this.readPos += i;
        }
    }

    /* renamed from: e */
    private void m11435e(int i) {
        byte[] bArr = this.bytes;
        if (i < bArr.length) {
            m11438k();
            if (this.writePos > i) {
                this.writePos = i;
            }
            this.bytes = Arrays.copyOfRange(this.bytes, 0, i);
            StringBuilder sb = new StringBuilder();
            sb.append("relloc new data");
            sb.append(this);
            Log.m11473a((Object) this, sb.toString());
        } else if (i > bArr.length) {
            m11438k();
            if (writeable() < i) {
                int i2 = this.f12739d;
                int i3 = (i / i2) * i2;
                if (i3 < i) {
                    i3 += i2;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("relloc data from ");
                sb2.append(this.bytes.length);
                sb2.append(" to ");
                sb2.append(i);
                sb2.append(" , ");
                sb2.append(this.readPos);
                sb2.append(" , ");
                sb2.append(this.writePos);
                Log.m11473a((Object) this, sb2.toString());
                byte[] bArr2 = new byte[i3];
                byte[] bArr3 = this.bytes;
                int i4 = this.readPos;
                System.arraycopy(bArr3, i4, bArr2, 0, this.writePos - i4);
                this.bytes = null;
                this.bytes = bArr2;
            }
        }
    }

    /* renamed from: d */
    public synchronized void mo43634d() {
        m11438k();
    }

    /* renamed from: k */
    private void m11438k() {
        int i = this.readPos;
        if (i != 0) {
            byte[] bArr = this.bytes;
            if (i == bArr.length) {
                this.readPos = 0;
                this.writePos = 0;
                return;
            }
            System.arraycopy(bArr, i, bArr, 0, this.writePos - i);
            this.writePos -= this.readPos;
            this.readPos = 0;
        }
    }

    //mo43635e
    public synchronized void reset() {
        this.readPos = 0;
        this.writePos = 0;
        this.f12743h = -1;
        this.f12744i = -1;
        this.maxReadable = -1;
        this.marketReadLen = -1;
        if (this.bytes.length > this.bytesMaxLength) {
            this.bytes = null;
            this.bytes = new byte[this.bytesMaxLength];
        }
    }

    //mo43636f
    public byte[] getBytes() {
        return this.bytes;
    }

    //mo43637g
    public int getReadPos() {
        return this.readPos;
    }

    //m11432d
    public static ByteBuffer getByteBufferByByteArr(byte[] bArr) {
        ByteBuffer byteBuffer = new ByteBuffer(bArr);
        byteBuffer.writePos = bArr.length;
        return byteBuffer;
    }

    //m11430a
    public static ByteBuffer getNewByteBuffer(byte[] bArr, int i) {
        return new ByteBuffer(bArr, i);
    }

    /* renamed from: h */
    public boolean mo43638h() {
        return !hasAvailableReadData();
    }

    //mo43626b
    public synchronized int addReadPosSynchronized(int i) {
        this.readPos += i;
        if (this.readPos < 0) {
            this.readPos = 0;
        } else if (this.readPos > this.writePos) {
            this.readPos = this.writePos;
        }
        return this.readPos;
    }

    //mo43632c
    public int addReadPos(int i) {
        return addReadPosSynchronized(i);
    }

    //mo43625a
    public byte[] interceptByteArr(int startIndex, int endIndex) {
        if (startIndex >= 0) {
            byte[] bArr = this.bytes;
            if (endIndex <= bArr.length) {
                int length = endIndex - startIndex;
                byte[] bArr2 = new byte[length];
                System.arraycopy(bArr, startIndex, bArr2, 0, length);
                return bArr2;
            }
            throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Invalidate to index %d (max %d)", new Object[]{Integer.valueOf(endIndex), Integer.valueOf(this.bytes.length)}));
        }
        throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Invalidate from index %d", new Object[]{Integer.valueOf(startIndex)}));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        stringBuffer.append(" [");
        StringBuilder sb = new StringBuilder();
        sb.append("capacity = ");
        sb.append(this.bytes.length);
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(", readPos = ");
        sb2.append(this.readPos);
        stringBuffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(", writePos = ");
        sb3.append(this.writePos);
        stringBuffer.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(", readable = ");
        sb4.append(availReadLength());
        stringBuffer.append(sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append(", maxReadable = ");
        sb5.append(this.maxReadable);
        stringBuffer.append(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append(", writeable = ");
        sb6.append(writeable());
        stringBuffer.append(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append(", marketReadLen = ");
        sb7.append(this.marketReadLen);
        stringBuffer.append(sb7.toString());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
