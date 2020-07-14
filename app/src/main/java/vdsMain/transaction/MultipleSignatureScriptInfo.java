package vdsMain.transaction;

import org.apache.commons.io.IOUtils;
import vdsMain.StringToolkit;

import java.util.List;
import java.util.Vector;

public class MultipleSignatureScriptInfo {

    //f12283a
    public int M = 0;

    //f12284b
    public int N = 0;

    //f12285c
    public List<byte[]> pubKeysList = new Vector(5);

    //f12286d
    public Script script = null;

    //mo43148a
    public void clear() {
        this.N = 0;
        this.M = 0;
        this.script = null;
        this.pubKeysList.clear();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("======== MultiSignature pub key info======");
        StringBuilder sb = new StringBuilder();
        sb.append("\nM: ");
        sb.append(this.M);
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\nN: ");
        sb2.append(this.N);
        stringBuffer.append(sb2.toString());
        stringBuffer.append("\nPubkeys: ");
        for (byte[] bArr : this.pubKeysList) {
            stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuffer.append(StringToolkit.bytesToString(bArr));
        }
        return stringBuffer.toString();
    }
}
