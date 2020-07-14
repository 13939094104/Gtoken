package bitcoin;

import androidx.annotation.NonNull;
import bitcoin.script.CScript;
import generic.crypto.KeyCryptor;
import vdsMain.CPrivateKeyInterface;
import vdsMain.CPubkeyInterface;
import vdsMain.CTxDestination;
import vdsMain.transaction.CScriptID;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;

public class CKeyStore {

    //f396a
    protected Wallet wallet;

    //f397b
    protected KeyCryptor keyCryptor;

    /* renamed from: c */
    protected Object f398c = new Object();

    //f399d
    private long nativePointer;

    private native void addScript(long j, byte[] bArr, byte[] bArr2);

    private native long createNativePointer();

    private native void destroyNativePointer(long j);

    private native byte[] getScript(long j, byte[] bArr);

    private native void nativeClear(long j);

    private native void removeScript(long j, byte[] bArr);


    public CKeyStore(@NonNull Wallet wallet, @NonNull KeyCryptor keyCryptor) {
        this.wallet = wallet;
        this.keyCryptor = keyCryptor;
        this.nativePointer = createNativePointer();
    }

    public void finalize() {
        destroyNativePointer(this.nativePointer);
        this.nativePointer = 0;
    }

    //mo9415a
    public CPrivateKeyInterface getPrivateKey(CTxDestination cTxDestination, CharSequence pwd) {
        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(cTxDestination);
        if (address == null || !address.privateKeyIsNotEmpty()) {
            return null;
        }
        try {
            return address.getPrivateKeyClone().getNewCKeyWithKeyCryptor(this.keyCryptor, pwd.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //mo9419a
    public void addScriptToKeyStore(CScriptID cScriptID, CScript cScript) {
        addScript(this.nativePointer, cScriptID.data(), cScript.copyToNewBytes());
    }

    //mo9416a
    public CPubkeyInterface getPubKey(CTxDestination cTxDestination) {
        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(cTxDestination);
        if (address == null) {
            return null;
        }
        return address.getSelfPubKey();
    }

    //mo9420b
    public boolean getScriptAndWriteToCSript(CScriptID cScriptID, CScript cScript) {
        byte[] script = getScript(this.nativePointer, cScriptID.data());
        if (script == null) {
            return false;
        }
        cScript.clean();
        cScript.writeAllBytes(script);
        return true;
    }

    public void mo9418a(CScriptID ckVar) {
        removeScript(this.nativePointer, ckVar.data());
    }

    //mo9417a
    public void clearNative() {
        nativeClear(this.nativePointer);
    }
}
