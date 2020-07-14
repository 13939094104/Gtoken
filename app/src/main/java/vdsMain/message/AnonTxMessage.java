package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.transaction.SaplingMerkleTree;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.VTransaction;
import vdsMain.wallet.Wallet;

import java.util.Vector;

//910  blb
public class AnonTxMessage extends VMessage {

    /* renamed from: a */
    private Vector<C3828a> f11971a;

    /* renamed from: blb$a */
    /* compiled from: AnonTxMessage */
    public class C3828a {

        /* renamed from: a */
        public UInt256 f11972a = new UInt256();

        /* renamed from: b */
        public SaplingMerkleTree f11973b;

        /* renamed from: c */
        public Vector<Transaction> f11974c;

        public C3828a() {
        }

        /* access modifiers changed from: 0000 */
        /* renamed from: a */
        public void mo42604a(AnonTxMessage blb) {
            this.f11972a.decodeSerialStream((SeriableData) blb);
            if (blb.readByte() == 1) {
                this.f11973b = new SaplingMerkleTree();
                this.f11973b.decodeSerialStream((SeriableData) blb);
            }
            int b = blb.readVariableInt().getIntValue();
            if (b > 0) {
                this.f11974c = new Vector<>(b);
                while (b > 0) {
                    VTransaction bqu = new VTransaction(AnonTxMessage.this.wallet);
                    bqu.mo44658c((SeriableData) blb);
                    this.f11974c.add(bqu);
                    b--;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) {
    }

    public AnonTxMessage(@NonNull Wallet izVar) {
        super(izVar, "anonytx");
    }

    public void onDecodeSerialData() {
        int b = readVariableInt().getIntValue();
        this.f11971a = new Vector<>(b);
        while (b > 0) {
            C3828a aVar = new C3828a();
            aVar.mo42604a(this);
            this.f11971a.add(aVar);
            b--;
        }
    }

    /* renamed from: a */
    public Vector<C3828a> mo42603a() {
        return this.f11971a;
    }
}
