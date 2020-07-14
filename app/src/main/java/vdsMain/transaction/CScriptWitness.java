package vdsMain.transaction;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.StringToolkit;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class CScriptWitness extends SeriableData {

    //f12275a
    public List<byte[]> stack = new Vector();

    public CScriptWitness() {
    }

    public CScriptWitness(CScriptWitness clVar) {
        if (clVar != null) {
            this.stack.addAll(clVar.stack);
        }
    }

    //mo43141a
    public boolean isBytesListEmpty() {
        return this.stack.isEmpty();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("CScriptWitness(");
        int i = 0;
        for (byte[] bArr : this.stack) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(StringToolkit.bytesToString(bArr));
            i++;
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableInt((long) this.stack.size());
        for (byte[] writeVariableBytes : this.stack) {
            streamWriter.writeVariableBytes(writeVariableBytes);
        }
    }

    public void onDecodeSerialData() {
        this.stack.clear();
        int count = readVariableInt().getIntValue();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                this.stack.add(readVariableBytes());
            }
        }
    }
}
