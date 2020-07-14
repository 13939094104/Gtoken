package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import vdsMain.peer.GetDataMessageInterface;
import vdsMain.peer.Inv;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//blh
public class GetDataMessage extends VMessage implements GetDataMessageInterface {

    //f11884a
    private Vector<Inv> invVector = new Vector<>();

    public GetDataMessage(@NonNull Wallet izVar) {
        super(izVar, "getdata");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        long c = readVariableInt().getValue();
        this.invVector.clear();
        this.invVector.setSize((int) c);
        for (long j = 0; j < c; j++) {
            Inv yVar = new Inv();
            yVar.decodeSerialStream(getTempInput());
            this.invVector.add(yVar);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableInt((long) this.invVector.size());
        Iterator it = this.invVector.iterator();
        while (it.hasNext()) {
            ((Inv) it.next()).serialToStream(streamWriter);
        }
    }

    //mo42563a
    public List<Inv> getInvVector() {
        return this.invVector;
    }

    //mo42566b
    public int getInvVectorSize() {
        return this.invVector.size();
    }

    public void clean() {
        this.invVector.clear();
    }

    //mo42565a
    public void addInvToList(Inv inv) {
        this.invVector.add(inv);
    }

    //mo42564a
    public void addToInvVector(List<Inv> list) {
        this.invVector.addAll(list);
    }

    //m9843a
    public static GetDataMessage getNewGetDataMessageByHashCollection(@NonNull Wallet wallet, Collection<UInt256> collection, long type) {
        GetDataMessage getDataMessage = new GetDataMessage(wallet);
        if (collection != null) {
            for (UInt256 blockHash : collection) {
                getDataMessage.addInvToList(new Inv(type, blockHash));
            }
        }
        return getDataMessage;
    }

    //910 m9960a
    //m9844a
    public static GetDataMessage getNewGetDataMessage(@NonNull Wallet wallet, List<Inv> list) {
        GetDataMessage getDataMessage = new GetDataMessage(wallet);
        getDataMessage.addToInvVector(list);
        return getDataMessage;
    }
}
