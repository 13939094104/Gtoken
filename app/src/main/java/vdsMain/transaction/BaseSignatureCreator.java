package vdsMain.transaction;

import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import vdsMain.BytesArrayBuffer;
import vdsMain.CKeyID;


public interface BaseSignatureCreator {
    /* renamed from: a */
    BaseSignatureChecker mo43139a();

    /* renamed from: a */
    boolean mo43140a(SigningProvider daVar, CharSequence charSequence, BytesArrayBuffer gdVar, CKeyID lVar, CScript cScript, SigVersion cxVar);
}
