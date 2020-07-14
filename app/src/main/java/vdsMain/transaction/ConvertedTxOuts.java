package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.CNoDestination;
import com.vc.libcommon.exception.AddressFormatException;
import generic.utils.AddressUtils;
import vdsMain.CTxDestination;
import vdsMain.StringToolkit;
import vdsMain.wallet.Wallet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ConvertedTxOuts extends ArrayList<TxOut> {

    //f12960a
    public long sumValue = 0;

    //f12961b
    protected Wallet wallet;

    public ConvertedTxOuts(@NonNull Wallet izVar) {
        this.wallet = izVar;
    }

    public void addContractCallInfo(ContractCallInfo contractCallInfo){
        TxOut newTxOut = this.wallet.getSelfWalletHelper().getNewTxOut();
        newTxOut.setFlag(0);
        newTxOut.setSatoshiValue(0);
        newTxOut.checkAndSetScript(Script.getContractCallScript(contractCallInfo), false);
        add(newTxOut);
        sumValue+=contractCallInfo.getGasLimitUsed();
    }

    //mo43998a
    public void addAddressMoneyInfo(List<AddressMoneyInfo> list, List<TxOut>... listArr) throws AddressFormatException {
        long sumOutsValue=0;
        if (list != null && !list.isEmpty()) {
            long j2 = 0;
            if (listArr.length > 0 && listArr[0] != null) {
                for (TxOut txOut : listArr[0]) {
                    add(txOut);
                    j2 += txOut.getSatoshi();
                }
            }
            for (AddressMoneyInfo addressMoneyInfo : list) {
                TxOut newTxOut = this.wallet.getSelfWalletHelper().getNewTxOut();
                newTxOut.setFlag(addressMoneyInfo.getFlag());
                newTxOut.setSatoshiValue(addressMoneyInfo.sendValue);
                if (addressMoneyInfo.getFlag() != 8 || (addressMoneyInfo.receiveAddress != null && !"".equals(addressMoneyInfo.receiveAddress))) {
                    if (!StringToolkit.checkIsNull((CharSequence) addressMoneyInfo.receiveAddress)) {
                        CTxDestination cTxDestination = AddressUtils.getDesFromAddressString((CharSequence) addressMoneyInfo.receiveAddress, this.wallet);
                        if (cTxDestination == null || (cTxDestination instanceof CNoDestination)) {
                            throw new AddressFormatException(String.format(Locale.getDefault(), "Unsupported address %s", new Object[]{addressMoneyInfo.receiveAddress}));
                        }
                        Script lockedSignScript = Script.getLockedSignScript(cTxDestination);
                        if (lockedSignScript != null) {
                            newTxOut.checkAndSetScript(lockedSignScript, false);
                        } else {
                            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not convert address to script %s.", new Object[]{addressMoneyInfo.receiveAddress}));
                        }
                    }
                    add(newTxOut);
                    sumOutsValue += addressMoneyInfo.sendValue;
                } else {
                    add(newTxOut);
                    sumOutsValue += addressMoneyInfo.sendValue;
                }
            }
            this.sumValue = sumOutsValue;
        }
    }

    //mo43999a
    public void addNewTxOut(CTxDestination cTxDestination, long value) {
        TxOut newTxOut = this.wallet.getSelfWalletHelper().getNewTxOut();
        newTxOut.setSatoshiValue(value);
        Script lockedSignScript = Script.getLockedSignScript(cTxDestination);
        if (lockedSignScript != null) {
            newTxOut.checkAndSetScript(lockedSignScript, new boolean[0]);
        }
        add(newTxOut);
    }

    /* renamed from: a */
    public boolean add(TxOut txOut) {
        txOut.setIndex(size());
        boolean add = super.add(txOut);
        if (add) {
            this.sumValue += txOut.getSatoshi();
        }
        return add;
    }

    public boolean addAll(Collection<? extends TxOut> collection) {
        boolean addAll = super.addAll(collection);
        if (!addAll) {
            this.sumValue = 0;
        } else {
            m12186a(collection);
        }
        return addAll;
    }

    /* renamed from: a */
    private void m12186a(Collection<? extends TxOut> collection) {
        if (collection != null) {
            int i = 0;
            for (TxOut txOut : collection) {
                int i2 = i + 1;
                txOut.setIndex(i);
                this.sumValue += txOut.getSatoshi();
                i = i2;
            }
            super.addAll(collection);
        }
    }

    public void clear() {
        super.clear();
        this.sumValue = 0;
    }
}