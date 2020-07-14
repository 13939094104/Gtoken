package bitcoin.script;

import bitcoin.CSHA256;
import bitcoin.UInt256;
import generic.serialized.SeriableData;
import net.bither.bitherj.exception.ScriptException;
import vdsMain.BytesArrayBuffer;
import vdsMain.transaction.*;
import vdsMain.wallet.WalletHelper;
import com.vc.libcommon.util.Integer;
import java.io.IOException;
import java.util.*;

public class Interpreter {
    /* access modifiers changed from: protected */
    public native boolean EvalScript(List<byte[]> list, byte[][] bArr, byte[] bArr2, int i, BaseSignatureChecker baseSignatureChecker, int i2, Integer integer);

    /* renamed from: a */
    public UInt256 mo9560a(TransactionInteface diVar, CScript cScript, int i, int i2, long j, SigVersion cxVar, PrecomputedTransactionData cqVar) throws IOException {
        CScript cScript2;
        int i3 = i;
        int i4 = i2;
        PrecomputedTransactionData cqVar2 = cqVar;
        boolean z = true;
        if (i3 < diVar.getSelfTxInList().size()) {
            List d = diVar.getSelfTxOutList();
            List e = diVar.getSelfTxInList();
            if (cxVar == SigVersion.WITNESS_V0) {
                UInt256 uInt256 = new UInt256();
                UInt256 uInt2562 = new UInt256();
                UInt256 uInt2563 = new UInt256();
                if (cqVar2 == null || !cqVar2.f12298d) {
                    z = false;
                }
                int i5 = i4 & 128;
                if (i5 == 0) {
                    uInt256 = z ? cqVar2.hashPrevouts : GetPrevoutHash(diVar);
                }
                if (i5 == 0) {
                    int i6 = i4 & 31;
                    if (!(i6 == 3 || i6 == 2)) {
                        uInt2562 = z ? cqVar2.hashSequence : GetSequenceHash(diVar);
                    }
                }
                int i7 = i4 & 31;
                if (i7 != 3 && i7 != 2) {
                    uInt2563 = z ? cqVar2.hashOutputs : GetOutputsHash(diVar);
                } else if (i7 == 3 && i3 < d.size()) {
                    CHashWriter kVar = new CHashWriter(4, 0);
                    kVar.writeToSteamWriter((SeriableData) (TxOut) d.get(i3));
                    uInt2563 = kVar.GetHash();
                }
                CHashWriter kVar2 = new CHashWriter(4, 0);
                kVar2.writeUInt32T(diVar.getVersion());
                kVar2.writeToSteamWriter((SeriableData) uInt256);
                kVar2.writeToSteamWriter((SeriableData) uInt2562);
                TxIn dlVar = (TxIn) e.get(i3);
                COutPoint j2 = dlVar.getPrevTxOut();
                if (j2 != null) {
                    kVar2.writeToSteamWriter((SeriableData) j2);
                    cScript2 = cScript;
                } else {
                    kVar2.writeToSteamWriter((SeriableData) diVar.getWallet().getSelfWalletHelper().getNewCOutPoint());
                    cScript2 = cScript;
                }
                kVar2.writeToSteamWriter((SeriableData) cScript2);
                kVar2.writeUInt64(j);
                kVar2.writeUInt32T(dlVar.getSequence());
                kVar2.writeToSteamWriter((SeriableData) uInt2563);
                kVar2.writeUInt32T(diVar.getLockTime());
                kVar2.writeUInt32T((long) i4);
                return kVar2.GetHash();
            }
            CScript cScript3 = cScript;
            UInt256 uint256S = UInt256.uint256S("0000000000000000000000000000000000000000000000000000000000000001");
            if ((i4 & 31) == 3 && i3 >= d.size()) {
                return uint256S;
            }
            CTransactionSignatureSerializer cmVar = new CTransactionSignatureSerializer(diVar.getWallet(), diVar, cScript, i, i2);
            CHashWriter kVar3 = new CHashWriter(4, 0);
            cmVar.mo42757b(kVar3);
            kVar3.writeUInt32T((long) i4);
            return kVar3.GetHash();
        }
        throw new IllegalArgumentException(String.format(Locale.getDefault(), "nIn (%d) smaller than txin count (%d)", new Object[]{java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(diVar.getSelfTxInList().size())}));
    }

    //m520a
    public static UInt256 GetPrevoutHash(TransactionInteface transactionInteface) {
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        List<TxIn> txInList = transactionInteface.getSelfTxInList();
        WalletHelper walletHelper = transactionInteface.getWallet().getSelfWalletHelper();
        for (TxIn txIn : txInList) {
            COutPoint prevTxOut = txIn.getPrevTxOut();
            if (prevTxOut == null) {
                prevTxOut = walletHelper.getNewCOutPoint();
            }
            cHashWriter.writeToSteamWriter((SeriableData) prevTxOut);
        }
        return cHashWriter.GetHash();
    }

    //m525b
    public static UInt256 GetSequenceHash(TransactionInteface transactionInteface) throws IOException {
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        for (TxIn txIn : transactionInteface.getSelfTxInList()) {
            cHashWriter.writeUInt32T(txIn.getSequence());
        }
        return cHashWriter.GetHash();
    }

    //m527c
    public static UInt256 GetOutputsHash(TransactionInteface transactionInteface) {
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        for (TxOut txOut : transactionInteface.getSelfTxOutList()) {
            cHashWriter.writeToSteamWriter((SeriableData) txOut);
        }
        return cHashWriter.GetHash();
    }

    //mo9562a
    public boolean VerifyWitnessProgram(CScriptWitness clVar, long j, byte[] bArr, int i, BaseSignatureChecker baseSignatureChecker, ScriptErrorResult... cuVarArr) {
        List list;
        CScript cScript;
        CScriptWitness clVar2 = clVar;
        byte[] bArr2 = bArr;
        ScriptErrorResult[] cuVarArr2 = cuVarArr;
        if (j == 0) {
            if (bArr2.length == 32) {
                if (clVar2.stack.size() == 0) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_PROGRAM_WITNESS_EMPTY, cuVarArr2);
                }
                cScript = new CScript((byte[]) clVar2.stack.get(clVar2.stack.size() - 1));
                ArrayList arrayList = new ArrayList(clVar2.stack.subList(0, clVar2.stack.size() - 1));
                UInt256 uInt256 = new UInt256();
                new CSHA256().mo9459a(cScript.copyToNewBytes()).mo9463b(uInt256.data());
                if (!Arrays.equals(uInt256.data(), bArr)) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_PROGRAM_MISMATCH, cuVarArr2);
                }
                list = arrayList;
            } else if (bArr2.length != 20) {
                return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_PROGRAM_WRONG_LENGTH, cuVarArr2);
            } else {
                if (clVar2.stack.size() != 2) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_PROGRAM_MISMATCH, cuVarArr2);
                }
                cScript = new CScript();
                cScript.checkAndAddOpCode(118).checkAndAddOpCode(169).mo9532c(bArr).checkAndAddOpCode(136).checkAndAddOpCode(172);
                list = new ArrayList(clVar2.stack);
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (((byte[]) list.get(i2)).length > 520) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_PUSH_SIZE, cuVarArr2);
                }
            }
            if (!evalScript(list, cScript, i, baseSignatureChecker, SigVersion.WITNESS_V0, cuVarArr)) {
                return false;
            }
            if (list.size() != 1) {
                return checkAndSetScriptError(ScriptError.SCRIPT_ERR_CLEANSTACK, cuVarArr2);
            }
            if (!CastToBool((byte[]) list.get(list.size() - 1))) {
                return checkAndSetScriptError(ScriptError.SCRIPT_ERR_EVAL_FALSE, cuVarArr2);
            }
            return true;
        } else if ((i & 4096) != 0) {
            return checkAndSetScriptError(ScriptError.SCRIPT_ERR_DISCOURAGE_UPGRADABLE_WITNESS_PROGRAM, cuVarArr2);
        } else {
            return setScriptErrOK(cuVarArr2[0]);
        }
    }

    //mo9563a
    public boolean evalScript(List<byte[]> stack, CScript script, int flags, BaseSignatureChecker checker, SigVersion sigVersion, ScriptErrorResult... scriptErrorResults) {
        Integer integer = new Integer();
        boolean isPass = EvalScript(stack, m524a(stack), script.copyToNewBytes(), flags, checker, sigVersion.mo43200a(), integer);
        if (scriptErrorResults.length > 0) {
            scriptErrorResults[0].scriptError = ScriptError.m10830a(integer.getValue());
        }
        return isPass;
    }

    /* renamed from: a */
    public static byte[][] m524a(List<byte[]> list) {
        byte[][] bArr = new byte[list.size()][];
        int i = 0;
        for (byte[] bArr2 : list) {
            bArr[i] = bArr2;
            i++;
        }
        return bArr;
    }

    //m523a
    public static boolean setScriptErrOK(ScriptErrorResult... scriptErrorResults) {
        if (scriptErrorResults.length > 0 && scriptErrorResults[0] != null) {
            scriptErrorResults[0].scriptError = ScriptError.SCRIPT_ERR_OK;
        }
        return true;
    }

    //m521a
    public static boolean checkAndSetScriptError(ScriptError scriptError, ScriptErrorResult... scriptErrorResults) {
        if (scriptErrorResults.length > 0 && scriptErrorResults[0] != null) {
            scriptErrorResults[0].scriptError = scriptError;
        }
        return false;
    }

    //m522a
    public static boolean CastToBool(byte[] bArr) {
        int i = 0;
        while (i < bArr.length) {
            if (bArr[i] == 0) {
                i++;
            } else if (i == bArr.length - 1 && bArr[i] == 128) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    //mo9561a
    public boolean VerifyScript(CScript scriptSig, CScript scriptPubKey, CScriptWitness witness, int flags, BaseSignatureChecker checker, ScriptErrorResult... serror) throws ScriptException {
        CScriptWitness tempCSriptWitness = witness == null ? new CScriptWitness() : witness;
        checkAndSetScriptError(ScriptError.SCRIPT_ERR_UNKNOWN_ERROR, serror);
        BytesArrayBuffer bytesArrayBuffer;
        int i2;
        boolean hadWitness=false;
        if ((flags & 32) != 0 && !scriptSig.IsPushOnly()) {
            return checkAndSetScriptError(ScriptError.SCRIPT_ERR_SIG_PUSHONLY, serror);
        }
        Vector<byte[]> stack = new Vector<byte[]>();
        Vector<byte[]> stackCopy = new Vector<byte[]>();
        if (!evalScript(stack, scriptSig, flags, checker, SigVersion.BASE, serror)) {
            return false;
        }
        if((flags&1)!=0){
            stackCopy=stack;
        }
        if (!evalScript(stack, scriptPubKey, flags, checker, SigVersion.BASE, serror)) {
            return false;
        }
        if (stack.isEmpty()) {
            return checkAndSetScriptError(ScriptError.SCRIPT_ERR_EVAL_FALSE, serror);
        }
        if (!CastToBool(stack.get(stack.size() - 1))) {
            return checkAndSetScriptError(ScriptError.SCRIPT_ERR_EVAL_FALSE, serror);
        }
        Integer witnessversion = new Integer();
        BytesArrayBuffer witnessprogram = new BytesArrayBuffer();
        if((flags&2048)!=0){
            if(scriptPubKey.IsWitnessProgram(witnessversion, witnessprogram)){
                hadWitness = true;
                if (scriptSig.getArrayBufferWritePos() != 0){
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_MALLEATED, serror);
                }
                if (!VerifyWitnessProgram(tempCSriptWitness, (long) witnessversion.getValue(), witnessprogram.copyToNewBytes(), flags, checker, serror)) {
                    return false;
                }
                vdsMain.Collection.resize((List) stack, 1);
            }
        }
        if ((flags&1)!= 0 && scriptPubKey.IsPayToScriptHash()) {
                if (!scriptSig.IsPushOnly()) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_SIG_PUSHONLY, serror);
                }
                new vdsMain.Collection().swap(stack,stackCopy);
                if (!stack.isEmpty()) {
                    CScript pubKey2 = new CScript((byte[]) stack.get(stack.size() - 1));
                    popstack(stack);
                    if (!evalScript(stack, pubKey2, flags, checker, SigVersion.BASE, serror)) {
                        return false;
                    }
                    if (stack.isEmpty()) {
                        return checkAndSetScriptError(ScriptError.SCRIPT_ERR_EVAL_FALSE, serror);
                    }
                    if (!CastToBool((byte[]) stack.get(stack.size() - 1))) {
                        return checkAndSetScriptError(ScriptError.SCRIPT_ERR_EVAL_FALSE, serror);
                    }
                    if ((flags&2048)!= 0) {
                        if (pubKey2.IsWitnessProgram(witnessversion, witnessprogram)) {
                            hadWitness = true;
                            CScript temCSript = new CScript();
                            temCSript.writeAllDataBytes(pubKey2.copyToNewBytes());
                            if (!scriptSig.equals(temCSript)) {
                                return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_MALLEATED_P2SH, serror);
                            }
                            if (!VerifyWitnessProgram(tempCSriptWitness, (long) (witnessversion.getValue()), witnessprogram.copyToNewBytes(), flags, checker, serror)) {
                                return false;
                            }
                            vdsMain.Collection.resize((List) stack, 1);
                        }
                    }
                } else {
                    throw new ScriptException("P2SH key stack was null.");
                }
        }
        if ((flags & 256) != 0) {
                if ((flags&1) == 0) {
                    throw new ScriptException(String.format(Locale.getDefault(), "Flags %d must contasin flag SCRIPT_VERIFY_P2SH", new Object[0]));
                } else if ((flags&2048) == 0) {
                    throw new ScriptException(String.format(Locale.getDefault(), "Flags %d must contasin flag SCRIPT_VERIFY_P2SH", new Object[0]));
                } else if (stack.size() != 1) {
                    return checkAndSetScriptError(ScriptError.SCRIPT_ERR_CLEANSTACK, serror);
                }
        }
        if ((flags&2048) != 0) {
            if ((flags&1) == 0) {
                throw new ScriptException(String.format(Locale.getDefault(), "Flags %d must contains flag SCRIPT_VERIFY_P2SH", new Object[]{java.lang.Integer.valueOf(flags)}));
            } else if (!hadWitness && !tempCSriptWitness.isBytesListEmpty()) {
                return checkAndSetScriptError(ScriptError.SCRIPT_ERR_WITNESS_UNEXPECTED, serror);
            }
        }
        return setScriptErrOK(serror);
    }

    //m526b
    public static void popstack(List<byte[]> list) {
        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
            return;
        }
        throw new IllegalArgumentException("popstack(): stack empty");
    }
}
