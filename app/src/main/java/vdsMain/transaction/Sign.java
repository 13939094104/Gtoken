package vdsMain.transaction;

import bitcoin.CPubKey;
import bitcoin.UInt160;
import bitcoin.script.*;
import com.vc.libcommon.exception.AddressFormatException;
import generic.serialized.SeriableData;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import vdsMain.*;
import vdsMain.Collection;
import vdsMain.wallet.Wallet;
import zcash.crypto.CRIPEMD160;
import vdsMain.Pair;
import java.util.*;

public class Sign {

    //f12368a
    protected Interpreter interpreter = getNewInterpreter();

    /* access modifiers changed from: protected */
    //mo42752a
    public Interpreter getNewInterpreter() {
        return new Interpreter();
    }

    //mo43209b
    public Interpreter getSelfInterpreter() {
        return this.interpreter;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0138, code lost:
        if (new bitcoin.script.Interpreter().mo9561a(r0.f12370a, r21, r0.f12371b, 131039, r20.mo43139a(), new p000.ScriptErrorResult[0]) != false) goto L_0x013c;
     */
    /* renamed from: a */
    public boolean mo43205a(SigningProvider daVar, CharSequence charSequence, BaseSignatureCreator cjVar, CScript cScript, SignatureData czVar, CScript... cScriptArr) throws AddressFormatException {
        CScript cScript2;
        boolean z;
        CScript cScript3;
        boolean z2;
        boolean z3;
        SignatureData czVar2 = czVar;
        ArrayList arrayList = new ArrayList();
        SolveResult dbVar = new SolveResult();
        boolean a = mo43206a(daVar, charSequence, cjVar, cScript, arrayList, SigVersion.BASE, dbVar, cScriptArr);
        czVar2.cScriptWitness.stack.clear();
        boolean z4 = true;
        if (!a || dbVar.txnOutType != TxnOutType.TX_SCRIPTHASH) {
            cScript2 = null;
            z = false;
        } else {
            CScript cScript4 = new CScript((byte[]) arrayList.get(0));
            if (a) {
                if (mo43206a(daVar, charSequence, cjVar, cScript4, arrayList, SigVersion.BASE, dbVar, cScriptArr) && dbVar.txnOutType != TxnOutType.TX_SCRIPTHASH) {
                    a = true;
                    cScript2 = cScript4;
                    z = true;
                }
            }
            a = false;
            cScript2 = cScript4;
            z = true;
        }
        if (a && dbVar.txnOutType == TxnOutType.TX_WITNESS_V0_KEYHASH) {
            CScript cScript5 = new CScript();
            cScript5.checkAndAddOpCode(118).checkAndAddOpCode(169).writeAllDataBytes((byte[]) arrayList.get(0)).checkAndAddOpCode(136).checkAndAddOpCode(172);
            if (a) {
                if (mo43206a(daVar, charSequence, cjVar, cScript5, arrayList, SigVersion.WITNESS_V0, dbVar, cScriptArr)) {
                    z3 = true;
                    czVar2.cScriptWitness.stack.addAll(arrayList);
                    czVar2.f12373d = true;
                    arrayList.clear();
                }
            }
            z3 = false;
            czVar2.cScriptWitness.stack.addAll(arrayList);
            czVar2.f12373d = true;
            arrayList.clear();
        } else if (a && dbVar.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH) {
            CScript cScript6 = new CScript((byte[]) arrayList.get(0));
            if (a) {
                cScript3 = cScript6;
                if (!(!mo43206a(daVar, charSequence, cjVar, cScript6, arrayList, SigVersion.WITNESS_V0, dbVar, cScriptArr) || dbVar.txnOutType == TxnOutType.TX_SCRIPTHASH || dbVar.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH || dbVar.txnOutType == TxnOutType.TX_WITNESS_V0_KEYHASH)) {
                    z2 = true;
                    arrayList.add(cScript3.copyToNewBytes());
                    czVar2.cScriptWitness.stack.addAll(arrayList);
                    czVar2.f12373d = true;
                    arrayList.clear();
                }
            } else {
                cScript3 = cScript6;
            }
            z2 = false;
            arrayList.add(cScript3.copyToNewBytes());
            czVar2.cScriptWitness.stack.addAll(arrayList);
            czVar2.f12373d = true;
            arrayList.clear();
        }
        if (z) {
            arrayList.add(cScript2.copyToNewBytes());
        }
        czVar2.cScript = addSignatureListToCScript(arrayList);
        if (z) {
            return a;
        }
        if (a) {
        }
        z4 = false;
        return z4;
    }

    //m10836a
    public static CScript addSignatureListToCScript(List<byte[]> values) {
        CScript cScript = new CScript();
        for (byte[] bArr : values) {
            if (bArr == null || bArr.length == 0) {
                cScript.checkAndAddOpCode(0);
            } else if (bArr.length == 1) {
                byte b = (byte) (bArr[0] & 0xff);
                if (b < 1 || b > 16) {
                    cScript.writeDataBytes(bArr);
                } else {
                    cScript.mo9527b((int) b);
                }
            } else {
                cScript.writeDataBytes(bArr);
            }
        }
        return cScript;
    }

    /* renamed from: a */
    public Stacks mo43203a(CScript cScript, BaseSignatureChecker baseSignatureChecker, TxnOutType txnOutType, List<byte[]> list, Stacks txInStacks, Stacks preVoutStacks, SigVersion cxVar) {
        switch (txnOutType) {
            case TX_NONSTANDARD:
            case TX_NULL_DATA:
            case TX_WITNESS_UNKNOWN:
                return txInStacks.cScriptByteArrList.size() >= preVoutStacks.cScriptByteArrList.size() ? txInStacks : preVoutStacks;
            case TX_PUBKEY:
            case TX_PUBKEYHASH:
                return (txInStacks.cScriptByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptByteArrList.get(0)).length == 0) ? preVoutStacks : txInStacks;
            case TX_WITNESS_V0_KEYHASH:
                return (txInStacks.cScriptWitnessByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptWitnessByteArrList.get(0)).length == 0) ? preVoutStacks : txInStacks;
            case TX_SCRIPTHASH:
                if (txInStacks.cScriptByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptByteArrList.get(txInStacks.cScriptByteArrList.size() - 1)).length == 0) {
                    return preVoutStacks;
                }
                if (preVoutStacks.cScriptByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptByteArrList.get(txInStacks.cScriptByteArrList.size() - 1)).length == 0) {
                    return txInStacks;
                }
                byte[] bArr = (byte[]) txInStacks.cScriptByteArrList.get(txInStacks.cScriptByteArrList.size() - 1);
                CScript cScript2 = new CScript(bArr);
                SolveResult dbVar = new SolveResult();
                Standard.setResultTxOutType(cScript2, dbVar);
                Collection.m11552a((List) txInStacks.cScriptByteArrList);
                Collection.m11552a((List) preVoutStacks.cScriptByteArrList);
                Stacks a = mo43203a(cScript2, baseSignatureChecker, dbVar.txnOutType, dbVar.vSolutions, txInStacks, preVoutStacks, cxVar);
                a.cScriptByteArrList.add(bArr);
                return a;
            case TX_MULTISIG:
                return new Stacks(mo43204a(cScript, baseSignatureChecker, list, txInStacks.cScriptByteArrList, preVoutStacks.cScriptByteArrList, cxVar));
            case TX_WITNESS_V0_SCRIPTHASH:
                if (txInStacks.cScriptWitnessByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptWitnessByteArrList.get(txInStacks.cScriptWitnessByteArrList.size() - 1)).length == 0) {
                    return preVoutStacks;
                }
                if (preVoutStacks.cScriptWitnessByteArrList.isEmpty() || ((byte[]) txInStacks.cScriptWitnessByteArrList.get(txInStacks.cScriptWitnessByteArrList.size() - 1)).length == 0) {
                    return txInStacks;
                }
                CScript cScript3 = new CScript((byte[]) txInStacks.cScriptWitnessByteArrList.get(txInStacks.cScriptWitnessByteArrList.size() - 1));
                SolveResult dbVar2 = new SolveResult();
                Standard.setResultTxOutType(cScript3, dbVar2);
                Collection.m11552a((List) txInStacks.cScriptWitnessByteArrList);
                Collection.m11554a((List) txInStacks.cScriptByteArrList, (List) txInStacks.cScriptWitnessByteArrList);
                txInStacks.cScriptWitnessByteArrList.clear();
                Collection.m11552a((List) preVoutStacks.cScriptWitnessByteArrList);
                Collection.m11554a((List) preVoutStacks.cScriptByteArrList, (List) preVoutStacks.cScriptWitnessByteArrList);
                preVoutStacks.cScriptWitnessByteArrList.clear();
                Stacks a2 = mo43203a(cScript3, baseSignatureChecker, dbVar2.txnOutType, dbVar2.vSolutions, txInStacks, preVoutStacks, SigVersion.WITNESS_V0);
                Collection.m11554a((List) a2.cScriptWitnessByteArrList, (List) a2.cScriptByteArrList);
                a2.cScriptByteArrList.clear();
                a2.cScriptWitnessByteArrList.add(cScript3.copyToNewBytes());
                return a2;
            default:
                return new Stacks();
        }
    }

    //mo43201a
    public SignatureData CombineSignatures(CScript cScript, BaseSignatureChecker baseSignatureChecker, SignatureData txInSignatureData, SignatureData prevOutSignatureData) {
        SolveResult solveResult = new SolveResult();
        Standard.setResultTxOutType(cScript, solveResult);
        return mo43203a(cScript, baseSignatureChecker, solveResult.txnOutType, solveResult.vSolutions, new Stacks(txInSignatureData, this.interpreter), new Stacks(prevOutSignatureData, this.interpreter), SigVersion.BASE).mo43210a();
    }

    //mo43202a
    public SignatureData DataFromTransaction(CMutableTransaction cMutableTransaction, long txInIndex) {
        SignatureData signatureData = new SignatureData();
        if (((long) cMutableTransaction.txInList.size()) > txInIndex) {
            int i = (int) txInIndex;
            signatureData.cScript = new CScript((SeriableData) ((TxIn) cMutableTransaction.txInList.get(i)).getScriptSig());
            signatureData.cScriptWitness = new CScriptWitness(((TxIn) cMutableTransaction.txInList.get(i)).getCScriptWitness());
            return signatureData;
        }
        throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalidate vin size: %d --> %d", new Object[]{Integer.valueOf(cMutableTransaction.txInList.size()), Long.valueOf(txInIndex)}));
    }

    /* renamed from: a */
    public boolean mo43206a(SigningProvider daVar, CharSequence charSequence, BaseSignatureCreator cjVar, CScript cScript, List<byte[]> list, SigVersion cxVar, SolveResult dbVar, CScript... cScriptArr) throws AddressFormatException {
        SigningProvider daVar2 = daVar;
        List<byte[]> list2 = list;
        SolveResult dbVar2 = dbVar;
        CScript[] cScriptArr2 = cScriptArr;
        CScriptID ckVar = new CScriptID();
        list.clear();
        if (!Standard.setResultTxOutType(cScript, dbVar2)) {
            return false;
        }
        CScript cScript2 = null;
        switch (dbVar2.txnOutType) {
            case TX_NONSTANDARD:
            case TX_NULL_DATA:
            case TX_WITNESS_UNKNOWN:
                return false;
            case TX_PUBKEY:
                return mo43208a(daVar, charSequence, (CKeyID) new CPubKey((byte[]) dbVar2.vSolutions.get(0)).getCKeyID(), cjVar, cScript, list, cxVar);
            case TX_PUBKEYHASH:
                CKeyID lVar = new CKeyID(new UInt160((byte[]) dbVar2.vSolutions.get(0)));
                if (!mo43208a(daVar, charSequence, lVar, cjVar, cScript, list, cxVar)) {
                    return false;
                }
                list2.add(daVar.mo42762a(lVar).getByteArr());
                return true;
            case TX_WITNESS_V0_KEYHASH:
                list2.add(dbVar2.vSolutions.get(0));
                return true;
            case TX_SCRIPTHASH:
                CScript cScript3 = new CScript();
                if (cScriptArr2.length > 0) {
                    cScript2 = cScriptArr2[0];
                }
                if (!m10838a(daVar, cScript2, new CScriptID((byte[]) dbVar2.vSolutions.get(0)), cScript3)) {
                    return false;
                }
                list2.add(cScript3.copyToNewBytes());
                return true;
            case TX_MULTISIG:
                list2.add(new byte[0]);
                return mo43207a(daVar, charSequence, dbVar2.vSolutions, cjVar, cScript, list, cxVar);
            case TX_WITNESS_V0_SCRIPTHASH:
                new CRIPEMD160().mo40492a((byte[]) dbVar2.vSolutions.get(0)).mo40494b(ckVar.data());
                ckVar.updateHash();
                StringBuilder sb = new StringBuilder();
                sb.append("get wit script: ");
                sb.append(AddressUtils.getAddressString((CTxDestination) ckVar, ((Wallet) daVar2).getChainParams()));
                sb.append(" , ");
                sb.append(ckVar.hexString());
                Log.info("test", sb.toString());
                CScript cScript4 = new CScript();
                if (cScriptArr2.length > 0) {
                    cScript2 = cScriptArr2[0];
                }
                if (!m10838a(daVar, cScript2, ckVar, cScript4)) {
                    return false;
                }
                list2.add(cScript4.copyToNewBytes());
                return true;
            default:
                return false;
        }
    }

    /* renamed from: a */
    public static boolean m10838a(SigningProvider daVar, CScript cScript, CScriptID ckVar, CScript cScript2) {
        if (daVar.mo42778a(ckVar, cScript2)) {
            return true;
        }
        if (cScript != null) {
            if (new CScriptID(cScript).equals(ckVar)) {
                cScript2.initWithSeriableData((SeriableData) cScript);
                return true;
            } else if (cScript.mo9540f()) {
                CScript a = CScript.m484a((CTxDestination) new WitnessV0ScriptHash(cScript));
                if (new CScriptID(a).equals(ckVar)) {
                    cScript2.initWithSeriableData((SeriableData) a);
                    return true;
                }
            }
        }
        return false;
    }

    /* renamed from: a */
    public List<byte[]> mo43204a(CScript cScript, BaseSignatureChecker baseSignatureChecker, List<byte[]> list, List<byte[]> list2, List<byte[]> list3, SigVersion cxVar) {
        List<byte[]> list4 = list;
        Vector<byte[]> vector = new Vector<>();
        for (byte[] bArr : list2) {
            if (!(bArr == null || bArr.length == 0)) {
                vector.add(bArr);
            }
        }
        for (byte[] bArr2 : list3) {
            if (!(bArr2 == null || bArr2.length == 0)) {
                vector.add(bArr2);
            }
        }
        if (list.size() > 1) {
            byte b = (byte) ((list4.get(0))[0] & 0xff);
            int size = list.size() - 2;
            SparseArray gtVar = new SparseArray();
            Vector vector2 = new Vector(size);
            ListIterator listIterator = list4.listIterator(1);
            for (int i = 0; i < size; i++) {
                byte[] bArr3 = (byte[]) listIterator.next();
                int hashCode = Arrays.hashCode(bArr3);
                if (!vector2.contains(Integer.valueOf(hashCode))) {
                    vector2.add(new Pair(Integer.valueOf(hashCode), bArr3));
                }
            }
            for (byte[] bArr4 : vector) {
                Iterator it = vector2.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        CScript cScript2 = cScript;
                        BaseSignatureChecker baseSignatureChecker2 = baseSignatureChecker;
                        SigVersion cxVar2 = cxVar;
                        break;
                    }
                    Pair gsVar = (Pair) it.next();
                    if (!gtVar.isNotNull(((Integer) gsVar.key).intValue())) {
                        if (baseSignatureChecker.CheckSig(bArr4, (byte[]) gsVar.value, cScript, cxVar)) {
                            gtVar.put(((Integer) gsVar.key).intValue(), bArr4);
                            break;
                        }
                    }
                }
            }
            Vector vector3 = new Vector();
            vector3.add(new byte[0]);
            ListIterator listIterator2 = list4.listIterator(1);
            int i2 = 0;
            for (int i3 = 0; i3 < size && i2 < b; i3++) {
                byte[] bArr5 = (byte[]) gtVar.get(Arrays.hashCode((byte[]) listIterator2.next()));
                if (bArr5 != null) {
                    vector3.add(bArr5);
                    i2++;
                }
            }
            return vector3;
        }
        throw new IllegalArgumentException("vSolutions size must grater than 1.");
    }

    /* renamed from: a */
    public boolean mo43208a(SigningProvider daVar, CharSequence charSequence, CKeyID lVar, BaseSignatureCreator cjVar, CScript cScript, List<byte[]> list, SigVersion cxVar) {
        BytesArrayBuffer gdVar = new BytesArrayBuffer();
        if (!cjVar.mo43140a(daVar, charSequence, gdVar, lVar, cScript, cxVar)) {
            return false;
        }
        list.add(gdVar.copyToNewBytes());
        return true;
    }

    /* renamed from: a */
    public boolean mo43207a(SigningProvider daVar, CharSequence charSequence, List<byte[]> list, BaseSignatureCreator cjVar, CScript cScript, List<byte[]> list2, SigVersion cxVar) throws AddressFormatException {
        List<byte[]> list3 = list;
        byte b = (byte) (((byte[]) list3.get(0))[0] & 0xff);
        ListIterator listIterator = list3.listIterator(1);
        int i = 0;
        for (int i2 = 1; i2 < list.size() - 1 && i < b; i2++) {
            if (mo43208a(daVar, charSequence, (CKeyID) new CPubKey((byte[]) listIterator.next()).getCKeyID(), cjVar, cScript, list2, cxVar)) {
                i++;
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }

    //m10837a
    public static void UpdateTransaction(TxIn txIn, SignatureData signatureData) {
        try {
            txIn.getScriptSig().initByCScript(signatureData.cScript);
            txIn.setCScriptWitness(signatureData.cScriptWitness, new boolean[0]);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
