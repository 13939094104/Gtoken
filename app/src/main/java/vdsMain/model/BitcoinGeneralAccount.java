package vdsMain.model;

import androidx.annotation.NonNull;
import bitcoin.CExtKey;
import bitcoin.account.hd.HDSeed;
import generic.exceptions.EncryptException;
import generic.exceptions.NotMatchException;
import vdsMain.WitnessKeyHashAddress;
import vdsMain.WitnessKeyScriptAddress;
import vdsMain.WitnessScriptHashAddress;
import vdsMain.WitnessScriptHashScriptAddress;
import vdsMain.table.AddressTable;
import vdsMain.table.WalletTable;

import java.util.List;
import java.util.Vector;

public class BitcoinGeneralAccount extends GeneralAccount {

    //f4890a
    private int index = 0;

    public BitcoinGeneralAccount(@NonNull WalletTable kjVar) {
        super(kjVar);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public Address getAddressEntity(WalletTable walletTable, CharSequence pwd) {
        return new BitCoinAddress(walletTable, pwd.toString());
    }

    /* renamed from: a */
    public boolean isBitCoinAddress(Address address) {
        return (address instanceof BitCoinAddress)|| (address instanceof WitnessKeyHashAddress) || (address instanceof WitnessScriptHashAddress) || (address instanceof WitnessKeyScriptAddress) || (address instanceof WitnessScriptHashScriptAddress);
    }

    /* renamed from: a */
    public List<Address> createAddress(CharSequence charSequence, int amount, CharSequence... charSequenceArr) throws EncryptException, NotMatchException {
        CharSequence pwd = charSequence;
        int sumAmount = amount;
        CharSequence[] labelArr = charSequenceArr;
        this.wallet.checkWalletPassword(pwd);
        Vector vector = new Vector(sumAmount);
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        AccountModel accountModel = this.wallet.getSelfAccountModel();
        AddressTable addressTable = this.wallet.getPersonalDB().getSelfAddressTable();
        Vector hdAccountVector = accountModel.getHDAccountVector();
        if (hdAccountVector == null || hdAccountVector.isEmpty()) {
            return vector;
        }
        HDAccount hdAccount = (HDAccount) hdAccountVector.get(0);
        if (!hdAccount.isMontitored()) {
            HDSeed hdSeed = hdAccount.getHDSeedFormEncypted(pwd);
            CExtKey hdCExtKey = hdSeed.getCExtKey(HDSeed.InteralType.INTERNAL);
            int createNumer = sumAmount;
            while (createNumer > 0) {
                CExtKey cExtKey = new CExtKey();
                hdCExtKey.mo9392a(cExtKey, ((long) this.index) | 2147483648L);
                try {
                    BitCoinAddress bitCoinAddress = new BitCoinAddress(addressTable, cExtKey.cKey.getPubKey().getByteArr(),cExtKey.cKey.getUnCompressedPubKey().getByteArr() ,cExtKey.cKey.getCopyBytes(), pwd);
                    if (addressModel.isUsingDesAddressMapHasKey(bitCoinAddress.getCTxDestination())) {
                        cExtKey.resetBytes();
                        this.index++;
                    } else {
                        if (labelArr.length > 0) {
                            bitCoinAddress.setLabel(labelArr[sumAmount - createNumer]);
                        }
                        vector.add(bitCoinAddress);
                        this.index++;
                        createNumer--;
                        cExtKey.resetBytes();
                    }
                } catch (Exception e) {
                    createNumer--;
                    e.printStackTrace();
                }
            }
            hdSeed.reset();
            checkAndAddToBitCoinCtxDestAddressMap((List<Address>) vector, true);
            return vector;
        }
        throw new EncryptException("There's no avaliable general address seed.");
    }
}