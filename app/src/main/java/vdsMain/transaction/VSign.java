package vdsMain.transaction;


import bitcoin.CPubKey;
import bitcoin.UInt160;
import bitcoin.script.CScript;
import bitcoin.script.Interpreter;
import com.vc.libcommon.exception.AddressFormatException;
import generic.serialized.SeriableData;
import net.bither.bitherj.exception.ScriptException;
import vcash.script.VInterpreter;
import vcash.script.VStandard;
import vdsMain.BytesArrayBuffer;
import vdsMain.CKeyID;
import vdsMain.CTxDestination;
import zcash.crypto.CRIPEMD160;

import java.util.ArrayList;
import java.util.List;

//bps
public class VSign extends Sign {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public Interpreter getNewInterpreter() {
        return new VInterpreter();
    }

    //mo42753a
    public boolean ProduceSignature(CharSequence pwd, VBaseSignatureCreator signatureCreator, CScript fromPubKey, SignatureData signatureData, CScript... cScriptArr) throws AddressFormatException, ScriptException {
        CScript script = new CScript((SeriableData) fromPubKey);
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        TxnOutTypeRef whichType = new TxnOutTypeRef();
        boolean solved = SignStep(pwd, signatureCreator, script, result, whichType, SigVersion.BASE, cScriptArr);
        boolean P2SH=false;
        CScript subscript=null;
        signatureData.cScriptWitness.stack.clear();
        if(solved && whichType.txnOutType==TxnOutType.TX_SCRIPTHASH)
        {
            subscript = new CScript((byte[]) result.get(0));
            script.initWithSeriableData((SeriableData) subscript);
            solved=SignStep(pwd,signatureCreator,script,result,whichType,SigVersion.BASE,cScriptArr)&&whichType.txnOutType != TxnOutType.TX_SCRIPTHASH;
            P2SH = true;
        }
        if (solved && whichType.txnOutType == TxnOutType.TX_WITNESS_V0_KEYHASH) {
            CScript witnessscript = new CScript();
            witnessscript.checkAndAddOpCode(118);
            witnessscript.checkAndAddOpCode(169);
            witnessscript.writeDataBytes((byte[]) result.get(0));
            witnessscript.checkAndAddOpCode(136);
            witnessscript.checkAndAddOpCode(172);
            TxnOutTypeRef subType = new TxnOutTypeRef();
            solved=SignStep(pwd, signatureCreator, witnessscript, result, subType, SigVersion.WITNESS_V0, cScriptArr);
            signatureData.cScriptWitness.stack=result;
            signatureData.f12373d = true;
            result.clear();
        } else if (solved && whichType.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH) {
            CScript witnessscript = new CScript((byte[]) result.get(0));
            TxnOutTypeRef subType = new TxnOutTypeRef();
            solved=SignStep(pwd, signatureCreator, witnessscript, result, subType, SigVersion.WITNESS_V0, cScriptArr)
                    && subType.txnOutType != TxnOutType.TX_SCRIPTHASH && subType.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH
                    && subType.txnOutType != TxnOutType.TX_WITNESS_V0_KEYHASH;
            result.add(witnessscript.copyToNewBytes());
            signatureData.cScriptWitness.stack=result;
            signatureData.f12373d = true;
            result.clear();
        }
        if (P2SH) {
            result.add(subscript.copyToNewBytes());
        }
        signatureData.cScript = Sign.addSignatureListToCScript(result);
        return solved&&interpreter.VerifyScript(signatureData.cScript,fromPubKey,signatureData.cScriptWitness,131039,signatureCreator.getSignatureChecker());
    }

    //mo42756a
    public boolean Sign1(CharSequence pwd, CKeyID address, VBaseSignatureCreator signatureCreator, CScript scriptCode, List<byte[]> ret, SigVersion sigVersion) {
        BytesArrayBuffer vchSig = new BytesArrayBuffer();
        if (!signatureCreator.CreateSig(pwd, vchSig, address, scriptCode, sigVersion)) {
            return false;
        }
        ret.add(vchSig.copyToNewBytes());
        return true;
    }

    //mo42755a
    public boolean SignN(CharSequence charSequence, List<byte[]> list, VBaseSignatureCreator vBaseSignatureCreator, CScript cScript, List<byte[]> list2, SigVersion sigVersion) throws AddressFormatException {
        List<byte[]> list3 = list;
        byte b = (byte) (((byte[]) list3.get(0))[0] & 0xff);
        int i = 0;
        for (int i2 = 1; i2 < list.size() - 1 && i < b; i2++) {
            if (Sign1(charSequence, (CKeyID) new CPubKey((byte[]) list3.get(i2)).getCKeyID(), vBaseSignatureCreator, cScript, list2, sigVersion)) {
                i++;
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    //mo42754a
    public boolean SignStep(CharSequence pwd, VBaseSignatureCreator creator, CScript scriptPubKey, List<byte[]> ret, TxnOutTypeRef whichTypeRet, SigVersion sigVersion, CScript... cScriptArr) throws AddressFormatException {
        CScript scriptRet = new CScript();
        UInt160 h160 = new UInt160();
        ret.clear();
        SolveResult solveResult = new SolveResult();
        if (!VStandard.setSloveResultTypeAndCheckIsNotTxNonStandard(scriptPubKey, solveResult, new boolean[0])) {
            return false;
        }
        whichTypeRet.txnOutType = solveResult.txnOutType;
        CKeyID keyID;
        switch (solveResult.txnOutType) {
            case TX_NONSTANDARD:
            case TX_NULL_DATA:
                return false;
            case TX_PUBKEY:
                keyID=(CKeyID) new CPubKey((byte[]) solveResult.vSolutions.get(0)).getCKeyID();
                return Sign1(pwd,keyID , creator, scriptPubKey, ret, sigVersion);
            case TX_PUBKEYHASH:
                keyID = new CKeyID((byte[]) solveResult.vSolutions.get(0));
                if (!Sign1(pwd, keyID, creator, scriptPubKey, ret, sigVersion)) {
                    return false;
                }else {
                    ret.add(creator.getCKeyStore().getPubKey((CTxDestination) keyID).getByteArr());
                }
                return true;
            case TX_SCRIPTHASH:
                if (creator.getCKeyStore().getScriptAndWriteToCSript(new CScriptID((byte[]) solveResult.vSolutions.get(0)), scriptRet)) {
                    ret.add(scriptRet.copyToNewBytes());
                    return true;
                }
            case TX_MULTISIG:
                ret.add(new byte[0]);
                return SignN(pwd, solveResult.vSolutions, creator, scriptPubKey, ret, sigVersion);
            case TX_WITNESS_V0_KEYHASH:
                ret.add(solveResult.vSolutions.get(0));
                return true;
            case TX_WITNESS_V0_SCRIPTHASH:
                new CRIPEMD160().mo40492a((byte[]) solveResult.vSolutions.get(0)).mo40494b(h160.begin());
                if (!creator.getCKeyStore().getScriptAndWriteToCSript(new CScriptID(h160), scriptRet)) {
                    return false;
                }
                ret.add(scriptRet.copyToNewBytes());
                return true;
            default:
                return false;
        }
    }
}
