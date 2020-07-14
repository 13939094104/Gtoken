package vdsMain.transaction;

import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vdsMain.model.Address;
//bjx
public class SerializedSaplingNoteMap extends SeriableData {

    //f11816a
    public HashMap<Integer, SerializedSaplingNoteItem> indexToSaplingNoteItemMap = new HashMap<>();

    //mo42528a
    public void addToIndexToSaplingNoteItemMap(List<SaplingUtxoValue> list, MapSaplingNoteDataT mapSaplingNoteDataT) {
        this.indexToSaplingNoteItemMap.clear();
        if (mapSaplingNoteDataT != null && !mapSaplingNoteDataT.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (SaplingUtxoValue saplingUtxoValue : list) {
                hashMap.put(saplingUtxoValue.saplingOutpoint, saplingUtxoValue);
            }
            for (Map.Entry entry : mapSaplingNoteDataT.entrySet()) {
                SaplingOutpoint saplingOutpoint = (SaplingOutpoint) entry.getKey();
                SaplingNoteData saplingNoteData = (SaplingNoteData) entry.getValue();
                SerializedSaplingNoteItem saplingNoteItem = new SerializedSaplingNoteItem();
                SaplingUtxoValue saplingUtxoValue = (SaplingUtxoValue) hashMap.get(saplingOutpoint);
                saplingNoteItem.value = saplingUtxoValue.value;
                saplingNoteItem.cTxDestination = saplingUtxoValue.cTxDestination;
                saplingNoteItem.saplingNoteData = saplingNoteData;
                this.indexToSaplingNoteItemMap.put(Integer.valueOf(((SaplingOutpoint) entry.getKey()).index), saplingNoteItem);
            }
        }
    }

    /* renamed from: a */
    public void mo42527a(UInt256 uInt256, Wallet izVar, List<SaplingUtxoValue> list, MapSaplingNoteDataT brr) {
        brr.clear();
        if (!this.indexToSaplingNoteItemMap.isEmpty()) {
            for (Map.Entry entry : this.indexToSaplingNoteItemMap.entrySet()) {
                SerializedSaplingNoteItem bjw = (SerializedSaplingNoteItem) entry.getValue();
                SaplingUtxoValue bpn = new SaplingUtxoValue();
                bpn.saplingOutpoint = new SaplingOutpoint(uInt256, ((Integer) entry.getKey()).intValue());
                bpn.value = bjw.value;
                Address b = izVar.getAddressByCTxDestinationFromArrayMap(bjw.cTxDestination);
                if (b != null) {
                    bpn.cTxDestination = b.getCTxDestination();
                    list.add(bpn);
                    brr.put(bpn.saplingOutpoint, bjw.saplingNoteData);
                }
            }
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableInt((long) this.indexToSaplingNoteItemMap.size());
        if (!this.indexToSaplingNoteItemMap.isEmpty()) {
            for (Map.Entry entry : this.indexToSaplingNoteItemMap.entrySet()) {
                writeVariableInt((long) ((Integer) entry.getKey()).intValue());
                ((SerializedSaplingNoteItem) entry.getValue()).serialToStream(streamWriter);
            }
        }
    }

    public void onDecodeSerialData() {
        this.indexToSaplingNoteItemMap.clear();
        int b = readVariableInt().getIntValue();
        if (b >= 1) {
            while (b > 0) {
                int b2 = readVariableInt().getIntValue();
                SerializedSaplingNoteItem bjw = new SerializedSaplingNoteItem();
                bjw.decodeSerialStream((SeriableData) this);
                this.indexToSaplingNoteItemMap.put(Integer.valueOf(b2), bjw);
                b--;
            }
        }
    }
}
