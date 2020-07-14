package vdsMain.transaction;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.ByteBuffer;
import vdsMain.DumpedStreamWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

//bsh
public class SaplingWitness extends IncrementalWitness<PedersenHash> {

    //f12224g
    private int witnessHeight;

    public SaplingWitness() {
        super(32, MerkleUint256.m10387b());
    }

    public SaplingWitness(SaplingWitness bsh) {
        super((IncrementalWitness<PedersenHash>) bsh, MerkleUint256.m10387b());
        this.witnessHeight = bsh.witnessHeight;
    }

    protected SaplingWitness(IncrementalMerkleTree<PedersenHash> bqv, @NonNull MerkleUint256<PedersenHash> bqy) {
        super(32, bqv, bqy);
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public IncrementalWitness mo42911e() {
        return new SaplingWitness();
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public IncrementalMerkleTree mo42910d() {
        return new SaplingMerkleTree();
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public Class mo42913f() {
        return SaplingMerkleTree.class;
    }

    //mo43027a
    public void setWitnessHeight(int i) {
        this.witnessHeight = i;
    }

    /* renamed from: h */
    public int mo43029h() {
        return this.witnessHeight;
    }

    /* renamed from: i */
    public byte[] mo43030i() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            serialToStream((StreamWriter) new DumpedStreamWriter(byteArrayOutputStream));
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public boolean mo43028a(byte[] bArr) {
        try {
            decodeSerialStream(new ByteBuffer(bArr));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        super.writeSerialData(streamWriter);
    }

    public void onDecodeSerialData() {
        super.onDecodeSerialData();
    }
}
