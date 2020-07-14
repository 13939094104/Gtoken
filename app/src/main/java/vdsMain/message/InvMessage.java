package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.peer.Inv;
import vdsMain.peer.InvMessageInterface;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//blw
public class InvMessage extends VMessage implements InvMessageInterface {

    //f11894a
    private List<Inv> invList = new ArrayList();

    public InvMessage(@NonNull Wallet izVar) {
        super(izVar, "inv");
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        int b = readVariableInt().getIntValue();
        this.invList.clear();
        for (int i = 0; i < b; i++) {
            Inv inv = new Inv();
            inv.decodeSerialStream(getTempInput());
            this.invList.add(inv);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableInt((long) this.invList.size());
        for (Inv serialToStream : this.invList) {
            serialToStream.serialToStream(streamWriter);
        }
    }

    //mo42576a
    public void addInv(Inv inv) {
        this.invList.add(inv);
    }

    //mo42575a
    public List<Inv> getInvList() {
        return this.invList;
    }

    public void clean() {
        this.invList.clear();
    }

    /* renamed from: b */
    public boolean mo42577b() {
        return this.invList.isEmpty();
    }
}
