package vdsMain.transaction;

import bitcoin.CKeyStore;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import vdsMain.BytesArrayBuffer;
import vdsMain.CKeyID;

//bpp
public abstract class VBaseSignatureCreator {

    //f12036a
    protected CKeyStore cKeyStore;

    //mo42750a
    public abstract boolean CreateSig(CharSequence pwd, BytesArrayBuffer bytesArrayBuffer, CKeyID cKeyID, CScript cScript, SigVersion sigVersion);

    //mo42751b
    public abstract BaseSignatureChecker getSignatureChecker();

    public VBaseSignatureCreator(CKeyStore cKeyStore) {
        this.cKeyStore = cKeyStore;
    }

    //mo42749a
    public CKeyStore getCKeyStore() {
        return this.cKeyStore;
    }
}
