package vdsMain.transaction;

import bitcoin.script.CScript;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CMultisigPubkey;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ScriptList extends SeriableData {

    //f13349a
    public List<CScript> cScriptList;

    //mo44672a
    public void addToCScriptList(CScript cScript) {
        if (this.cScriptList == null) {
            this.cScriptList = new Vector();
        }
        this.cScriptList.add(cScript);
    }

    /* renamed from: a */
    public void mo44673a(CMultisigPubkey nVar) {
        if (this.cScriptList == null) {
            this.cScriptList = new Vector();
        }
        if (nVar == null) {
            this.cScriptList.add(null);
        } else {
            this.cScriptList.add(nVar.mo9451j());
        }
    }

    //mo44670a
    public int getScriptListSize() {
        List<CScript> list = this.cScriptList;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.cScriptList.size();
    }

    /* renamed from: a */
    public CScript mo44671a(int i) {
        return (CScript) this.cScriptList.get(i);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        List<CScript> list = this.cScriptList;
        if (list == null || list.isEmpty()) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) this.cScriptList.size());
        for (CScript cScript : this.cScriptList) {
            if (cScript == null || cScript.getArrayBufferWritePos() == 0) {
                streamWriter.writeVariableInt(0);
            } else {
                cScript.serialToStream(streamWriter);
            }
        }
    }

    public void onDecodeSerialData() {
        this.cScriptList = null;
        int b = readVariableInt().getIntValue();
        if (b >= 1) {
            this.cScriptList = new Vector(b);
            for (int i = 0; i < b; i++) {
                CScript cScript = new CScript();
                cScript.decodeSerialStream((SeriableData) this);
                if (cScript.getArrayBufferWritePos() != 0) {
                    this.cScriptList.add(cScript);
                } else {
                    this.cScriptList.add(null);
                }
            }
        }
    }
}
