package generic.utils;

import androidx.annotation.NonNull;
import bitcoin.CKey;
import bitcoin.CNoDestination;
import bitcoin.CPubKey;
import bitcoin.UInt160;
import bitcoin.script.CScript;
import bitcoin.script.WitnessUnknown;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import com.vc.libcommon.exception.AddressFormatException;
import generic.crypto.Base58;
import generic.keyid.CTxDestinationFactory;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.transaction.CScriptID;
import vdsMain.transaction.Script;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.util.ArrayList;
import java.util.Locale;

public class AddressUtils {

    private static native byte[] decodeSaplingAddrText(String str, String str2, int i, boolean z);

    private static native String saplingAddrNative(byte[] bArr, String str);

    private static native String witnessAddress(String str, byte[] bArr, int i);

    private static native String witnessUnknownAddress(String str, int i, byte[] bArr, int i2);

    //m930a
    //915 m933a
    public static String getAddressString(CTxDestination txDestination, ChainParams chainParams) {
        byte[] bArr;
        CTxDestinationType a = txDestination.getCTxDestinationType();
        if (a == CTxDestinationType.KEYID) {
            bArr = chainParams.getBase58Prefixes_public_address();
        } else if (a == CTxDestinationType.SCRIPTID) {
            bArr = chainParams.getBase58Prefixes_script_address();
        }
//        else if (a == CTxDestinationType.ANONYMOUST_KEY) {
//            return m924a((bsf) txDestination, (biq) chainParams);
//        }
        else {
            if (a == CTxDestinationType.CONTRACT) {
                return StringToolkit.bytesToString(txDestination.data());
            }
//            if (a == CTxDestinationType.WITNESS_V0_KEY_HASH || a == CTxDestinationType.WITNESS_V0_SCRIPT_HASH || a == CTxDestinationType.WITNESS_UNKNOWN) {
//                return m950b(txDestination, chainParams);
//            }
            bArr = null;
        }
        if (bArr == null) {
            return "";
        }
        return Base58.encodeChecked(DataTypeToolkit.mergeArr(bArr, txDestination.data()));
    }

    public static boolean m944a(CTxDestination des, boolean... zArr) {
        boolean z = true;
        if (zArr.length == 0 || zArr[0]) {
            if (des == null || (des instanceof CNoDestination) || (des instanceof WitnessUnknown)) {
                z = false;
            }
            return z;
        }
        if (des == null || (des instanceof CNoDestination) || (des instanceof WitnessUnknown) || (des instanceof SaplingPaymentAddress)) {
            z = false;
        }
        return z;
    }

    public static String m925a(SaplingPaymentAddress bsf, ChainParams uVar) throws AddressFormatException {
        if (uVar instanceof VChainParam) {
            try {
                return saplingAddrNative(bsf.serialToStream(), ((VChainParam) uVar).mo42378a(0));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new AddressFormatException("Chain param must be VChainParam or it's children.");
        }
    }

    public static boolean m955c(CharSequence charSequence, ChainParams chainParams) {
        return m944a(m938a(charSequence.toString(), chainParams));
    }

    public static String m926a(SaplingPubKey bsg, ChainParams chainParams) throws AddressFormatException {
        if (chainParams instanceof VChainParam) {
            return m925a(bsg.mo43026i(), chainParams);
        }
        throw new AddressFormatException("Chain param must be VChainParam or it's children.");
    }

    public static final String m929a(CPubkeyInterface pubKey, ChainParams chainParams) throws AddressFormatException {
        byte[] bArr;
        PubkeyType pubkeyType = pubKey.mo210g();
        if (pubkeyType == PubkeyType.WITNESS_V0_KEY_HASH || pubkeyType == PubkeyType.WITNESS_SCRIPT_HASH) {
            return getAddressString(pubKey.getCKeyID(), chainParams);
        }
        if (pubkeyType == PubkeyType.CONTRACT) {
            return StringToolkit.bytesToString(pubKey.mo9455n());
        }
        if (pubkeyType == PubkeyType.MULTISIG) {
            bArr = DataTypeToolkit.mergeArr(chainParams.getBase58Prefixes_script_address(), pubKey.mo9455n());
        } else if (pubkeyType == PubkeyType.PUBKEY) {
            bArr = DataTypeToolkit.mergeArr(chainParams.getBase58Prefixes_public_address(), Utils.sha256hash160(pubKey.getByteArr()));
        } else if (pubkeyType == PubkeyType.ANONYMOUS) {
            return m926a((SaplingPubKey) pubKey, chainParams);
        } else {
            bArr = null;
        }
        return Base58.encodeChecked(bArr);
    }

    public static ComplexBitcoinAddress m921a(Wallet izVar, Address address) {
        CScriptID ckVar;
        CTxDestination cTxDestination = address.getCTxDestination();
        if (cTxDestination instanceof SaplingPaymentAddress) {
            return null;
        }
        ArrayList arrayList = new ArrayList(2);
        CPubkeyInterface t = address.getSelfPubKey();
        boolean z = t != null && t.checkLength();
        if (cTxDestination instanceof CKeyID) {
            arrayList.add(cTxDestination);
            WitnessV0KeyHash witnessV0KeyHash = new WitnessV0KeyHash(cTxDestination);
            if (z) {
                arrayList.add(witnessV0KeyHash);
            }
            ckVar = new CScriptID(Script.m10760a(witnessV0KeyHash));
        } else if (cTxDestination instanceof WitnessV0KeyHash) {
            if (!z) {
                return null;
            }
            arrayList.add(new CKeyID((UInt160) (WitnessV0KeyHash) cTxDestination));
            arrayList.add(cTxDestination);
            ckVar = new CScriptID(Script.getLockedSignScript(cTxDestination));
        } else if (cTxDestination instanceof WitnessV0ScriptHash) {
            arrayList.add(cTxDestination);
            ckVar = new CScriptID(Script.getLockedSignScript(cTxDestination));
        } else if (cTxDestination instanceof CScriptID) {
            CScriptID ckVar2 = (CScriptID) cTxDestination;
            CPubkeyInterface t2 = address.getSelfPubKey();
            if (t2 instanceof CMultisigPubkey) {
                CScript j = ((CMultisigPubkey) t2).mo9451j();
                arrayList.add(new CScriptID(j));
                WitnessV0ScriptHash witnessV0ScriptHash = new WitnessV0ScriptHash(j);
                arrayList.add(witnessV0ScriptHash);
                CScriptID ckVar3 = new CScriptID(CScript.m484a((CTxDestination) witnessV0ScriptHash));
                if (!ckVar3.equals(ckVar2)) {
                    ckVar = ckVar3;
                }
            }
            ckVar = ckVar2;
        } else {
            ckVar = null;
        }
        if (ckVar == null) {
            return null;
        }
        return new ComplexBitcoinAddress(izVar, ckVar, arrayList);
    }

    public static CTxDestination m937a(String str, Wallet wallet) throws AddressFormatException {
        ResolvedAddressInfo b = m952b((CharSequence) str, wallet);
        if (b != null) {
            return b.cTxDestination;
        }
        return null;
    }

    public static CTxDestination m938a(String str, ChainParams chainParams) {
        try {
            ResolvedAddressInfo a = m932a((CharSequence) str, chainParams);
            if (a == null) {
                return null;
            }
            return a.cTxDestination;
        } catch (AddressFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResolvedAddressInfo m933a(CharSequence address, ChainParams chainParams, boolean z) throws AddressFormatException {
        if (chainParams instanceof VChainParam) {
            ResolvedAddressInfo resolvedAddressInfo = new ResolvedAddressInfo();
            if (isAnonymousAddress(address, (VChainParam) chainParams, resolvedAddressInfo)) {
                return resolvedAddressInfo;
            }
        }
        ResolvedAddressInfo witnessTypeAddressInfo = getWitnessTypeAddressInfo(address, chainParams);
        if (witnessTypeAddressInfo != null) {
            return witnessTypeAddressInfo;
        }
        return m935a(Base58.decodeChecked(address.toString()), chainParams, z);
    }

    public static ResolvedAddressInfo m932a(CharSequence charSequence, ChainParams uVar) throws AddressFormatException {
        return m933a(charSequence, uVar, false);
    }

    public static byte[] m948a(@NonNull ChainParams uVar, String str) throws AddressFormatException {
        ResolvedAddressInfo a = m932a((CharSequence) str, uVar);
        if (a != null) {
            return a.cTxDestination.data();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalidate address header: ");
        sb.append(str);
        throw new AddressFormatException(sb.toString());
    }

    public static boolean m946a(byte[] bArr, byte[] bArr2) {
        if (bArr2.length < bArr.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr2[i] != bArr[i]) {
                return false;
            }
        }
        return true;
    }




    //m922a
    public static SaplingPaymentAddress getSaplingPaymentAddressByDecode(String str, VChainParam vChainParam) {
        byte[] decodeSaplingAddrText = decodeSaplingAddrText(str, vChainParam.mo42378a(0), 69, false);
        if (decodeSaplingAddrText == null) {
            return null;
        }
        return new SaplingPaymentAddress(decodeSaplingAddrText, 0);
    }

    //m949b
    public static SaplingExtendedSpendingKey getSaplingExtendedSpendingKeyByDecode(String str, VChainParam vChainParam) {
        byte[] decodeSaplingAddrText = decodeSaplingAddrText(str, vChainParam.mo42378a(3), 271, true);
        if (decodeSaplingAddrText == null) {
            return null;
        }
        try {
            SaplingExtendedSpendingKey saplingExtendedSpendingKey = new SaplingExtendedSpendingKey(decodeSaplingAddrText, 0);
            DataTypeToolkit.setBytesZero(decodeSaplingAddrText);
            return saplingExtendedSpendingKey;
        } catch (Exception e) {
            e.printStackTrace();
            DataTypeToolkit.setBytesZero(decodeSaplingAddrText);
            return null;
        }
    }

    //m941a
    public static boolean isAnonymousAddress(CharSequence address, VChainParam vChainParam, ResolvedAddressInfo resolvedAddressInfo) {
        if (address == null) {
            return false;
        }
        String addressString = address.toString();
        SaplingPaymentAddress saplingPaymentAddress = getSaplingPaymentAddressByDecode(addressString, vChainParam);
        if (saplingPaymentAddress != null) {
            resolvedAddressInfo.addressType = AddressType.ANONYMOUS;
            resolvedAddressInfo.headerType = HeaderType.SAPLING_PAY_ADDR;
            resolvedAddressInfo.cTxDestination = saplingPaymentAddress;
            return true;
        }
        SaplingExtendedSpendingKey saplingExtendedSpendingKey = getSaplingExtendedSpendingKeyByDecode(addressString, vChainParam);
        if (saplingExtendedSpendingKey == null) {
            return false;
        }
        resolvedAddressInfo.addressType = AddressType.ANONYMOUS;
        resolvedAddressInfo.headerType = HeaderType.PRIVATE_KEY_ANONYMOUS;
        resolvedAddressInfo.cPrivateKey = saplingExtendedSpendingKey;
        resolvedAddressInfo.cPubKey = saplingExtendedSpendingKey.getPubKey();
        resolvedAddressInfo.cTxDestination = resolvedAddressInfo.cPubKey.getCKeyID();
        return true;
    }

    //m953b
    public static ResolvedAddressInfo getWitnessTypeAddressInfo(CharSequence charSequence, ChainParams chainParams) {
        ResolvedAddressInfo resolvedAddressInfo = null;
        if (charSequence == null || charSequence.toString().indexOf(chainParams.getF12925t()) == -1) {
            return null;
        }
        CTxDestination DecodeWitnessTxDestination = CTxDestinationFactory.DecodeWitnessTxDestination(charSequence.toString(), chainParams.getF12925t());
        if (DecodeWitnessTxDestination instanceof CNoDestination) {
            return null;
        }
        if (DecodeWitnessTxDestination instanceof WitnessV0KeyHash) {
            resolvedAddressInfo = new ResolvedAddressInfo();
            resolvedAddressInfo.headerType = HeaderType.WITNESS_V0_KEY_HASH;
            resolvedAddressInfo.addressType = AddressType.WITNESS_V0_KEY_HASH;
            resolvedAddressInfo.cTxDestination = DecodeWitnessTxDestination;
        } else if (DecodeWitnessTxDestination instanceof WitnessV0ScriptHash) {
            resolvedAddressInfo = new ResolvedAddressInfo();
            resolvedAddressInfo.headerType = HeaderType.WITNESS_V0_SCRIPT_HASH;
            resolvedAddressInfo.addressType = AddressType.WITNESS_V0_SCRIPT_HASH;
            resolvedAddressInfo.cTxDestination = DecodeWitnessTxDestination;
        } else if (DecodeWitnessTxDestination instanceof WitnessUnknown) {
            resolvedAddressInfo = new ResolvedAddressInfo();
            resolvedAddressInfo.headerType = HeaderType.WITNESS_UNKNOWN;
            resolvedAddressInfo.cTxDestination = DecodeWitnessTxDestination;
        }
        return resolvedAddressInfo;
    }

    private static void m940a(ResolvedAddressInfo joVar, ChainParams uVar) throws AddressFormatException {
        if (joVar.headerType != HeaderType.UNKNOWN) {
            switch (joVar.headerType) {
                case PUBKEY:
                    if (joVar.f13115c.length < 32) {
                        if (joVar.f13115c.length == 20) {
                            joVar.cTxDestination = new CKeyID(joVar.f13115c);
                            break;
                        }
                    } else {
                        joVar.cPubKey = new CPubKey(joVar.f13115c);
                        joVar.cTxDestination = joVar.cPubKey.getCKeyID();
                        break;
                    }
                    break;
//                case PUBKEY_P2SH:
//                    joVar.f13116d = new CScriptID(joVar.f13115c);
//                    break;
                case PRIVATE_KEY:
                    joVar.cPrivateKey = new CKey(joVar.f13115c);
                    joVar.cPubKey = joVar.cPrivateKey.getPubKey();
                    joVar.cTxDestination = joVar.cPubKey.getCKeyID();
                    break;
//                case CONTRACT:
//                    joVar.f13116d = new bjm(joVar.f13115c);
//                    break;
            }
            if ((joVar.f13113a == null || joVar.f13113a.length() == 0) && joVar.cTxDestination != null) {
                joVar.f13113a = getAddressString(joVar.cTxDestination, uVar);
            }
        }
    }

    private static boolean m943a(ResolvedAddressInfo resolvedAddressInfo, byte[] bArr, byte[] bArr2, AddressType addressType, HeaderType headerType, ChainParams uVar, boolean z) throws AddressFormatException {
        if (!m946a(bArr2, bArr)) {
            return false;
        }
        resolvedAddressInfo.f13114b = DataTypeToolkit.bytesCopy(bArr2);
        resolvedAddressInfo.f13115c = DataTypeToolkit.copyPartBytes(bArr, bArr2.length, bArr.length - bArr2.length);
        resolvedAddressInfo.addressType = addressType;
        resolvedAddressInfo.headerType = headerType;
        if (!z) {
            m940a(resolvedAddressInfo, uVar);
        }
        return true;
    }

    /* renamed from: a */
    public static CTxDestination m939a(CTxDestination oVar, Address jjVar) {
        if ((oVar instanceof WitnessV0KeyHash) || (oVar instanceof WitnessV0ScriptHash) || (oVar instanceof WitnessUnknown)) {
            return null;
        }
        if (oVar instanceof CKeyID) {
            return new WitnessV0KeyHash(oVar);
        }
        if ((oVar instanceof CScriptID) && jjVar.getAddressType() == AddressType.MULTISIG) {
            CMultisigPubkey nVar = (CMultisigPubkey) jjVar.getSelfPubKey();
            if (nVar != null) {
                return WitnessV0ScriptHash.m545a(nVar);
            }
        }
        return null;
    }

    public static ResolvedAddressInfo m935a(byte[] bArr, ChainParams chainParams, boolean z) throws AddressFormatException {
        ResolvedAddressInfo resolvedAddressInfo = new ResolvedAddressInfo();
        if (m943a(resolvedAddressInfo, bArr, chainParams.getBase58Prefixes_public_address(), AddressType.GENERAL, HeaderType.PUBKEY, chainParams, z)) {
            return resolvedAddressInfo;
        }
        if (m943a(resolvedAddressInfo, bArr, chainParams.getBase58Prefixes_script_address(), AddressType.MULTISIG, HeaderType.PUBKEY_P2SH, chainParams, z)) {
            return resolvedAddressInfo;
        }
        return m943a(resolvedAddressInfo, bArr, chainParams.getF12924s(), AddressType.GENERAL, HeaderType.PRIVATE_KEY, chainParams, z) ? resolvedAddressInfo : resolvedAddressInfo;
    }

    public static ResolvedAddressInfo m934a(byte[] bArr, ChainParams uVar) throws AddressFormatException {
        return m935a(bArr, uVar, false);
    }

    public static ResolvedAddressInfo m952b(CharSequence address, Wallet wallet) throws AddressFormatException {
        if (address == null) {
            return null;
        }
        ChainParams chainParams = wallet.getChainParams();
        if (chainParams instanceof VChainParam) {
            ResolvedAddressInfo resolvedAddressInfo = new ResolvedAddressInfo();
            if (isAnonymousAddress(address, (VChainParam) chainParams, resolvedAddressInfo)) {
                return resolvedAddressInfo;
            }
        }
        ResolvedAddressInfo witnessTypeAddressInfo = getWitnessTypeAddressInfo(address, chainParams);
        if (witnessTypeAddressInfo != null) {
            return witnessTypeAddressInfo;
        }
        try {
            return m934a(Base58.decodeChecked(address.toString()), wallet.getChainParams());
        } catch (Exception e) {
            e.printStackTrace();
            if (address.length() != 40) {
                return null;
            }
            ResolvedAddressInfo joVar2 = new ResolvedAddressInfo();
            m942a(joVar2, StringToolkit.m11526a(address.toString()), false);
            return joVar2;
        }
    }

    public static boolean m942a(ResolvedAddressInfo resolvedAddressInfo, byte[] bArr, boolean z) throws AddressFormatException {
        if (bArr.length == 20) {
            resolvedAddressInfo.f13114b = null;
            resolvedAddressInfo.f13115c = DataTypeToolkit.bytesCopy(bArr);
            resolvedAddressInfo.addressType = AddressType.CONTRACT;
            resolvedAddressInfo.headerType = HeaderType.CONTRACT;
            if (!z) {
                m940a(resolvedAddressInfo, (ChainParams) null);
            }
            return true;
        }
        throw new AddressFormatException(String.format(Locale.getDefault(), "Invalidate contract address len: %d", new Object[]{Integer.valueOf(bArr.length)}));
    }

    //m936a
    public static CTxDestination getDesFromAddressString(CharSequence address, Wallet wallet) {
        try {
            ResolvedAddressInfo resolvedAddressInfo = m952b(address, wallet);
            if (resolvedAddressInfo == null) {
                return null;
            }
            return resolvedAddressInfo.cTxDestination;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //m945a
    public static boolean checkLength(byte[] bArr) {
        if (bArr.length < 32) {
            return false;
        }
        if (bArr.length == 32) {
            return true;
        }
        if (bArr.length == 33 && bArr[32] == 1) {
            return true;
        }
        return false;
    }
    //m951b
    private static String mergeByteArr(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return Base58.encodeChecked(bArr3);
    }

    //m931a
    public static String getBase58String(ChainParams chainParams, HeaderType headerType, byte[] bArr) throws com.vc.libcommon.exception.AddressFormatException {
        if (headerType == HeaderType.PRIVATE_KEY) {
            if (checkLength(bArr)) {
                return mergeByteArr(chainParams.getF12924s(), bArr);
            }
            throw new com.vc.libcommon.exception.AddressFormatException("Invalidate private key!");
        }
//        else if (headerType == HeaderType.PRIVATE_KEY_ANONYMOUS) {
//            try {
//                return uVar instanceof biq ? m923a(new brw(bArr), (biq) uVar) : "";
//            } catch (IOException e) {
//                throw new com.vc.libcommon.exception.AddressFormatException((Throwable) e);
//            }
//        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate privatekey ");
            sb.append(Base58.encodeToString(bArr));
            throw new com.vc.libcommon.exception.AddressFormatException(sb.toString());
        }
    }

}
