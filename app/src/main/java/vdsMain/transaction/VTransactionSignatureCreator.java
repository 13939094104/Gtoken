package vdsMain.transaction;

import bitcoin.CKeyStore;
import bitcoin.UInt256;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import vcash.script.VInterpreter;
import vcash.script.VTransactionSignatureChecker;
import vdsMain.*;

//bpt
public class VTransactionSignatureCreator extends VBaseSignatureCreator {

    //f12041b
    protected VTransactionInterface vTransactionInterface;

    //f12042c
    protected int txInIndex;

    //f12043d
    protected int hashType;

    //f12044e
    private VTransactionSignatureChecker vTransactionSignatureChecker;

    public VTransactionSignatureCreator(CKeyStore cKeyStore, VTransactionInterface vTransactionInterface, int i, int... nHashType) {
        super(cKeyStore);
        this.vTransactionInterface = vTransactionInterface;
        this.txInIndex = i;
        if (nHashType.length == 0) {
            this.hashType = 1;
        } else {
            this.hashType = nHashType[0];
        }
        this.vTransactionSignatureChecker = new VTransactionSignatureChecker(vTransactionInterface, this.txInIndex);
    }

    /* renamed from: b */
    public BaseSignatureChecker getSignatureChecker() {
        return this.vTransactionSignatureChecker;
    }

    //mo42750a
    //TODO 私钥签名
    public boolean CreateSig(CharSequence pwd, BytesArrayBuffer bytesArrayBuffer, CKeyID address, CScript scriptCode, SigVersion sigVersion) {
        CPrivateKeyInterface privateKey = this.cKeyStore.getPrivateKey((CTxDestination) address, pwd);
        if (privateKey == null) {
            return false;
        }
        if (sigVersion == SigVersion.WITNESS_V0 && !privateKey.IsCompressed()) {
            return false;
        }
        UInt256 hash;
        try {
            hash=new VInterpreter().SignatureHash(scriptCode, this.vTransactionInterface, (long) this.txInIndex, this.hashType, sigVersion, (VPrecomputedTransactionData) null);
            byte[] signuatureBytes = privateKey.signNativeByTransDataDefault(hash);
            if (signuatureBytes == null) {
                return false;
            }
            bytesArrayBuffer.writeAllBytes(signuatureBytes);
            bytesArrayBuffer.writeOnByte((byte) this.hashType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

