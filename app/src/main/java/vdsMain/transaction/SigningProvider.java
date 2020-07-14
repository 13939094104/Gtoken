package vdsMain.transaction;

import bitcoin.script.CScript;
import vdsMain.CPrivateKeyInterface;
import vdsMain.CPubkeyInterface;
import vdsMain.CTxDestination;

public interface SigningProvider {
    /* renamed from: a */
    CPrivateKeyInterface mo42761a(CTxDestination oVar, CharSequence charSequence);

    /* renamed from: a */
    CPubkeyInterface mo42762a(CTxDestination oVar);

    /* renamed from: a */
    boolean mo42778a(CScriptID ckVar, CScript cScript);
}
