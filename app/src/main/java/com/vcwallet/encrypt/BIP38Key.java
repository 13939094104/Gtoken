package com.vcwallet.encrypt;

import generic.exceptions.EncryptException;
import generic.exceptions.InvalidatePasswordException;
import vdsMain.*;
import vdsMain.wallet.ChainParams;

public class BIP38Key {
    public static native byte[] DecryptBIP38Key(String str, String str2);

    private static native String EncryptBIP38Key(String str, byte[] bArr, String str2, boolean z);

    //m897a
    public static String getBIP38Key(ChainParams chainParams, CPrivateKeyInterface cPrivateKeyInterface, CharSequence charSequence) throws EncryptException, InvalidatePasswordException {
        if (cPrivateKeyInterface == null) {
            throw new EncryptException("Private key is null!");
        } else if (cPrivateKeyInterface.getPivateKeyType() != PrivateKeyType.SAPLING_EXTENED_SPEND_KEY) {
            PasswordUtil.m12070a(charSequence);
            CPubkeyInterface pubkeyInterface = cPrivateKeyInterface.getPubKey();
            return EncryptBIP38Key(new CBitcoinAddress(pubkeyInterface.getCKeyID(), chainParams).toString(), cPrivateKeyInterface.getCopyBytes(), charSequence.toString(), pubkeyInterface.checkLength());
        } else {
            throw new EncryptException("Not support sapling extened spend key yet!");
        }
    }
}