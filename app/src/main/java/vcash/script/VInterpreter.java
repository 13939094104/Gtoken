package vcash.script;

import bitcoin.UInt256;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import bitcoin.script.Interpreter;
import generic.exceptions.SignatureFailedException;
import generic.serialized.SeriableData;
import vdsMain.transaction.*;
import com.vc.libcommon.util.Integer;
import java.io.IOException;
import java.util.List;

public class VInterpreter extends Interpreter {
    public native boolean EvalScript(List<byte[]> list, byte[][] bArr, byte[] bArr2, int i, BaseSignatureChecker baseSignatureChecker, int i2, Integer integer);


    //mo40488a
    public UInt256 SignatureHash(CScript scriptCode, VTransactionInterface txTo, long nIn, int nHashType, SigVersion sigversion, VPrecomputedTransactionData cache) throws IOException, SignatureFailedException {
        if (nIn >= ((long) txTo.getSelfTxInList().size()) && nIn != ((long) Transaction.uIntMax)) {
            throw new SignatureFailedException("input index is out of range");
        }
        UInt256 hashPrevouts=new UInt256();
        UInt256 hashSequence=new UInt256();
        UInt256 hashOutputs=new UInt256();
        UInt256 hashShieldedSpends=new UInt256();
        UInt256 hashShieldedOutputs=new UInt256();
        if((nHashType&128)==0){
            hashPrevouts= cache != null ? cache.hashPrevouts : GetPrevoutHash((TransactionInteface) txTo);
        }
        if((nHashType&128)==0 && (nHashType&31)!=3 && (nHashType&31)!=2){
            hashSequence = cache != null ? cache.hashSequence : GetSequenceHash((TransactionInteface) txTo);
        }
        if((nHashType&31)!=3 && (nHashType&31)!=2){
            hashOutputs= cache!=null ? cache.hashOutputs : GetOutputsHash(txTo);
        }else if((nHashType&31)==3 && nIn<txTo.getSelfTxOutList().size()){
            CHashWriter cHashWriter = new CHashWriter(4, 0);
            cHashWriter.writeToSteamWriter((SeriableData) txTo.getSelfTxOutList().get((int) nIn));
            hashOutputs=cHashWriter.GetHash();
        }
        if(txTo.isSpendDescriptionListNotEmpty()){
            hashShieldedSpends =  cache != null ? cache.hashShieldedSpends : GetShieldedSpendsHash(txTo);
        }
        if(txTo.isOutputDescriptionListNotEmpty()){
            hashShieldedOutputs =  cache != null ? cache.hashShieldedOutputs : GetShieldedOutputsHash(txTo);
        }
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        cHashWriter.writeToSteamWriter((SeriableData) hashPrevouts);
        cHashWriter.writeToSteamWriter((SeriableData) hashSequence);
        cHashWriter.writeToSteamWriter((SeriableData) hashOutputs);
        cHashWriter.writeToSteamWriter((SeriableData) hashShieldedSpends);
        cHashWriter.writeToSteamWriter((SeriableData) hashShieldedOutputs);
        cHashWriter.writeUInt32T(txTo.getLockTime());
        cHashWriter.writeUInt32T(txTo.getExp());
        cHashWriter.writeUInt64(txTo.getVBalance());
        cHashWriter.writeUInt32T((long) nHashType);
        if (nIn != (Transaction.uIntMax)) {
            TxIn txIn = (TxIn) txTo.getSelfTxInList().get((int) nIn);
            cHashWriter.writeToSteamWriter((SeriableData) txIn.getPrevTxOut());
            cHashWriter.writeToSteamWriter((SeriableData) scriptCode);
            cHashWriter.writeUInt32T(txIn.getSequence());
        }
        return cHashWriter.GetHash();
    }

    /* renamed from: a */
    public UInt256 mo9560a(TransactionInteface diVar, CScript cScript, int i, int i2, long j, SigVersion cxVar, PrecomputedTransactionData cqVar) throws IOException {
        return mo40489a(diVar, cScript, i, i2);
    }

    /* renamed from: a */
    public UInt256 mo40489a(TransactionInteface diVar, CScript cScript, int i, int i2) throws IOException {
        if (i >= diVar.getSelfTxInList().size() && i != VTransaction.uIntMax) {
            throw new IllegalArgumentException("input index is out of range");
        } else if ((i2 & 31) != 3 || i < diVar.getSelfTxOutList().size()) {
            VTransactionSignatureSerializer bpu = new VTransactionSignatureSerializer(diVar.getWallet(), diVar, cScript, i, i2, false);
            CHashWriter kVar = new CHashWriter();
            bpu.mo42757b(kVar);
            kVar.writeUInt32T((long) i2);
            return kVar.GetHash();
        } else {
            throw new IllegalArgumentException("no matching output for SIGHASH_SINGLE");
        }
    }

    /* renamed from: a */
    public boolean evalScript(List<byte[]> stack, CScript script, int flags, BaseSignatureChecker checker, SigVersion sigVersion, ScriptErrorResult... scriptErrorResults) {
        Integer integer = new Integer();
        boolean EvalScript = EvalScript(stack, m524a(stack), script.copyToNewBytes(), flags, checker, sigVersion.mo43200a(), integer);
        if (scriptErrorResults.length > 0) {
            scriptErrorResults[0].scriptError = ScriptError.m10830a(integer.getValue());
        }
        return EvalScript;
    }

    //m4827a
    public static UInt256 GetShieldedSpendsHash(ZTxInterface zTxInterface) {
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        List<SpendDescription> spendDescriptionList = zTxInterface.getSpendDescriptionList();
        if (spendDescriptionList != null && !spendDescriptionList.isEmpty()) {
            for (SpendDescription spendDescription : spendDescriptionList) {
                cHashWriter.writeToSteamWriter((SeriableData) spendDescription.cv);
                cHashWriter.writeToSteamWriter((SeriableData) spendDescription.anchor);
                cHashWriter.writeToSteamWriter((SeriableData) spendDescription.nullifier);
                cHashWriter.writeToSteamWriter((SeriableData) spendDescription.rk);
                cHashWriter.writeToSteamWriter((SeriableData) spendDescription.proof);
            }
        }
        return cHashWriter.GetHash();
    }

    //m4828b
    public static UInt256 GetShieldedOutputsHash(ZTxInterface zTxInterface) {
        CHashWriter cHashWriter = new CHashWriter(4, 0);
        List<OutputDescription> outputDescriptions = zTxInterface.getOutputDescriptionList();
        if (outputDescriptions != null && !outputDescriptions.isEmpty()) {
            for (OutputDescription outputDescription : outputDescriptions) {
                cHashWriter.writeToSteamWriter((SeriableData) outputDescription);
            }
        }
        return cHashWriter.GetHash();
    }
}
