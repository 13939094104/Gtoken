package vdsMain;

import bitcoin.CNoDestination;
import vdsMain.transaction.CScriptID;
import vdsMain.wallet.ChainParams;

public class CBitcoinAddress extends CBase58Data {
    /* renamed from: a */
    public boolean mo43449a(CNoDestination cNoDestination, ChainParams uVar) {
        return false;
    }

    public CBitcoinAddress() {
    }

    public CBitcoinAddress(CTxDestination oVar, ChainParams uVar) {
        if (oVar instanceof CKeyID) {
            mo43451a((CKeyID) oVar, uVar);
        } else if (oVar instanceof CScriptID) {
            mo43450a((CScriptID) oVar, uVar);
        } else if (oVar instanceof CNoDestination) {
            mo43449a((CNoDestination) oVar, uVar);
        }
    }

    /* renamed from: a */
    public boolean mo43451a(CKeyID lVar, ChainParams chainParams) {
        mo42885a(chainParams.mo43957a(Base58Type.PUBKEY_ADDRESS), lVar.data(), 20);
        return true;
    }

    /* renamed from: a */
    public boolean mo43450a(CScriptID ckVar, ChainParams uVar) {
        mo42885a(uVar.mo43957a(Base58Type.SCRIPT_ADDRESS), ckVar.data(), 20);
        return true;
    }
}