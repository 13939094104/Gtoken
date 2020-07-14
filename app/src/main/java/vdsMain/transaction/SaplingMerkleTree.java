package vdsMain.transaction;

//bsb
public class SaplingMerkleTree extends IncrementalMerkleTree<PedersenHash> {
    public SaplingMerkleTree() {
        super(32, MerkleUint256.m10387b());
    }

    /* renamed from: f */
    public IncrementalWitness<PedersenHash> mo43005f() {
        return new SaplingWitness(this, MerkleUint256.m10387b());
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public IncrementalMerkleTree mo42901d() {
        return new SaplingMerkleTree();
    }
}