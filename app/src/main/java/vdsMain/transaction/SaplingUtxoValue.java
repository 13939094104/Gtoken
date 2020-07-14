package vdsMain.transaction;

import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;

//bpn
public class SaplingUtxoValue extends SeriableData {

    //f12033a
    public CTxDestination cTxDestination;

    //f12034b
    public SaplingOutpoint saplingOutpoint;

    //f12035c
    public long value;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        this.cTxDestination.writeTypeAndData(streamWriter);
        this.saplingOutpoint.writeObject(streamWriter);
        streamWriter.writeUInt64(this.value);
    }

    public void onDecodeSerialData() throws IOException {
        this.cTxDestination = CTxDestinationFactory.m910a((SeriableData) this);
        this.saplingOutpoint = new SaplingOutpoint();
        this.saplingOutpoint.decodeSerialStream((SeriableData) this);
        this.value = readUInt64().longValue();
    }

    public int hashCode() {
        SaplingOutpoint brk = this.saplingOutpoint;
        if (brk == null) {
            return 0;
        }
        return brk.hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaplingUtxoValue)) {
            return false;
        }
        SaplingUtxoValue bpn = (SaplingUtxoValue) obj;
        if (!this.cTxDestination.equals(bpn.cTxDestination) || !this.saplingOutpoint.equals(bpn.saplingOutpoint) || this.value != bpn.value) {
            z = false;
        }
        return z;
    }
}
