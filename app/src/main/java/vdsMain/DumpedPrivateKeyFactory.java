package vdsMain;

import androidx.annotation.NonNull;
import com.vc.libcommon.exception.AddressFormatException;
import generic.crypto.Base58;
import generic.utils.AddressUtils;
import vdsMain.wallet.ChainParams;

import java.util.Locale;

public final class DumpedPrivateKeyFactory {
    /* renamed from: a */
    public static ChainParams m12067a(String str, ChainParams... chainParams) throws AddressFormatException {
        if (chainParams == null || chainParams.length == 0) {
            throw new AddressFormatException("Please give at last 1 chain params!");
        }
        for (ChainParams params : chainParams) {
            String[] b = params.mo42379b();
            if (!(b == null || b.length == 0)) {
                for (String indexOf : b) {
                    if (str.indexOf(indexOf) == 0) {
                        return params;
                    }
                }
                continue;
            }
        }
        byte[] decodeChecked = Base58.decodeChecked(str);
        for (ChainParams uVar2 : chainParams) {
            for (byte[] b2 : uVar2.mo42380c()) {
                if (DataTypeToolkit.m11506b(decodeChecked, b2)) {
                    return uVar2;
                }
            }
        }
        return null;
    }

    //m12065a
    public static DecodedPrivateKeyInfo checkAndGetDecodedPrivateKeyInfo(@NonNull ChainParams chainParams, String str) throws AddressFormatException {
        if (chainParams instanceof VChainParam) {
            VChainParam chainParam = (VChainParam) chainParams;
            ResolvedAddressInfo addressInfo = new ResolvedAddressInfo();
            if (AddressUtils.isAnonymousAddress((CharSequence) str, chainParam, addressInfo)) {
                if (addressInfo.headerType == HeaderType.PRIVATE_KEY_ANONYMOUS) {
                    return new DecodedPrivateKeyInfo(HeaderType.PRIVATE_KEY_ANONYMOUS, null, addressInfo.cPrivateKey.getCopyBytes(), null);
                }
                throw new AddressFormatException(String.format(Locale.getDefault(), "Address [%s] not an validate sapling anonymous private key.", new Object[0]));
            }
        }
        return getDecodedPrivateKeyInfo(chainParams, Base58.decodeChecked(str));
    }

    //m12066a
    public static DecodedPrivateKeyInfo getDecodedPrivateKeyInfo(@NonNull ChainParams chainParams, byte[] bArr) throws AddressFormatException {
        if (bArr.length == 32) {
            byte[] bArr2 = new byte[(chainParams.getF12924s().length + bArr.length)];
            System.arraycopy(chainParams.getF12924s(), 0, bArr2, 0, chainParams.getF12924s().length);
            System.arraycopy(bArr, 0, bArr2, chainParams.getF12924s().length, 32);
            return new DecodedPrivateKeyInfo(HeaderType.PRIVATE_KEY, chainParams.getF12924s(), bArr, bArr2);
        } else if (bArr.length == 169) {
            new DecodedPrivateKeyInfo();
            return new DecodedPrivateKeyInfo(HeaderType.PRIVATE_KEY, chainParams.getF12924s(), bArr, bArr);
        } else {
            HeaderType jnVar = HeaderType.UNKNOWN;
            byte[] bArr3 = null;
            if (AddressUtils.m946a(chainParams.getF12924s(), bArr)) {
                jnVar = HeaderType.PRIVATE_KEY;
                bArr3 = chainParams.getF12924s();
            }
            if (jnVar != HeaderType.UNKNOWN) {
                int length = bArr.length - bArr3.length;
                byte[] bArr4 = new byte[length];
                System.arraycopy(bArr, bArr3.length, bArr4, 0, length);
                if (AddressUtils.checkLength(bArr4)) {
                    return new DecodedPrivateKeyInfo(jnVar, bArr3, bArr4, bArr);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Invalidate private key ");
                sb.append(Base58.encodeToString(bArr));
                throw new AddressFormatException(sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalidate private key ");
            sb2.append(Base58.encodeToString(bArr));
            throw new AddressFormatException(sb2.toString());
        }
    }
}
