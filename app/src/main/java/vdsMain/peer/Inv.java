package vdsMain.peer;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;

public class Inv extends SeriableData {

    //f15448a
    private long type = 0;

    //f15449b
    private UInt256 hash;

    public Inv() {
    }

    public Inv(long type, UInt256 uInt256) {
        this.type = type;
        this.hash = uInt256;
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.type);
        streamWriter.writeUInt256(this.hash);
    }

    public void onDecodeSerialData() {
        this.type = readUInt32();
        if (this.hash == null) {
            this.hash = new UInt256();
        }
        this.hash.decodeSerialStream((SeriableData) this);
    }

    //mo45488a
    public long getType() {
        return this.type;
    }

    //mo45489a
    public void setType(long j) {
        this.type = j;
    }

    //mo45490b
    public UInt256 getHash() {
        return this.hash;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [ ");
        StringBuffer stringBuffer = new StringBuffer(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("type = ");
        sb2.append(this.type);
        stringBuffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" , hash = ");
        sb3.append(this.hash);
        stringBuffer.append(sb3.toString());
        stringBuffer.append(" ]");
        return stringBuffer.toString();
    }
}

