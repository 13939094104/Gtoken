package vdsMain.model;

import androidx.annotation.NonNull;
import bitcoin.script.WitnessV0ScriptHash;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.transaction.CScriptID;
import vdsMain.wallet.CWallet;
import vdsMain.wallet.VCWallet;
import vdsMain.wallet.VWallet;
import vdsMain.wallet.Wallet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

//bpg
public class VAddressModel extends AddressModel {

    public VAddressModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    //f12018a
    private LinkedHashMap<CTxDestination, ComplexBitcoinAddress> cTxDesComplexBitCoinAddressLinkedMap = new LinkedHashMap<>();

    public void ClearAll() {
        super.ClearAll();
        this.cTxDesComplexBitCoinAddressLinkedMap.clear();
    }

    //m10070l
    public static boolean isVid(Address address) {
        if (address.isUnlessAddress()) {
            return false;
        }
        return address.isFlagEqual(16);
    }

    public void addAddressToUsingTxDesMap(Address address) {
        super.addAddressToUsingTxDesMap(address);
        if (address.getAddressType() == AddressType.ANONYMOUS) {
            ((VCCryptoKeyStore) ((VCWallet) this.wallet.getSelfCWallet()).getCKeyStore()).mo42930a(address);
        }
        m10071n(address);
        if (isVid(address)) {
            ((VWallet) this.wallet).mo42394ad().mo42684a(address);
        }
    }

    //mo42669a
    public CScriptID getComplexBitcoinAddressCScrptID(Address address, List<CTxDestination> list) {
        CTxDestination cTxDestination = address.getCTxDestination();
        ComplexBitcoinAddress complexBitcoinAddress = AddressUtils.m921a(this.wallet, address);
        if (complexBitcoinAddress == null) {
            return null;
        }
        if (!complexBitcoinAddress.mo42495e() && (address instanceof BitcoinMultiSigAddress)) {
            if (list != null) {
                list.add(cTxDestination);
            }
            BitcoinMultiSigAddress alVar = (BitcoinMultiSigAddress) address;
            if (alVar.mo40878h() > 0) {
                complexBitcoinAddress.mo42490a((CTxDestination) new WitnessV0ScriptHash(alVar.mo40879i()));
            }
        }
        if (list != null) {
            list.clear();
            list.addAll(complexBitcoinAddress.mo42491b());
        }
        return complexBitcoinAddress.mo42488a();
    }

    private void m10071n(Address address) {
        Vector vector = new Vector(2);
        CScriptID cScriptID = getComplexBitcoinAddressCScrptID(address, vector);
        synchronized (this.cTxDesComplexBitCoinAddressLinkedMap) {
            if (cScriptID != null) {
                ComplexBitcoinAddress complexBitcoinAddress = (ComplexBitcoinAddress) this.cTxDesComplexBitCoinAddressLinkedMap.get(cScriptID);
                if (complexBitcoinAddress == null) {
                    this.cTxDesComplexBitCoinAddressLinkedMap.put(cScriptID, new ComplexBitcoinAddress(this.wallet, cScriptID, vector));
                } else {
                    complexBitcoinAddress.mo42489a((List<CTxDestination>) vector);
                }
            }
        }
    }

    private void m10072o(Address address) {
        Vector vector = new Vector(2);
        CScriptID a = getComplexBitcoinAddressCScrptID(address, vector);
        if (a != null) {
            synchronized (this.cTxDesComplexBitCoinAddressLinkedMap) {
                ComplexBitcoinAddress bja = (ComplexBitcoinAddress) this.cTxDesComplexBitCoinAddressLinkedMap.get(a);
                if (bja != null) {
                    bja.mo42492b(vector);
                    if (!bja.mo42495e() && a.equals(address.getCTxDestination())) {
                        this.cTxDesComplexBitCoinAddressLinkedMap.remove(a);
                    }
                }
            }
        }
    }

    public ComplexBitcoinAddress mo42675m(Address address) {
        ComplexBitcoinAddress complexBitcoinAddress;
        synchronized (this.cTxDesComplexBitCoinAddressLinkedMap) {
            CTxDestination u = address.getCTxDestination();
            complexBitcoinAddress = (ComplexBitcoinAddress) this.cTxDesComplexBitCoinAddressLinkedMap.get(u);
            if (complexBitcoinAddress == null) {
                if (address == null) {
                    address = getAddressFromUnuseAddressMap(u);
                }
                if (address != null) {
                    CScriptID a = getComplexBitcoinAddressCScrptID(address, null);
                    if (a != null) {
                        complexBitcoinAddress = (ComplexBitcoinAddress) this.cTxDesComplexBitCoinAddressLinkedMap.get(a);
                    }
                }
            }
        }
        return complexBitcoinAddress;
    }

    //mo42671b
    public void addAllCTxDesToCAddressMapTolist(List<ComplexBitcoinAddress> list) {
        list.clear();
        synchronized (this.cTxDesComplexBitCoinAddressLinkedMap) {
            list.addAll(this.cTxDesComplexBitCoinAddressLinkedMap.values());
        }
    }

    public void mo42673g(Address address) {
        super.mo42673g(address);
        if (address.getAddressType() == AddressType.ANONYMOUS) {
            ((VCCryptoKeyStore) ((CWallet) this.wallet.getSelfCWallet()).getCKeyStore()).mo42933b(address);
        }
        m10072o(address);
        if (isVid(address)) {
            ((VWallet) this.wallet).mo42394ad().mo42689b(address);
        }
    }

    //mo42670b
    public void initAddressSparseArr() {
        super.initAddressSparseArr();
        addAddressToSparseArrByFlag(2);
        addAddressToSparseArrByFlag(16);
        addAddressToSparseArrByFlag(512);
    }
}
