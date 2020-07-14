package vdsMain;

import bitcoin.script.CScript;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import vdsMain.transaction.CScriptID;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TxDestinationShadowMap {

    //f13124a
    private HashMap<CTxDestination, CTxDestination> cTxDesHashMap = new HashMap<>();

    //f13125b
    private Multimap<CTxDestination, CTxDestination> cTxDesMutimap = HashMultimap.create();

    //mo44266a
    public synchronized boolean addCScriptToMap(CScript cScript) {
        if (cScript.mo9540f()) {
            CScriptID cScriptID = new CScriptID(cScript);
            WitnessV0ScriptHash witnessV0ScriptHash = new WitnessV0ScriptHash(cScript);
            this.cTxDesHashMap.put(witnessV0ScriptHash, cScriptID);
            this.cTxDesMutimap.put(cScriptID, witnessV0ScriptHash);
            CScriptID witnessSrciptID = witnessV0ScriptHash.mo9570d();
            this.cTxDesHashMap.put(witnessSrciptID, cScriptID);
            this.cTxDesMutimap.put(cScriptID, witnessSrciptID);
        }
        return false;
    }

    //mo44265a
    public synchronized void addCKeyIDToMap(CKeyID cKeyID) {
        WitnessV0KeyHash witnessV0KeyHash = new WitnessV0KeyHash((CTxDestination) cKeyID);
        CScriptID cScriptID = witnessV0KeyHash.mo9569d();
        if (!this.cTxDesMutimap.containsKey(cKeyID)) {
            this.cTxDesHashMap.put(witnessV0KeyHash, cKeyID);
            this.cTxDesHashMap.put(cScriptID, cKeyID);
            this.cTxDesMutimap.put(cKeyID, witnessV0KeyHash);
            this.cTxDesMutimap.put(cKeyID, cScriptID);
        }
    }

    //mo44263a
    public synchronized Collection<CTxDestination> removeAllByCTxDestination(CTxDestination cTxDestination) {
        Collection<CTxDestination> removeAll;
        removeAll = this.cTxDesMutimap.removeAll(cTxDestination);
        if (removeAll != null && !removeAll.isEmpty()) {
            for (CTxDestination remove : removeAll) {
                this.cTxDesHashMap.remove(remove);
            }
        }
        return removeAll;
    }

    //mo44267b
    public synchronized CTxDestination removeAndGetValueFromHashMap(CTxDestination cTxDestination) {
        CTxDestination cTxDestination1;
        cTxDestination1 = (CTxDestination) this.cTxDesHashMap.remove(cTxDestination);
        if (cTxDestination1 != null) {
            this.cTxDesMutimap.remove(cTxDestination1, cTxDestination);
        }
        return cTxDestination1;
    }

    //mo44270c
    public CTxDestination getCTxDesHashMapValue(CTxDestination cTxDestination) {
        return (CTxDestination) this.cTxDesHashMap.get(cTxDestination);
    }

    //mo44271d
    public boolean cTxDesMutimapContainKey(CTxDestination oVar) {
        return this.cTxDesMutimap.containsKey(oVar);
    }

    //mo44272e
    public boolean cTxDesHashMapContainKey(CTxDestination oVar) {
        return this.cTxDesHashMap.containsKey(oVar);
    }

    //mo44262a
    public Collection<CTxDestination> getMutiMapKeyCollection() {
        return this.cTxDesMutimap.keySet();
    }

    /* renamed from: b */
    public synchronized void mo44268b() {
        this.cTxDesHashMap.clear();
        this.cTxDesMutimap.clear();
    }

    //mo44269c
    public int getTxDesHashMapSize() {
        return this.cTxDesHashMap.size();
    }

    /* renamed from: a */
    public void mo44264a(LinkedHashMap<CTxDestination, Boolean> linkedHashMap) {
        HashMap<CTxDestination, CTxDestination> hashMap = new HashMap(this.cTxDesHashMap);
        for (Map.Entry entry : this.cTxDesMutimap.entries()) {
            CTxDestination keyCTx = (CTxDestination) entry.getKey();
            CTxDestination valueCTx = (CTxDestination) entry.getValue();
            hashMap.remove(valueCTx);
            if (!linkedHashMap.containsValue(keyCTx)) {
                linkedHashMap.put(keyCTx, Boolean.valueOf(true));
            }
            if (!linkedHashMap.containsValue(valueCTx)) {
                linkedHashMap.put(valueCTx, Boolean.valueOf(true));
            }
        }
        for (Map.Entry<CTxDestination, CTxDestination> entry2 : hashMap.entrySet()) {
            if (!linkedHashMap.containsValue(entry2.getKey())) {
                linkedHashMap.put(entry2.getKey(), Boolean.valueOf(true));
            }
            if (!linkedHashMap.containsValue(entry2.getValue())) {
                linkedHashMap.put(entry2.getValue(), Boolean.valueOf(true));
            }
        }
    }
}