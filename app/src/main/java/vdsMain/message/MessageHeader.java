package vdsMain.message;

import vdsMain.ByteBuffer;
import vdsMain.StringToolkit;
import vdsMain.Utils;

public class MessageHeader {

    //f12204a
    private byte[] magicBytes = new byte[4];

    //f12205b
    private String mCommand = null;

    //f12206c
    private long mPayloadLength = -1;

    //f12207d
    private byte[] mCheckSum = null;

    //f12208e
    private boolean isInit = false;

    //mo42995a
    public boolean initData(ByteBuffer byteBuffer) {
        boolean isInit = this.isInit;
        if (!isInit) {
            if (!isInit) {
                if (byteBuffer.availReadLength() < 24) {
                    return false;
                }
                this.magicBytes = getMagicBytesFromByteBuffer(byteBuffer);
                this.mCommand = getCommandFromByteBuffer(byteBuffer);
                this.mPayloadLength = Utils.readLongFromByteBuffer(byteBuffer);
                this.mCheckSum = getCheckSumFromByteBuffer(byteBuffer);
            }
            this.isInit = true;
            return true;
        }
        throw new IllegalStateException("MessageHeader was already complete, please use reset function to clear data.");
    }

    //m10482d
    private byte[] getMagicBytesFromByteBuffer(ByteBuffer byteBuffer) {
        if (((long) byteBuffer.availReadLength()) < 4) {
            return null;
        }
        byte[] bArr = new byte[4];
        byteBuffer.copyToBytesAndReadPosSynchronized(bArr);
        return bArr;
    }

    //mo42998b
    public String getCommandFromByteBuffer(ByteBuffer byteBuffer) {
        return Utils.readFromByteBufferToUTF8String(byteBuffer, 12);
    }

    //mo43000c
    public byte[] getCheckSumFromByteBuffer(ByteBuffer gcVar) {
        return Utils.readFromByteBufferToBytes(gcVar, 4);
    }

    //mo42996a
    public byte[] getMagicBytes() {
        return this.magicBytes;
    }

    //mo42997b
    public String getCommand() {
        return this.mCommand;
    }

    //mo42999c
    public long getPayloadLength() {
        return this.mPayloadLength;
    }

    //mo43001d
    public byte[] getCheckSum() {
        return this.mCheckSum;
    }

    //mo43002e
    public boolean getIsInit() {
        return this.isInit;
    }

    //mo43003f
    public void clear() {
        this.magicBytes = new byte[4];
        this.mCommand = null;
        this.mPayloadLength = -1;
        this.mCheckSum = null;
        this.isInit = false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("[");
        StringBuilder sb = new StringBuilder();
        sb.append(" magic = ");
        sb.append(StringToolkit.bytesToString(this.magicBytes));
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" ,mCommand = ");
        sb2.append(this.mCommand);
        stringBuffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" ,mPayloadLength = ");
        sb3.append(this.mPayloadLength);
        stringBuffer.append(sb3.toString());
        if (this.mCheckSum != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(" ,mChecksum = ");
            sb4.append(this.mCheckSum.length);
            stringBuffer.append(sb4.toString());
        } else {
            stringBuffer.append(" ,mChecksum = null");
        }
        stringBuffer.append(" ]");
        return stringBuffer.toString();
    }
}
