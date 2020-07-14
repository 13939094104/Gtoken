package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.CNoDestination;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

//bkv
public class CluePreCreateTreeMssage extends VMessage {

    /* renamed from: a */
    public CTxDestination f11861a = new CNoDestination();

    /* renamed from: b */
    public CTxDestination f11862b = new CNoDestination();

    //f11863h
    public CluePreCreateCode cluePreCreateCode = CluePreCreateCode.ERR_UNKNOWN;

    //f11864i
    public Vector<ClueParent> clueParentVector = new Vector<>();

    /* renamed from: bkv$a */
    //C3814a
    public class ClueParent {

        //f11865a
        public CTxDestination des;

        //f11866b
        public long directClueCount;

        public ClueParent() {
        }
    }

    /* renamed from: bkv$b */
    //C3815b
    public enum CluePreCreateCode {
        SUCCESS(0),
        ERR_ALREADY_CLUE(1),
        ERR_PARENT_NOT_CLUE(2),
        ERR_NO_VALID_PARENT(3),
        ERR_INVALID_ADDR(4),
        ERR_UNKNOWN(99);


        /* renamed from: g */
        private int f11875g;

        private CluePreCreateCode(int i) {
            this.f11875g = i;
        }

        /* renamed from: a */
        public int mo42561a() {
            return this.f11875g;
        }

        /* renamed from: a */
        public static CluePreCreateCode m9831a(int i) {
            switch (i) {
                case 0:
                    return SUCCESS;
                case 1:
                    return ERR_ALREADY_CLUE;
                case 2:
                    return ERR_PARENT_NOT_CLUE;
                case 3:
                    return ERR_NO_VALID_PARENT;
                case 4:
                    return ERR_INVALID_ADDR;
                default:
                    return ERR_UNKNOWN;
            }
        }
    }

    public CluePreCreateTreeMssage(@NonNull Wallet izVar) {
        super(izVar, "clprecreate");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        this.f11861a.writeTypeAndData(streamWriter);
        this.f11862b.writeTypeAndData(streamWriter);
        writeVariableInt((long) this.cluePreCreateCode.mo42561a());
        if (this.cluePreCreateCode == CluePreCreateCode.SUCCESS) {
            streamWriter.writeVariableInt((long) this.clueParentVector.size());
            Iterator it = this.clueParentVector.iterator();
            while (it.hasNext()) {
                ClueParent aVar = (ClueParent) it.next();
                aVar.des.writeTypeAndData(streamWriter);
                streamWriter.writeVariableInt(aVar.directClueCount);
            }
        }
    }

    public void onDecodeSerialData() throws IOException {
        this.f11861a = CTxDestinationFactory.m910a((SeriableData) this);
        this.f11862b = CTxDestinationFactory.m910a((SeriableData) this);
        this.cluePreCreateCode = CluePreCreateCode.m9831a(readVariableInt().getIntValue());
        this.clueParentVector.clear();
        if (this.cluePreCreateCode == CluePreCreateCode.SUCCESS) {
            long c = readVariableInt().getValue();
            for (int i = 0; ((long) i) < c; i++) {
                ClueParent aVar = new ClueParent();
                aVar.des = CTxDestinationFactory.m910a((SeriableData) this);
                aVar.directClueCount = readVariableInt().getValue();
                this.clueParentVector.add(aVar);
            }
        }
    }
}