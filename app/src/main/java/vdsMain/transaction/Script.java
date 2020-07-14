package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.CNoDestination;
import bitcoin.CPubKey;
import bitcoin.VariableInteger;
import bitcoin.script.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.UnsignedBytes;
import com.vc.libcommon.exception.AddressFormatException;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.wallet.ChainParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class Script extends SeriableData {

    private static final HashMap<TxnOutType, Vector<Integer>> f12299e = new HashMap<>();

    //f12301b
    protected ScriptChunk firstChunk;

    //f12302c
    protected ScriptChunk endScripChunk;

    //f12303d
    private TxnOutType txnOutType;

    //f12305g
    private ScriptSigType scriptSigType;

    //f12300a
    protected List<ScriptChunk> scriptChunks;

    //f12304f
    private long timeStamp;

    public Script() {
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.firstChunk = null;
        this.endScripChunk = null;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        this.scriptChunks = Lists.newArrayList();
        this.timeStamp = Utils.getTimeStamp();
    }

    public Script(Script crVar) {
        super((SeriableData) crVar);
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.firstChunk = null;
        this.endScripChunk = null;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        mo43154a(crVar);
    }

    public Script(CScript cScript) throws ScriptException {
        super((SeriableData) cScript);
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.firstChunk = null;
        this.endScripChunk = null;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        initByCScript(cScript);
    }

    public Script(byte[] bArr) throws ScriptException {
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.firstChunk = null;
        this.endScripChunk = null;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        this.scriptChunks = Lists.newArrayList();
        this.timeStamp = Utils.getTimeStamp();
        initByBytes(bArr);
    }

    public Script(byte[] bArr, int i, int i2) throws ScriptException {
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.firstChunk = null;
        this.endScripChunk = null;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        this.scriptChunks = Lists.newArrayList();
        this.timeStamp = Utils.getTimeStamp();
        init(bArr, i, i2);
    }

    public CTxDestination mo43169j() {
        return getCtxDestinationFomFirstAndEndChunk();
    }

    //mo43173n
    public TxnOutType getTxnOutType() {
        return this.txnOutType;
    }

    public List<ScriptChunk> mo43161d() {
        return this.scriptChunks;
    }

    //mo43153a
    public void initByCScript(CScript cScript) throws ScriptException {
        initByBytes(cScript.copyToNewBytes());
    }

    public boolean mo43156a() {
        return mo43151a(193) == 1;
    }

    public boolean mo43158b() {
        return mo43151a(194) == 1;
    }

    public int mo43151a(int i) {
        List<ScriptChunk> list = this.scriptChunks;
        int i2 = 0;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        for (ScriptChunk csVar : this.scriptChunks) {
            if (csVar.opWord == i) {
                i2++;
            }
        }
        return i2;
    }

    //mo43179t
    public int getScriptLength() {
        List<ScriptChunk> list = this.scriptChunks;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        int i = 0;
        for (ScriptChunk scriptChunk : this.scriptChunks) {
            i += scriptChunk.mo43196j();
        }
        return i + VariableInteger.getLengthNative((long) i);
    }

    //mo43175p
    public boolean isScriptChunkListEmpty() {
        return this.scriptChunks.isEmpty();
    }

    private boolean m10771a(List<byte[]> list, List<CPubKey> list2, int i) {
        boolean z = false;
        if (list.isEmpty()) {
            return false;
        }
        if (list.size() == i) {
            z = true;
        }
        return z;
    }

    public int mo43172m() {
        int i = 0;
        if (this.endScripChunk == null) {
            return 0;
        }
        for (ScriptChunk c : this.scriptChunks) {
            if (c.opWordIsNA()) {
                i++;
            }
        }
        return i;
    }

    private boolean m10772a(byte[] bArr, CPubKey cPubKey) {
        return true;
    }

    //TODO 可能反编译有问题
    public boolean mo43176q() {
        ScriptChunk csVar=null;
        if (isScriptChunkListEmpty()) {
            return false;
        }
        if (isTxContractType()) {
            return true;
        }
        ListIterator listIterator = this.scriptChunks.listIterator();
        TxnOutType ddVar = TxnOutType.TX_NONSTANDARD;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (listIterator.hasNext()) {
            arrayList.clear();
            arrayList2.clear();
            ScriptChunk scriptChunk = (ScriptChunk) listIterator.next();
            if (DataTypeToolkit.m11505b(scriptChunk.opWord, 0)) {
                if (!listIterator.hasNext()) {
                    return false;
                }
                ListIterator listIterator2 = this.scriptChunks.listIterator(listIterator.nextIndex());
                Object next = listIterator2.next();
                while (listIterator2.hasNext()) {
                    csVar = (ScriptChunk) next;
                    if (csVar.opWordIsNA() && listIterator2.hasNext()) {
                        arrayList.add(csVar.mo43189e());
                        next = listIterator2.next();
                    }
                }
                if (Standard.m532a(csVar.opWord)) {
                    int i = csVar.getOP_N();
                    while (true) {
                        if (!listIterator2.hasNext()) {
                            break;
                        }
                        ScriptChunk csVar3 = (ScriptChunk) listIterator2.next();
                        if (csVar3.isDataOutput()) {
                            try {
                                arrayList2.add(new CPubKey(csVar3.bytes));
                            } catch (AddressFormatException e) {
                                e.printStackTrace();
                                return false;
                            }
                        } else if (Standard.m532a(csVar3.opWord) && listIterator2.hasNext() && DataTypeToolkit.m11505b(((ScriptChunk) listIterator2.next()).opWord, 174)) {
                            return m10771a((List<byte[]>) arrayList, (List<CPubKey>) arrayList2, i);
                        }
                    }
                } else {
                    continue;
                }
            } else if (!scriptChunk.opWordIsNA()) {
                continue;
            } else if (!listIterator.hasNext()) {
                return false;
            } else {
                arrayList.add(scriptChunk.mo43189e());
                ListIterator listIterator3 = this.scriptChunks.listIterator(listIterator.nextIndex());
                ScriptChunk csVar4 = (ScriptChunk) listIterator3.next();
                if (csVar4.isDataOutput()) {
                    try {
                        arrayList2.add(new CPubKey(csVar4.bytes));
                        if (!listIterator3.hasNext()) {
                            return m10772a((byte[]) arrayList.get(0), (CPubKey) arrayList2.get(0));
                        }
                        ScriptChunk csVar5 = (ScriptChunk) listIterator3.next();
                        if (DataTypeToolkit.m11505b(csVar5.opWord, 172)) {
                            return m10772a((byte[]) arrayList.get(0), (CPubKey) arrayList2.get(0));
                        }
                        if (DataTypeToolkit.m11505b(csVar5.opWord, 118)) {
                            if (listIterator3.hasNext() && DataTypeToolkit.m11505b(((ScriptChunk) listIterator3.next()).opWord, 169) && listIterator3.hasNext() && ((ScriptChunk) listIterator3.next()).isOpWordEqual20() && listIterator3.hasNext() && DataTypeToolkit.m11505b(((ScriptChunk) listIterator3.next()).opWord, 136) && listIterator3.hasNext() && DataTypeToolkit.m11505b(((ScriptChunk) listIterator3.next()).opWord, 172)) {
                                return m10772a((byte[]) arrayList.get(0), (CPubKey) arrayList2.get(0));
                            }
                            return false;
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return false;
                    }
                } else if (!listIterator.hasNext()) {
                    return true;
                } else {
                    while (csVar4.opWordIsNA()) {
                        arrayList.add(csVar4.mo43189e());
                        if (!listIterator3.hasNext()) {
                            break;
                        }
                        csVar4 = (ScriptChunk) listIterator3.next();
                    }
                    if (csVar4.mo43188d()) {
                        try {
                            Script crVar = new Script(csVar4.bytes);
                            int i2 = ((ScriptChunk) crVar.scriptChunks.get(0)).getOP_N();
                            arrayList2.addAll(crVar.mo43168i());
                            return m10771a((List<byte[]>) arrayList, (List<CPubKey>) arrayList2, i2);
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<CPubKey> mo43168i() {
        ArrayList arrayList = new ArrayList();
        if (this.firstChunk != null) {
            Iterator it = this.scriptChunks.iterator();
            while (it.hasNext()) {
                if (it.next() == this.firstChunk) {
                    break;
                }
            }
            ScriptChunk csVar = this.firstChunk;
            if (this.txnOutType == TxnOutType.TX_PUBKEY) {
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    try {
                        if (csVar.isDataOutput()) {
                            arrayList.add(new CPubKey(csVar.bytes));
                            break;
                        }
                        csVar = (ScriptChunk) it.next();
                    } catch (AddressFormatException e) {
                        e.printStackTrace();
                        return arrayList;
                    }
                }
            } else if (this.txnOutType == TxnOutType.TX_MULTISIG) {
                new Script().addScriptChunk(csVar);
                while (it.hasNext()) {
                    try {
                        if (csVar.isDataOutput()) {
                            arrayList.add(new CPubKey(csVar.bytes));
                        }
                        csVar = (ScriptChunk) it.next();
                    } catch (AddressFormatException e2) {
                        e2.printStackTrace();
                        arrayList.clear();
                        return arrayList;
                    }
                }
            }
        }
        if (this.endScripChunk != null) {
            Iterator it2 = this.scriptChunks.iterator();
            while (it2.hasNext()) {
                if (it2.next() == this.endScripChunk) {
                    break;
                }
            }
            ScriptChunk csVar2 = this.endScripChunk;
            if (this.txnOutType == TxnOutType.TX_PUBKEYHASH) {
                while (it2.hasNext()) {
                    if (csVar2.isDataOutput()) {
                        try {
                            arrayList.add(new CPubKey(csVar2.bytes));
                        } catch (AddressFormatException e3) {
                            e3.printStackTrace();
                            arrayList.clear();
                            return arrayList;
                        }
                    }
                    csVar2 = (ScriptChunk) it2.next();
                }
            } else if (this.txnOutType == TxnOutType.TX_SCRIPTHASH) {
                while (it2.hasNext()) {
                    if (csVar2.mo43188d()) {
                        try {
                            arrayList.addAll(new Script(csVar2.bytes).mo43168i());
                            return arrayList;
                        } catch (ScriptException e4) {
                            e4.printStackTrace();
                            arrayList.clear();
                            return arrayList;
                        }
                    } else {
                        csVar2 = (ScriptChunk) it2.next();
                    }
                }
            }
        }
        return arrayList;
    }


    private void m10782v() {
        checkScriptType(getScriptChunksBytes());
    }

    public Script mo43170k() {
        Script script = new Script();
        for (ScriptChunk scriptChunk : this.scriptChunks) {
            if (scriptChunk == this.firstChunk && this.txnOutType != TxnOutType.TX_MULTISIG) {
                break;
            }
            script.addScriptChunk(scriptChunk);
        }
        script.m10782v();
        return script;
    }

    public List<byte[]> mo43171l() {
        if (this.endScripChunk == null) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        boolean z = false;
        for (ScriptChunk csVar : this.scriptChunks) {
            if (csVar == this.endScripChunk) {
                z = true;
                byte[] e = csVar.mo43189e();
                if (e != null) {
                    linkedList.add(e);
                }
            } else if (z) {
                byte[] e2 = csVar.mo43189e();
                if (e2 == null) {
                    break;
                }
                linkedList.add(e2);
            } else {
                continue;
            }
        }
        return linkedList;
    }

    //mo43177r
    public boolean isTxContractType() {
        switch (this.txnOutType) {
            case TX_CONTRACT_CREATE:
            case TX_CONTRACT_CALL:
            case TX_CONTRACT_SPENT:
                return true;
            default:
                return false;
        }
    }

    //m10757F
    private ListIterator<ScriptChunk> getListIteratorIfEqualFirstChunk() {
        if (this.firstChunk != null) {
            List<ScriptChunk> list = this.scriptChunks;
            if (list != null) {
                ListIterator<ScriptChunk> listIterator = list.listIterator();
                while (listIterator.hasNext()) {
                    if (listIterator.next() == this.firstChunk) {
                        return listIterator;
                    }
                }
                return null;
            }
        }
        return null;
    }

    //m10765a
    private ScriptChunk getAfterScriptChunk(ListIterator<ScriptChunk> listIterator, int loopTime) {
        ScriptChunk scriptChunk = null;
        for (int i = 0; i < loopTime; i++) {
            if (!listIterator.hasNext()) {
                return null;
            }
            scriptChunk = (ScriptChunk) listIterator.next();
        }
        return scriptChunk;
    }

    //m10777b
    private byte[] getAfterScriptChunkBytes(ListIterator<ScriptChunk> listIterator, int i) {
        ScriptChunk afterScriptChunk = getAfterScriptChunk(listIterator, i);
        if (afterScriptChunk != null && afterScriptChunk.isValuePushingWord()) {
            return afterScriptChunk.bytes;
        }
        return null;
    }

    //mo43166h
    public byte[] getAfterFirstScriptChunkBytes() {
        if (this.txnOutType != TxnOutType.TX_CONTRACT_CALL) {
            return null;
        }
        ListIterator listIterator = getListIteratorIfEqualFirstChunk();
        if (listIterator == null) {
            return null;
        }
        return getAfterScriptChunkBytes(listIterator, 4);
    }

    //mo43155a
    public void addScriptChunk(ScriptChunk csVar) {
        this.scriptChunks.add(csVar);
    }

    //m10756E
    private byte[] getBytesIfEqualFirstChunk() {
        Iterator it = this.scriptChunks.iterator();
        while (it.hasNext()) {
            if (it.next() == this.firstChunk && it.hasNext()) {
                return ((ScriptChunk) it.next()).bytes;
            }
        }
        return null;
    }

    //mo43165g
    public CTxDestination getCTxDestinationByOutTypeSinceFirstChunk() {
        Iterator it = this.scriptChunks.iterator();
        while (it.hasNext()) {
            if (it.next() == this.firstChunk) {
                break;
            }
        }
        if (this.txnOutType == TxnOutType.TX_PUBKEY) {
            if (it.hasNext() && this.firstChunk.isDataOutput()) {
                return new CKeyID(CHash160.encodeToUInt160(this.firstChunk.bytes));
            }
        } else if (this.txnOutType == TxnOutType.TX_PUBKEYHASH) {
            if (!it.hasNext()) {
                return new CNoDestination();
            }
            if (((ScriptChunk) it.next()).opWord != 169) {//OP_HASH160
                return new CNoDestination();
            }
            if (!it.hasNext()) {
                return new CNoDestination();
            }
            ScriptChunk scriptChunk = (ScriptChunk) it.next();
            if (scriptChunk.isOpWordEqual20()) {
                return new CKeyID(scriptChunk.bytes);
            }
        } else if (this.txnOutType == TxnOutType.TX_MULTISIG) {
            if (this.firstChunk.mo43188d()) {
                return new CScriptID(CHash160.encodeToUInt160(this.firstChunk.bytes));
            }
            Script script = new Script();
            script.addScriptChunk(this.firstChunk);
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ScriptChunk csVar2 = (ScriptChunk) it.next();
                if (csVar2.isDataOutput()) {
                    script.addScriptChunk(csVar2);
                } else if (csVar2.opWord != 174) {//OP_NOP4
                    return new CNoDestination();
                }
            }
            return new CScriptID(CHash160.encodeToUInt160(script.getScriptChunksBytes()));
        } else if (this.txnOutType == TxnOutType.TX_SCRIPTHASH) {
            if (it.hasNext()) {
                ScriptChunk scriptChunk = (ScriptChunk) it.next();
                if (scriptChunk.isOpWordEqual20()) {
                    return new CScriptID(scriptChunk.bytes);
                }
            }
        } else if (this.txnOutType == TxnOutType.TX_WITNESS_V0_KEYHASH) {
            return new WitnessV0KeyHash(getBytesIfEqualFirstChunk());
        } else {
            if (this.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH) {
                return new WitnessV0ScriptHash(getBytesIfEqualFirstChunk());
            }
            if (this.txnOutType == TxnOutType.TX_WITNESS_UNKNOWN) {
                WitnessUnknown witnessUnknown = new WitnessUnknown();
                witnessUnknown.f417a = decodeOP_N((int) this.firstChunk.bytes[0] & UnsignedBytes.MAX_VALUE);
                witnessUnknown.f418b = this.firstChunk.bytes.length - 1;
                witnessUnknown.f419c = DataTypeToolkit.copyPartBytes(this.firstChunk.bytes, 1, witnessUnknown.f418b);
                return witnessUnknown;
            }
        }
        return new CNoDestination();
    }

    //mo43164f
    public CTxDestination getCTxDestinationByTypeSinceEndChunk() {
        Iterator it = this.scriptChunks.iterator();
        while (it.hasNext()) {
            if (it.next() == this.endScripChunk) {
                break;
            }
        }
        if (this.txnOutType == TxnOutType.TX_PUBKEYHASH) {
            if (it.hasNext()) {
                ScriptChunk scriptChunk = (ScriptChunk) it.next();
                if (scriptChunk.isOpWordEqual20()) {
                    return new CKeyID(scriptChunk.bytes);
                }
                if (scriptChunk.isDataOutput()) {
                    try {
                        return new CKeyID(CHash160.encodeToUInt160(scriptChunk.bytes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return new CNoDestination();
        } else if (this.txnOutType == TxnOutType.TX_PUBKEY) {
            if (it.hasNext()) {
                ScriptChunk csVar2 = (ScriptChunk) it.next();
                if (csVar2.isDataOutput()) {
                    return new CKeyID(CHash160.encodeToUInt160(csVar2.bytes));
                }
            }
            return new CNoDestination();
        } else {
            if (this.txnOutType == TxnOutType.TX_SCRIPTHASH) {
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ScriptChunk csVar3 = (ScriptChunk) it.next();
                    if (!csVar3.opWordIsNA()) {
                        if (csVar3.mo43188d()) {
                            return new CScriptID(CHash160.encodeToUInt160(csVar3.bytes));
                        }
                    }
                }
            } else if (this.txnOutType == TxnOutType.TX_WITNESS_V0_KEYHASH) {
                return new CScriptID(m10760a(new WitnessV0KeyHash(DataTypeToolkit.m11500a(this.endScripChunk.bytes, 2))));
            } else {
                if (this.txnOutType == TxnOutType.TX_WITNESS_V0_SCRIPTHASH) {
                    return new WitnessV0ScriptHash(DataTypeToolkit.m11500a(this.endScripChunk.bytes, 2));
                }
                if (this.txnOutType == TxnOutType.TX_WITNESS_UNKNOWN) {
                    WitnessUnknown witnessUnknown = new WitnessUnknown();
                    witnessUnknown.f417a = decodeOP_N((int) this.endScripChunk.bytes[1] & 0xff);
                    witnessUnknown.f418b = this.endScripChunk.bytes.length - 2;
                    witnessUnknown.f419c = DataTypeToolkit.copyPartBytes(this.endScripChunk.bytes, 2, witnessUnknown.f418b);
                }
            }
            return new CNoDestination();
        }
    }

    //mo43162e
    public CTxDestination getCtxDestinationFomFirstAndEndChunk() {
        if (isTxContractType()) {
            byte[] h = getAfterFirstScriptChunkBytes();
            if (h == null) {
                return new CNoDestination();
            }
            return new CTxDestinationContract(h);
        }
        CTxDestination cTxDestination = null;
        if (this.firstChunk != null) {
            cTxDestination = getCTxDestinationByOutTypeSinceFirstChunk();
        }
        if ((cTxDestination == null || (cTxDestination instanceof CNoDestination)) && this.endScripChunk != null) {
            return getCTxDestinationByTypeSinceEndChunk();
        }
        if (cTxDestination == null) {
            cTxDestination = new CNoDestination();
        }
        return cTxDestination;
    }

    public String mo43152a(@NonNull ChainParams uVar) {
        CTxDestination e = getCtxDestinationFomFirstAndEndChunk();
        if (e == null || (e instanceof CNoDestination)) {
            return null;
        }
        return AddressUtils.getAddressString(e, uVar);
    }

    public int mo43174o() {
        if (this.txnOutType == TxnOutType.TX_MULTISIG) {
            ScriptChunk csVar = this.firstChunk;
            if (csVar == null) {
                return -1;
            }
            return csVar.getOP_N();
        } else if (this.txnOutType != TxnOutType.TX_SCRIPTHASH || this.endScripChunk == null) {
            return -1;
        } else {
            boolean z = false;
            for (ScriptChunk csVar2 : this.scriptChunks) {
                if (csVar2 == this.endScripChunk) {
                    z = true;
                }
                if (z && !csVar2.opWordIsNA()) {
                    if (csVar2 == this.firstChunk) {
                        break;
                    }
                    MultipleSignatureScriptInfo multipleSignatureScriptInfo = new MultipleSignatureScriptInfo();
                    if (m10770a(csVar2, multipleSignatureScriptInfo, true)) {
                        return multipleSignatureScriptInfo.M;
                    }
                }
            }
            return -1;
        }
    }

    public void mo43154a(Script crVar) {
        this.scriptChunks = new ArrayList();
        List<ScriptChunk> list = crVar.scriptChunks;
        if (list != null && !list.isEmpty()) {
            for (ScriptChunk csVar : crVar.scriptChunks) {
                ScriptChunk csVar2 = new ScriptChunk(csVar);
                this.scriptChunks.add(csVar2);
                if (csVar == crVar.firstChunk) {
                    this.firstChunk = csVar2;
                } else if (csVar == crVar.endScripChunk) {
                    this.endScripChunk = csVar2;
                }
            }
        }
        this.timeStamp = crVar.timeStamp;
        this.txnOutType = crVar.txnOutType;
    }

    //m10768a
    private void initByBytes(byte[] bArr) throws ScriptException {
            init(bArr, 0, bArr.length);
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        byte[] c = getScriptChunksBytes();
        if (c == null) {
            streamWriter.writeVariableInt(0);
            return;
        }
        streamWriter.writeVariableInt((long) c.length);
        streamWriter.write(c);
    }

    public void onDecodeSerialData() throws IOException {
        int b = readVariableInt().getIntValue();
        if (b > 0) {
            try {
                init(readBytes(b), 0, b);
            } catch (ScriptException e) {
                throw new IOException(e);
            }
        } else {
            this.scriptChunks.clear();
        }
    }

    //mo43159c
    public byte[] getScriptChunksBytes() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (ScriptChunk scriptChunk : this.scriptChunks) {
                scriptChunk.writeScriptChunkToSteam((OutputStream) byteArrayOutputStream);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //mo43160d
    public Script addNoBytesScriptChunk(int i) {
        Preconditions.checkArgument(i >= 0 || i <= 255);
        addScriptChunk(new ScriptChunk(i, null));
        return this;
    }

    //mo43157b
    public Script addDataBytes(byte[] bArr, boolean z) {
        this.scriptChunks.add(m10766a(bArr, z));
        return this;
    }

    //m10763a
    public static Script addTxPubkeyHashChunk(CKeyID cKeyID) {
        Script script = new Script();
        script.addNoBytesScriptChunk(118);
        script.addNoBytesScriptChunk(169);
        script.addDataBytes(cKeyID.data(), true);
        script.addNoBytesScriptChunk(136);
        script.addNoBytesScriptChunk(172);
        script.txnOutType = TxnOutType.TX_PUBKEYHASH;
        return script;
    }

    //m10762a
    public static Script addTxScriptHashChunk(CScriptID ckVar) {
        Script script = new Script();
        script.addNoBytesScriptChunk(169);
        script.addDataBytes(ckVar.data(), true);
        script.addNoBytesScriptChunk(135);
        script.txnOutType = TxnOutType.TX_SCRIPTHASH;
        return script;
    }

    //m10761a
    public static Script addTxWitnessV0ScriptHash(WitnessV0ScriptHash witnessV0ScriptHash) {
        Script script = new Script();
        script.addNoBytesScriptChunk(0);
        script.addDataBytes(witnessV0ScriptHash.data(), true);
        script.txnOutType = TxnOutType.TX_WITNESS_V0_SCRIPTHASH;
        return script;
    }

    public static Script m10759a(CNoDestination cNoDestination) {
        return new Script();
    }


    public static Script getContractCallScript(ContractCallInfo contractCallInfo) {
        Script script = new Script();
        script.addDataBytes(new byte[]{contractCallInfo.vm_version},true);
        //script.addDataBytes(DataTypeToolkit.m11499a(contractCallInfo.gas_limit),true);
        script.addDataBytes(DataTypeToolkit.trimZeroBytes(DataTypeToolkit.getBytesAndReverse(contractCallInfo.gas_limit)),true);
        script.addDataBytes(new byte[]{contractCallInfo.gas_price},true);
        byte[] byes=StringToolkit.getBytes(contractCallInfo.getDataHex());
        script.addDataBytes(byes,true);
        script.addDataBytes(StringToolkit.getBytes(contractCallInfo.contract_address),true);
        script.addNoBytesScriptChunk(194);
        script.txnOutType = TxnOutType.TX_CONTRACT_CALL;
        script.firstChunk = (ScriptChunk) script.scriptChunks.get(0);
//        script.scriptSigType = ScriptSigType.LOCKED;
        return script;
    }

    //m10764a
    public static Script getLockedSignScript(CTxDestination cTxDestination) {
        Script script = cTxDestination instanceof CKeyID ? addTxPubkeyHashChunk((CKeyID) cTxDestination) : cTxDestination instanceof CScriptID ? addTxScriptHashChunk((CScriptID) cTxDestination) : cTxDestination instanceof CNoDestination ? m10759a((CNoDestination) cTxDestination) : cTxDestination instanceof WitnessV0KeyHash ? m10760a((WitnessV0KeyHash) cTxDestination) : cTxDestination instanceof WitnessV0ScriptHash ? addTxWitnessV0ScriptHash((WitnessV0ScriptHash) cTxDestination) : null;
        if (script != null) {
            script.firstChunk = (ScriptChunk) script.scriptChunks.get(0);
            script.scriptSigType = ScriptSigType.LOCKED;
        }
        return script;
    }

    //m10778c
    public static int encodeOP_N(int i) {
        boolean z = i >= -1 && i <= 16;
        StringBuilder sb = new StringBuilder();
        sb.append("encodeToOpN called for ");
        sb.append(i);
        sb.append(" which we cannot encode in an opcode.");
        Preconditions.checkArgument(z, sb.toString());
        if (i == 0) {
            return 0;
        }
        if (i == -1) {
            return 79;
        }
        return (i - 1) + 81;
    }

    public static ScriptChunk m10766a(byte[] bArr, boolean z) {
        byte[] bArr2;
        int opWord = 76;//OP_PUSHDATA1
        if (bArr.length < 76) {
            opWord = bArr.length;
            bArr2 = z ? Arrays.copyOf(bArr, bArr.length) : bArr;
        } else if (bArr.length <= 255) {
            bArr2 = new byte[(bArr.length + 1)];
            bArr2[0] = (byte) bArr.length;
        } else if (bArr.length <= 65535) {
            opWord = 77;//OP_PUSHDATA2
            bArr2 = new byte[(bArr.length + 2)];
            Utils.m3453b(bArr.length, bArr2, 0);
            System.arraycopy(bArr, 0, bArr2, 2, bArr.length);
        } else {
            opWord = 78;//OP_PUSHDATA4
            bArr2 = new byte[(bArr.length + 4)];
            Utils.uint32ToByteArrayLE((long) bArr.length, bArr2, 0);
            System.arraycopy(bArr, 0, bArr2, 4, bArr.length);
        }
        return new ScriptChunk(opWord, bArr2);
    }

    public static Script m10760a(WitnessV0KeyHash witnessV0KeyHash) {
        Script script = new Script();
        script.addNoBytesScriptChunk(0);
        script.addDataBytes(witnessV0KeyHash.data(), true);
        script.txnOutType = TxnOutType.TX_WITNESS_V0_KEYHASH;
        return script;
    }

    /**
     Word	        Opcode	Hex	        Input	    Output	        Description
     OP_0, OP_FALSE	0	    0x00	    Nothing.	(empty value)	An empty array of bytes is pushed onto the stack. (This is not a no-op: an item is added to the stack.)
     N/A	        1-75	0x01-0x4b	(special)	data	        The next opcode bytes is data to be pushed onto the stack
     OP_PUSHDATA1	76	    0x4c	    (special)	data	        The next byte contains the number of bytes to be pushed onto the stack.
     OP_PUSHDATA2	77	    0x4d	    (special)	data	        The next two bytes contain the number of bytes to be pushed onto the stack in little endian order.
     OP_PUSHDATA4	78	    0x4e	    (special)	data	        The next four bytes contain the number of bytes to be pushed onto the stack in little endian order.
     */
    //m10758a
    private static long getDataLength(int i, ByteArrayInputStream byteArrayInputStream) throws ScriptException {
        if (i >= 0 && i < 76) {
            return (long) i;
        }
        if (i == 76) {
            if (byteArrayInputStream.available() >= 1) {
                return (long) byteArrayInputStream.read();
            }
            throw new ScriptException("Unexpected end of script");
        } else if (i == 77) {
            if (byteArrayInputStream.available() >= 2) {
                return (long) (byteArrayInputStream.read() | (byteArrayInputStream.read() << 8));
            }
            throw new ScriptException("Unexpected end of script");
        } else if (i != 78) {
            return -1;
        } else {//i == 78
            if (byteArrayInputStream.available() >= 4) {
                return (((long) byteArrayInputStream.read()) << 24) |
                        (((long) byteArrayInputStream.read()) << 8) |
                        ((long) byteArrayInputStream.read()) |
                        (((long) byteArrayInputStream.read()) << 16);
            }
            throw new ScriptException("Unexpected end of script");
        }
    }

    //m10769
    private void init(byte[] bArr, int off, int len) throws ScriptException {
        ScriptChunk scriptChunk;
        this.scriptChunks = new ArrayList(5);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, off, len);
        byteArrayInputStream.available();
        while (byteArrayInputStream.available() > 0) {
            byteArrayInputStream.available();
            int opWord = byteArrayInputStream.read();
            long pushedData = getDataLength(opWord, byteArrayInputStream);
            if (pushedData == -1) {
                scriptChunk = new ScriptChunk(opWord, null);
            } else {
                boolean z = false;
                if (pushedData > ((long) byteArrayInputStream.available())) {
                    long available = (long) byteArrayInputStream.available();
                    if (available > 0) {
                        int i3 = (int) available;
                        byte[] bArr2 = new byte[i3];
                        byteArrayInputStream.read(bArr2, 0, i3);
                        scriptChunk = new ScriptChunk(opWord, bArr2);
                    } else {
                        scriptChunk = new ScriptChunk(opWord, null);
                    }
                } else {
                    int i4 = (int) pushedData;
                    byte[] bArr3 = new byte[i4];
                    if (pushedData == 0 || ((long) byteArrayInputStream.read(bArr3, 0, i4)) == pushedData) {
                        z = true;
                    }
                    Preconditions.checkState(z);
                    scriptChunk = new ScriptChunk(opWord, bArr3);
                }
            }
            this.scriptChunks.add(scriptChunk);
        }
        checkScriptType(bArr);
    }

    //m10775b
    private void checkScriptType(byte[] bArr) {
        this.firstChunk = null;
        this.endScripChunk = null;
        this.txnOutType = TxnOutType.TX_NONSTANDARD;
        this.scriptSigType = ScriptSigType.UNKNOWN_TYPE;
        checkPartTxType(bArr, (List<byte[]>) new Vector<byte[]>());
        m10786z();
    }

    private void m10786z() {
        if (this.scriptSigType == ScriptSigType.UNKNOWN_TYPE) {
            if (this.txnOutType == TxnOutType.TX_PUBKEYHASH) {
                m10753B();
            } else if (this.txnOutType == TxnOutType.TX_PUBKEY) {
                m10754C();
            } else if (this.txnOutType == TxnOutType.TX_MULTISIG) {
                m10755D();
            } else if (this.txnOutType != TxnOutType.TX_SCRIPTHASH && this.txnOutType == TxnOutType.TX_NONSTANDARD) {
                m10752A();
            }
        }
    }

    private void m10753B() {
        List<ScriptChunk> list = this.scriptChunks;
        ListIterator listIterator = list.listIterator(list.size());
        if (this.firstChunk != null) {
            while (listIterator.hasPrevious()) {
                if (((ScriptChunk) listIterator.previous()) == this.firstChunk) {
                    if (!listIterator.hasPrevious()) {
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    } else if (!((ScriptChunk) listIterator.previous()).isDataOutput()) {
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    } else if (!listIterator.hasPrevious()) {
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    } else {
                        ScriptChunk csVar = (ScriptChunk) listIterator.previous();
                        if (csVar.opWordIsNA()) {
                            this.endScripChunk = csVar;
                            if (this.firstChunk == null) {
                                this.scriptSigType = ScriptSigType.UNLOCK;
                            } else {
                                this.scriptSigType = ScriptSigType.VERIFY;
                            }
                            return;
                        }
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    }
                }
            }
        }
    }

    private void m10754C() {
        List<ScriptChunk> list = this.scriptChunks;
        ListIterator listIterator = list.listIterator(list.size());
        if (this.firstChunk != null) {
            while (listIterator.hasPrevious()) {
                if (((ScriptChunk) listIterator.previous()) == this.firstChunk) {
                    if (!listIterator.hasPrevious()) {
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    }
                    ScriptChunk csVar = (ScriptChunk) listIterator.previous();
                    if (csVar.opWordIsNA()) {
                        this.endScripChunk = csVar;
                        if (this.firstChunk == null) {
                            this.scriptSigType = ScriptSigType.UNLOCK;
                        } else {
                            this.scriptSigType = ScriptSigType.VERIFY;
                        }
                        return;
                    }
                    this.scriptSigType = ScriptSigType.LOCKED;
                    return;
                }
            }
        }
    }

    private void m10755D() {
        List<ScriptChunk> list = this.scriptChunks;
        ListIterator listIterator = list.listIterator(list.size());
        if (this.firstChunk != null) {
            while (true) {
                if (!listIterator.hasPrevious()) {
                    break;
                } else if (((ScriptChunk) listIterator.previous()) == this.firstChunk) {
                    if (!listIterator.hasPrevious()) {
                        this.scriptSigType = ScriptSigType.LOCKED;
                        return;
                    }
                }
            }
        }
        Stack stack = new Stack();
        while (true) {
            if (!listIterator.hasPrevious()) {
                break;
            }
            ScriptChunk csVar = (ScriptChunk) listIterator.previous();
            if (csVar.opWord == 0) {
                while (!stack.isEmpty()) {
                    if (!m10776b((ScriptChunk) stack.pop())) {
                        break;
                    }
                }
                this.endScripChunk = csVar;
            } else {
                stack.push(csVar);
            }
        }
        this.scriptSigType = ScriptSigType.LOCKED;
    }

    private boolean m10776b(ScriptChunk scriptChunk) {
        if (scriptChunk.opWord > 0 && scriptChunk.opWord <= 78) {
            decodeOP_N(scriptChunk.opWord);
            if (!vdsMain.Utils.m13316b(scriptChunk.bytes)) {
                return false;
            }
        }
        return false;
    }

    private void m10752A() {
        List<ScriptChunk> list = this.scriptChunks;
        ListIterator<ScriptChunk> listIterator = list.listIterator(list.size());
        ScriptChunk tempScriptChunk = null;
        int i = 0;
        while (listIterator.hasPrevious()) {
            ScriptChunk scriptChunk = (ScriptChunk) listIterator.previous();
            if (scriptChunk.opWordIsNA()) {
                i++;
                //TODO 先改了试试
                if (i == 1) {
                    if (tempScriptChunk == null) {
                        this.endScripChunk = scriptChunk;
                        this.txnOutType = TxnOutType.TX_PUBKEY;
                        this.scriptSigType = ScriptSigType.UNLOCK;
                    } else if (scriptChunk.isDataOutput()) {
                        this.endScripChunk = listIterator.previous();
                        this.txnOutType = TxnOutType.TX_PUBKEYHASH;
                        this.scriptSigType = ScriptSigType.UNLOCK;
                    }
                }
            } else if (scriptChunk.opWord == 0) {
                if (i > 0) {
                    this.endScripChunk = scriptChunk;
                    this.txnOutType = TxnOutType.TX_MULTISIG;
                    this.scriptSigType = ScriptSigType.UNLOCK;
                    return;
                }
            } else if (i != 1) {
                tempScriptChunk = scriptChunk;
            } else if (tempScriptChunk != null && tempScriptChunk.isDataOutput()) {
                this.endScripChunk = scriptChunk;
                this.scriptSigType = ScriptSigType.UNLOCK;
                this.txnOutType = TxnOutType.TX_PUBKEYHASH;
            }
        }
    }

    private boolean m10770a(ScriptChunk chunk, MultipleSignatureScriptInfo multipleSignatureScriptInfo, boolean z) {
        if ((chunk.opWord < 76 && chunk.opWord > 78) || !chunk.isLengthBigger(4) || DataTypeToolkit.m11503b(chunk.bytes[chunk.bytes.length - 1]) != 174) {
            return false;
        }
        multipleSignatureScriptInfo.clear();
        try {
            Script script = new Script(chunk.bytes);
            multipleSignatureScriptInfo.script = script;
            int size = script.scriptChunks.size() - 2;
            Iterator iterator = script.scriptChunks.iterator();
            int i = 0;
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                ScriptChunk scriptChunk = (ScriptChunk) iterator.next();
                if (i == 0) {
                    multipleSignatureScriptInfo.M = decodeOP_N(scriptChunk.opWord);
                } else if (i == size) {
                    multipleSignatureScriptInfo.N = decodeOP_N(scriptChunk.opWord);
                    break;
                } else {
                    multipleSignatureScriptInfo.pubKeysList.add(scriptChunk.bytes);
                }
                i++;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //m10783w
    //910 m10908w
    private boolean checkIsScriptHashTx() {
        ListIterator listIterator = this.scriptChunks.listIterator();
        while (listIterator.hasNext()) {
            ScriptChunk scriptChunk = (ScriptChunk) listIterator.next();
            if (scriptChunk.opWord == 169) {//OP_HASH160
                if (!listIterator.hasNext()) {
                    return false;
                }
                ListIterator hash160NextIterator = this.scriptChunks.listIterator(listIterator.nextIndex());
                ScriptChunk pubkeyHashData = (ScriptChunk) listIterator.next();
                if (pubkeyHashData.opWord != 20) {
                    listIterator.previous();
                } else if (!pubkeyHashData.isLengthBigger(20)) {
                    continue;
                } else if (!listIterator.hasNext()) {
                    return false;
                } else {
                    if (((ScriptChunk) listIterator.next()).opWord == 135) {//OP_EQUAL
                        this.txnOutType = TxnOutType.TX_SCRIPTHASH;
                        this.firstChunk = scriptChunk;
                        ScriptChunk csVar3 = null;
                        boolean z = false;
                        while (true) {
                            if (!hash160NextIterator.hasPrevious()) {
                                break;
                            }
                            ScriptChunk csVar4 = (ScriptChunk) hash160NextIterator.previous();
                            if (csVar4.opWordIsNA()) {
                                if (!z) {
                                    this.scriptSigType = ScriptSigType.LOCKED;
                                    break;
                                }
                                csVar3 = csVar4;
                            } else if (csVar3 == null) {
                                MultipleSignatureScriptInfo cnVar = new MultipleSignatureScriptInfo();
                                if (z || !m10770a(csVar4, cnVar, false)) {
                                    this.scriptSigType = ScriptSigType.LOCKED;
                                } else {
                                    z = true;
                                }
                            } else if (csVar3 == null || !z) {
                                this.scriptSigType = ScriptSigType.LOCKED;
                            } else {
                                this.scriptSigType = ScriptSigType.VERIFY;
                                this.endScripChunk = csVar3;
                            }
                        }
                        if (this.scriptSigType == ScriptSigType.UNKNOWN_TYPE) {
                            if (!z || csVar3 == null) {
                                this.scriptSigType = ScriptSigType.LOCKED;
                            } else {
                                this.endScripChunk = csVar3;
                                this.scriptSigType = ScriptSigType.VERIFY;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean m10779c(ScriptChunk scriptChunk) {
        if (!scriptChunk.isValuePushingWord()) {
            return false;
        }
        if ((scriptChunk.bytes[0] == 0 || (scriptChunk.bytes[0] & UnsignedBytes.MAX_VALUE) >= 81) && (scriptChunk.bytes[0] & UnsignedBytes.MAX_VALUE) <= 96 && (scriptChunk.bytes[1] & UnsignedBytes.MAX_VALUE) + 2 == scriptChunk.bytes.length) {
            ScriptChunk csVar2 = new ScriptChunk(scriptChunk.bytes[1], DataTypeToolkit.m11500a(scriptChunk.bytes, 2));
            if (csVar2.isValuePushingWord()) {
                if (csVar2.bytes != null) {
                    if (csVar2.bytes.length == 20) {
                        this.txnOutType = TxnOutType.TX_WITNESS_V0_KEYHASH;
                        this.endScripChunk = scriptChunk;
                        return true;
                    } else if (csVar2.bytes.length == 32) {
                        this.txnOutType = TxnOutType.TX_WITNESS_V0_SCRIPTHASH;
                        this.endScripChunk = scriptChunk;
                        return true;
                    }
                }
            } else if (!(!csVar2.isOpWordEqual79OrZero() || scriptChunk.bytes == null || scriptChunk.bytes.length == 0)) {
                this.txnOutType = TxnOutType.TX_WITNESS_UNKNOWN;
                this.endScripChunk = scriptChunk;
                return true;
            }
        }
        return false;
    }

    //910 m10910y
    //m10785y
    private boolean isWitnessTxType() {
        if (this.scriptChunks.size() == 1 && m10779c((ScriptChunk) this.scriptChunks.get(0))) {
            return true;
        }
        ListIterator listIterator = this.scriptChunks.listIterator();
        while (listIterator.hasNext()) {
            ScriptChunk scriptChunk = (ScriptChunk) listIterator.next();
            if (listIterator.hasNext()) {
                if (scriptChunk.opWord == 0) {
                    if (!listIterator.hasNext()) {
                        return false;
                    }
                    ScriptChunk csVar2 = (ScriptChunk) listIterator.next();
                    if (csVar2.isValuePushingWord() && !listIterator.hasNext() && csVar2.bytes != null) {
                        if (csVar2.bytes.length == 20) {
                            this.txnOutType = TxnOutType.TX_WITNESS_V0_KEYHASH;
                            this.firstChunk = scriptChunk;
                            return true;
                        } else if (csVar2.bytes.length == 32) {
                            this.txnOutType = TxnOutType.TX_WITNESS_V0_SCRIPTHASH;
                            this.firstChunk = scriptChunk;
                            return true;
                        }
                    }
                } else if (!scriptChunk.isOpWordEqual79OrZero()) {
                    continue;
                } else if (!listIterator.hasNext()) {
                    return false;
                } else {
                    if (!(scriptChunk.bytes == null || scriptChunk.bytes.length == 0)) {
                        this.txnOutType = TxnOutType.TX_WITNESS_UNKNOWN;
                        this.firstChunk = scriptChunk;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //910 m10909x
    //m10784x
    private boolean isContractTxType() {
        if (this.scriptSigType != ScriptSigType.UNKNOWN_TYPE) {
            return false;
        }
        ListIterator listIterator = this.scriptChunks.listIterator();
        int i = 0;
        while (listIterator.hasNext()) {
            ScriptChunk scriptChunk = (ScriptChunk) listIterator.next();
            if (scriptChunk.isNotValuePushingWord()) {
                if (scriptChunk.opWord == 193) {
                    if (i < 4) {
                        continue;
                    } else {
                        ListIterator listIterator2 = this.scriptChunks.listIterator(i);
                        if (((ScriptChunk) listIterator2.previous()).isValuePushingWord() && ((ScriptChunk) listIterator2.previous()).isValuePushingWord() && ((ScriptChunk) listIterator2.previous()).isValuePushingWord()) {
                            ScriptChunk csVar2 = (ScriptChunk) listIterator2.previous();
                            if (csVar2.isValuePushingWord()) {
                                this.txnOutType = TxnOutType.TX_CONTRACT_CREATE;
                                this.firstChunk = csVar2;
                                return true;
                            }
                        }
                    }
                } else if (scriptChunk.opWord == 194) {
                    if (i < 5) {
                        continue;
                    } else {
                        ListIterator listIterator3 = this.scriptChunks.listIterator(i);
                        ScriptChunk csVar3 = (ScriptChunk) listIterator3.previous();
                        if ((csVar3.isValuePushingWord() || (csVar3.bytes != null && csVar3.bytes.length == 20)) && ((ScriptChunk) listIterator3.previous()).isValuePushingWord() && ((ScriptChunk) listIterator3.previous()).isValuePushingWord() && ((ScriptChunk) listIterator3.previous()).isValuePushingWord()) {
                            ScriptChunk csVar4 = (ScriptChunk) listIterator3.previous();
                            if (csVar4.isValuePushingWord()) {
                                this.txnOutType = TxnOutType.TX_CONTRACT_CALL;
                                this.firstChunk = csVar4;
                                return true;
                            }
                        }
                    }
                } else if (scriptChunk.opWord == 195) {
                    List<ScriptChunk> list = this.scriptChunks;
                    if (list != null && list.size() == 1) {
                        this.txnOutType = TxnOutType.TX_CONTRACT_SPENT;
                        this.firstChunk = scriptChunk;
                        return true;
                    }
                } else {
                    continue;
                }
            }
            i++;
        }
        return false;
    }

    //m10773a
    private boolean checkPartTxType(byte[] bArr, List<byte[]> list) {
        list.clear();
        if (checkIsScriptHashTx() || isWitnessTxType() || isContractTxType()) {
            return true;
        }
        Set entrySet = f12299e.entrySet();
        OPResult cpVar = new OPResult();
        Iterator it = entrySet.iterator();
        while (true) {
            if (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Vector vector = (Vector) entry.getValue();
                list.clear();
                ListIterator listIterator = this.scriptChunks.listIterator();
                ListIterator listIterator2 = vector.listIterator();
                ScriptChunk csVar = null;
                boolean z = true;
                boolean z2 = false;
                while (true) {
                    if (listIterator.hasNext() || listIterator2.hasNext()) {
                        if (!ScriptChunk.makeOPResult(listIterator, cpVar) || !listIterator2.hasNext()) {
                            break;
                        }
                        int intValue = ((Integer) listIterator2.next()).intValue();
                        if (intValue == 251) {
                            if (csVar == null) {
                                csVar = cpVar.scriptChunk;
                            }
                            while (vdsMain.Utils.m13308a(cpVar.bytes)) {
                                list.add(cpVar.bytes);
                                if (!ScriptChunk.makeOPResult(listIterator, cpVar)) {
                                    break;
                                }
                            }
                            if (!listIterator2.hasNext()) {
                                break;
                            }
                            intValue = ((Integer) listIterator2.next()).intValue();
                        }
                        if (intValue == 254) {
                            if (csVar == null) {
                                csVar = cpVar.scriptChunk;
                            }
                            if (!vdsMain.Utils.m13308a(cpVar.bytes)) {
                                if (!z) {
                                    break;
                                }
                                while (true) {
                                    if (ScriptChunk.makeOPResult(listIterator, cpVar)) {
                                        if (vdsMain.Utils.m13308a(cpVar.bytes)) {
                                            csVar = cpVar.scriptChunk;
                                            list.add(cpVar.bytes);
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                list.add(cpVar.bytes);
                            }
                        } else if (intValue == 253) {
                            if (csVar == null) {
                                csVar = cpVar.scriptChunk;
                            }
                            if (cpVar.mo43150a() != 20) {
                                break;
                            }
                            list.add(cpVar.bytes);
                        } else if (intValue == 250) {
                            if (csVar == null) {
                                csVar = cpVar.scriptChunk;
                            }
                            if (cpVar.opWord != 0 && (cpVar.opWord < 81 || cpVar.opWord > 96)) {
                                break;
                            }
                            list.add(new byte[]{(byte) decodeOP_N(cpVar.opWord)});
                        } else if (cpVar.opWord != intValue || !Arrays.equals(cpVar.bytes, null)) {
                            if (z) {
                                while (true) {
                                    if (listIterator.hasNext() && ScriptChunk.makeOPResult(listIterator, cpVar)) {
                                        if (cpVar.opWord == intValue && Arrays.equals(cpVar.bytes, null)) {
                                            csVar = cpVar.scriptChunk;
                                            z2 = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            if (!z2) {
                                break;
                            }
                        } else if (csVar == null) {
                            csVar = cpVar.scriptChunk;
                        }
                        z = false;
                    } else {
                        this.txnOutType = (TxnOutType) entry.getKey();
                        if (this.txnOutType == TxnOutType.TX_MULTISIG) {
                            byte b = ((byte[]) list.get(0))[0];
                            byte b2 = ((byte[]) list.get(list.size() - 1))[0];
                            if (b < 1 || b2 < 1 || b > b2 || list.size() - 2 != b2) {
                                this.txnOutType = TxnOutType.TX_NONSTANDARD;
                                return false;
                            }
                            for (int i = 0; i < b2 + 3; i++) {
                                this.firstChunk = (ScriptChunk) listIterator.previous();
                            }
                        }
                        this.firstChunk = csVar;
                        return true;
                    }
                }
            } else if (isMutiSignTxType(bArr)) {
                return true;
            } else {
                this.txnOutType = TxnOutType.TX_NONSTANDARD;
                return false;
            }
        }
    }

    //OP_WORDtoInt
    //m10781e
    public static int OP_WORDtoInt(int i) {
        if (i == 0) {
            return 0;
        }
        if (i < 81 || i > 96) {
            return -1;
        }
        return i - 80;
    }

    //m10780c
    private boolean isMutiSignTxType(byte[] bArr) {
        ListIterator listIterator = this.scriptChunks.listIterator();
        while (listIterator.hasNext()) {
            ScriptChunk csVar = (ScriptChunk) listIterator.next();
            if (Standard.m532a(csVar.opWord)) {
                //int e = OP_WORDtoInt(csVar.opWord);
                int e = OP_WORDtoInt(csVar.opWord);
                if (e < 1) {
                    return false;
                }
                int i = 0;
                while (listIterator.hasNext()) {
                    ScriptChunk csVar2 = (ScriptChunk) listIterator.next();
                    if (csVar2.opWord <= 0 || csVar2.opWord > 78) {
                        if (Standard.m532a(csVar2.opWord)) {
                            //int e2 = OP_WORDtoInt(csVar2.opWord);
                            int e2 = OP_WORDtoInt(csVar2.opWord);
                            //174:OP_CHECKMULTISIG
                            if (e2 < e || e2 != i || !listIterator.hasNext() || ((ScriptChunk) listIterator.next()).opWord != 174) {
                                return false;
                            }
                            this.firstChunk = csVar;
                            this.txnOutType = TxnOutType.TX_MULTISIG;
                            this.scriptSigType = ScriptSigType.VERIFY;
                            return true;
                        }
                    } else if (csVar2.getBytesLength() < 1) {
                        return false;
                    } else {
                        try {
                            if ((csVar2.isDataOutput() || csVar2.isOpWordEqual20()) && !new CPubKey(csVar2.bytes).isLengthGreaterZero()) {
                                return false;
                            }
                            i++;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    }
                }
                return false;
            } else if (!csVar.isNotValuePushingWord() && csVar.opWord == 0) {
                if (listIterator.hasNext()) {
                    while (true) {
                        if (!listIterator.hasNext()) {
                            break;
                        }
                        ScriptChunk csVar3 = (ScriptChunk) listIterator.next();
                        if (csVar3.isNotValuePushingWord()) {
                            while (listIterator != listIterator) {
                                if (!m10776b((ScriptChunk) listIterator.next())) {
                                    return false;
                                }
                            }
                            this.endScripChunk = csVar;
                            this.txnOutType = TxnOutType.TX_MULTISIG;
                            this.scriptSigType = ScriptSigType.UNLOCK;
                            return true;
                        } else if (csVar3.opWord == 0) {
                            ScriptChunk csVar4 = (ScriptChunk) listIterator.previous();
                            break;
                        } else if (csVar3.opWord > 0 && csVar3.opWord <= 78 && m10770a(csVar3, new MultipleSignatureScriptInfo(), true)) {
                            this.txnOutType = TxnOutType.TX_MULTISIG;
                            this.scriptSigType = ScriptSigType.VERIFY;
                            this.endScripChunk = csVar;
                            this.firstChunk = csVar3;
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    //mo43178s
    public int calScriptLength() {
        return getScriptLength();
    }

    //mo43181u
    public boolean isOP_RETURN() {
        return (calScriptLength() > 0 && !this.scriptChunks.isEmpty() && ((ScriptChunk) this.scriptChunks.get(0)).opWord == 106) || calScriptLength() > 10000;
    }

    //m10774b
    static int decodeOP_N(int i) {
        Preconditions.checkArgument(i == 0 || i == 79 || (i >= 81 && i <= 96), "decodeFromOpN called on non OP_N opcode");
        if (i == 0) {
            return 0;
        }
        if (i == 79) {
            return -1;
        }
        return (i + 1) - 81;
    }
}
