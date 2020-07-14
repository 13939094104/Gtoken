package vdsMain.transaction;

import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.Interpreter;

import java.util.List;
import java.util.Vector;

public class Stacks {

    //f12376a
    public List<byte[]> cScriptByteArrList = new Vector();

    //f12377b
    public List<byte[]> cScriptWitnessByteArrList = new Vector();

    public Stacks() {
    }

    public Stacks(List<byte[]> list) {
        this.cScriptByteArrList.addAll(list);
    }

    public Stacks(SignatureData signatureData, Interpreter interpreter) {
        this.cScriptWitnessByteArrList.addAll(signatureData.cScriptWitness.stack);
        interpreter.evalScript(this.cScriptByteArrList, signatureData.cScript, 2, new BaseSignatureChecker(), SigVersion.BASE, new ScriptErrorResult[0]);
    }

    /* renamed from: a */
    public SignatureData mo43210a() {
        SignatureData signatureData = new SignatureData();
        signatureData.cScript = Sign.addSignatureListToCScript(this.cScriptByteArrList);
        signatureData.cScriptWitness.stack.addAll(this.cScriptWitnessByteArrList);
        return signatureData;
    }
}