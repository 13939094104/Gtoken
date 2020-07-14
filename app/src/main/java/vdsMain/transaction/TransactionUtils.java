package vdsMain.transaction;

import bitcoin.CNoDestination;
import com.vc.libcommon.exception.AddressFormatException;
import com.vtoken.vdsecology.vcash.VCashCore;
import generic.exceptions.*;
import generic.utils.AddressUtils;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.CTxDestination;
import vdsMain.StringToolkit;
import vdsMain.Utils;
import vdsMain.model.TransactionModel;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionUtils {

    /* renamed from: mc$a */
    /* compiled from: TransactionUtils */
    //C3902a
    public static class SendTransaction {

        //m13292a
        long fee;

        //f13374b
        List<Utxo> spendUtxoList;

        //f13375c
        List<AddressMoneyInfo> addressMoneyInfoList;

        //mo44712a
        public void setSpendUtxoList(List<Utxo> list) {
            this.spendUtxoList = list;
        }

        //mo44713b
        public void setAddressMoneyInfoList(List<AddressMoneyInfo> list) {
            this.addressMoneyInfoList = list;
        }

        //mo44711a
        public void setFee(long j) {
            this.fee = j;
        }
    }

    public static TxSignatureResult getCallContractTxSignatureResult(boolean checkMaxSize, VCashCore vCashCore, List<Utxo> selectUtxoList, List<Utxo> spendUtxoList,
                                                                     ContractCallInfo contractCallInfo, long fee, boolean inculdeFee, CharSequence pwd, BLOCK_CHAIN_TYPE block_chain_type) {
        SendTransaction sendTransaction;
        if (selectUtxoList == null) {
            sendTransaction = getContractCallTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, contractCallInfo, fee, inculdeFee, new Utxo[0]);
        } else if (selectUtxoList.size() > spendUtxoList.size()) {
            sortUtxoList(selectUtxoList);
            Utxo firstUnSpendUtxo = (Utxo) selectUtxoList.get(spendUtxoList.size());
            sendTransaction = getContractCallTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, contractCallInfo, fee, inculdeFee, firstUnSpendUtxo);
        } else {
            sendTransaction = getContractCallTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, contractCallInfo, fee, inculdeFee, new Utxo[0]);
        }
        return vCashCore.signContractCallTransactionAndGetResult(sendTransaction.spendUtxoList,contractCallInfo, sendTransaction.addressMoneyInfoList, sendTransaction.fee, pwd, block_chain_type);
    }

    public static SendTransaction getContractCallTransaction(Wallet wallet, boolean checkMaxSize, List<Utxo> spendUtxoList,ContractCallInfo contractCallInfo, long fee, boolean inculdeFee, Utxo... unSpendUtxos) {
        long j2;
        int i=0;
        long f;
        long sumSpendValue = 0;
        for (Utxo spendUtxo : spendUtxoList) {
            sumSpendValue += spendUtxo.getValue();
        }
        long sumSendValue = 0;
        sumSendValue += contractCallInfo.getGasLimitUsed();
        long remainingValue = (sumSpendValue - sumSendValue) - fee;
        ArrayList<TxOut> txOutList = new ArrayList();
        ArrayList<AddressMoneyInfo> addressMoneyInfoList=new ArrayList();
        TxOut newTxOut = wallet.getSelfWalletHelper().getNewTxOut();
        newTxOut.setFlag(0);
        newTxOut.setSatoshiValue(0);
        txOutList.add(newTxOut);
        Script contractCallScript = Script.getContractCallScript(contractCallInfo);
        if (contractCallScript != null) {
            newTxOut.checkAndSetScript(contractCallScript, false);
        }

        if (remainingValue >= getSeriableOutsLength(wallet, txOutList)) {
            if (remainingValue >= 30000) {
                addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(0)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), remainingValue));
                SendTransaction sendTransaction = new SendTransaction();
                sendTransaction.setFee(fee);
                sendTransaction.setSpendUtxoList(spendUtxoList);
                sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                return sendTransaction;
            } else if (unSpendUtxos.length > 0) {
                spendUtxoList.add(unSpendUtxos[0]);
                addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(0)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), remainingValue));
                try {
                    i = 0;
                    try {
                        j2 = wallet.getSelfTransactionModel().calMinFee(checkMaxSize, Utils.getCoutPointUtxoFromUtxoList(spendUtxoList), addressMoneyInfoList, fee, (VOutList) null, (ScriptList) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        j2 = 10000;
                        long max = Math.max(fee, j2);
                        f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - max;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    j2 = 10000;
                    long max2 = Math.max(fee, j2);
                    f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - max2;
                }
                long maxBetwennFee = Math.max(fee, j2);
                f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - maxBetwennFee;
                if (f >= 30000) {
                    addressMoneyInfoList.remove(addressMoneyInfoList.size() - 1);
                    addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(i)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), f));
                    SendTransaction sendTransaction = new SendTransaction();
                    sendTransaction.setFee(maxBetwennFee);
                    sendTransaction.setSpendUtxoList(spendUtxoList);
                    sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                    return sendTransaction;
                }
                spendUtxoList.remove(spendUtxoList.size() - 1);
                addressMoneyInfoList.remove(addressMoneyInfoList.size() - 1);
                SendTransaction sendTransaction = new SendTransaction();
                sendTransaction.setFee(fee);
                sendTransaction.setSpendUtxoList(spendUtxoList);
                sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                return sendTransaction;
            }
        }
        SendTransaction sendTransaction = new SendTransaction();
        sendTransaction.setFee(fee);
        sendTransaction.setSpendUtxoList(spendUtxoList);
        sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
        return sendTransaction;
    }

    //m13295a
    public static TxSignatureResult getTxSignatureResult(boolean checkMaxSize, VCashCore vCashCore, List<Utxo> selectUtxoList, List<Utxo> spendUtxoList, List<AddressMoneyInfo> addressMoneyInfoList, long fee, boolean inculdeFee, CharSequence pwd, BLOCK_CHAIN_TYPE block_chain_type) {
        SendTransaction sendTransaction;
        if (selectUtxoList == null) {
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, addressMoneyInfoList, fee, inculdeFee, new Utxo[0]);
        } else if (selectUtxoList.size() > spendUtxoList.size()) {
            sortUtxoList(selectUtxoList);
            Utxo firstUnSpendUtxo = (Utxo) selectUtxoList.get(spendUtxoList.size());
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, addressMoneyInfoList, fee, inculdeFee, firstUnSpendUtxo);
        } else {
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(block_chain_type), checkMaxSize, spendUtxoList, addressMoneyInfoList, fee, inculdeFee, new Utxo[0]);
        }
        return vCashCore.signTransactionAndGetResult(sendTransaction.spendUtxoList, sendTransaction.addressMoneyInfoList, sendTransaction.fee, pwd, block_chain_type);
    }



    //m13296a
    public static SendTransaction getSendTransaction(Wallet wallet, boolean checkMaxSize, List<Utxo> spendUtxoList, List<AddressMoneyInfo> addressMoneyInfoList, long fee, boolean inculdeFee, Utxo... unSpendUtxos) {
        long j2;
        int i;
        long f;
        long sumSpendValue = 0;
        for (Utxo spendUtxo : spendUtxoList) {
            sumSpendValue += spendUtxo.getValue();
        }
        long sumSendValue = 0;
        for (AddressMoneyInfo addressMoneyInfo : addressMoneyInfoList) {
            sumSendValue += addressMoneyInfo.sendValue;
        }
        long remainingValue = (sumSpendValue - sumSendValue) - fee;
        ArrayList<TxOut> txOutList = new ArrayList();
        for (AddressMoneyInfo addressMoneyInfo : addressMoneyInfoList) {
            TxOut newTxOut = wallet.getSelfWalletHelper().getNewTxOut();
            newTxOut.setFlag(addressMoneyInfo.getFlag());
            newTxOut.setSatoshiValue(addressMoneyInfo.sendValue);
            if (addressMoneyInfo.getFlag() != 8 || (addressMoneyInfo.receiveAddress != null && !"".equals(addressMoneyInfo.receiveAddress))) {
                txOutList.add(newTxOut);
                if (!StringToolkit.checkIsNull((CharSequence) addressMoneyInfo.receiveAddress)) {
                    CTxDestination cTxDestination = AddressUtils.getDesFromAddressString((CharSequence) addressMoneyInfo.receiveAddress, wallet);
                    if (cTxDestination != null && !(cTxDestination instanceof CNoDestination)) {
                        Script lockedSignScript = Script.getLockedSignScript(cTxDestination);
                        if (lockedSignScript != null) {
                            newTxOut.checkAndSetScript(lockedSignScript, false);
                        }
                    }
                }
            } else {
                txOutList.add(newTxOut);
            }
        }
        if (remainingValue >= getSeriableOutsLength(wallet, txOutList)) {
            if (remainingValue >= 30000) {
                addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(0)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), remainingValue));
                SendTransaction sendTransaction = new SendTransaction();
                sendTransaction.setFee(fee);
                sendTransaction.setSpendUtxoList(spendUtxoList);
                sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                return sendTransaction;
            } else if (unSpendUtxos.length > 0) {
                spendUtxoList.add(unSpendUtxos[0]);
                addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(0)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), remainingValue));
                try {
                    i = 0;
                    try {
                        j2 = wallet.getSelfTransactionModel().calMinFee(checkMaxSize, Utils.getCoutPointUtxoFromUtxoList(spendUtxoList), addressMoneyInfoList, fee, (VOutList) null, (ScriptList) null);
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        j2 = 10000;
                        long max = Math.max(fee, j2);
                        f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - max;
                        if (f >= 30000) {
                        }
                    }
                } catch (Exception e2) {
                    i = 0;
                    e2.printStackTrace();
                    j2 = 10000;
                    long max2 = Math.max(fee, j2);
                    f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - max2;
                    if (f >= 30000) {
                    }
                }
                long maxBetwennFee = Math.max(fee, j2);
                f = ((sumSpendValue + unSpendUtxos[i].getValue()) - sumSendValue) - maxBetwennFee;
                if (f >= 30000) {
                    addressMoneyInfoList.remove(addressMoneyInfoList.size() - 1);
                    addressMoneyInfoList.add(new AddressMoneyInfo(wallet.getAddressByCTxDestinationFromArrayMap(((Utxo) spendUtxoList.get(i)).getSaplingUtxoDes()).getAddressString(wallet.getBlockChainType()), f));
                    SendTransaction sendTransaction = new SendTransaction();
                    sendTransaction.setFee(maxBetwennFee);
                    sendTransaction.setSpendUtxoList(spendUtxoList);
                    sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                    return sendTransaction;
                }
                spendUtxoList.remove(spendUtxoList.size() - 1);
                addressMoneyInfoList.remove(addressMoneyInfoList.size() - 1);
                SendTransaction sendTransaction = new SendTransaction();
                sendTransaction.setFee(fee);
                sendTransaction.setSpendUtxoList(spendUtxoList);
                sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
                return sendTransaction;
            }
        }
        SendTransaction sendTransaction = new SendTransaction();
        sendTransaction.setFee(fee);
        sendTransaction.setSpendUtxoList(spendUtxoList);
        sendTransaction.setAddressMoneyInfoList(addressMoneyInfoList);
        return sendTransaction;
    }

    //m13292a
    private static long getSeriableOutsLength(Wallet wallet, List<TxOut> list) {
        SeriableOuts seriableOuts = new SeriableOuts(wallet);
        seriableOuts.setTxOutList(list);
        try {
            return (long) ((seriableOuts.serialToStream().length + 148) * 3);
        } catch (Exception e) {
            e.printStackTrace();
            return 10000;
        }
    }

    /* renamed from: a */
    public static OfflineTransaction m13294a(VCashCore vCashCore, List<Utxo> list, List<Utxo> list2, List<AddressMoneyInfo> list3, long j, boolean z, CharSequence charSequence, BLOCK_CHAIN_TYPE igVar) {
        SendTransaction sendTransaction;
        if (list == null) {
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(igVar), false, list2, list3, j, z);
        } else if (list.size() > list2.size()) {
            sortUtxoList(list);
            Utxo lzVar = (Utxo) list.get(list2.size());
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(igVar), false, list2, list3, j, z, lzVar);
        } else {
            sendTransaction = getSendTransaction(vCashCore.getWalletByChainType(igVar), false, list2, list3, j, z);
        }
        return vCashCore.mo43771a(charSequence, Utils.getCoutPointUtxoFromUtxoList(sendTransaction.spendUtxoList), sendTransaction.addressMoneyInfoList, sendTransaction.fee, igVar);
    }

    //m13293a
    public static Transaction getNewTransaction(TransactionModel transactionModel, boolean checkMaxSize, List<Utxo> utxoList, CharSequence pwd, List<Utxo> spendUtxoList,
                                                List<AddressMoneyInfo> addressMoneyInfos, long fee, boolean includeFee, VOutList vOutList, ScriptList scriptList, List<Utxo>... unSpendUtxoList) throws InvalidateUtxoException, AddressFormatException, NotMatchException, EncryptException, IOException, AddressNotFoundException, NoPrivateKeyException, SignatureFailedException {
        SendTransaction sendTransaction;
        if (utxoList == null) {
            sendTransaction = getSendTransaction(transactionModel.getWalllet(), checkMaxSize, spendUtxoList, addressMoneyInfos, fee, includeFee);
        } else if (utxoList.size() > spendUtxoList.size()) {
            sortUtxoList(utxoList);
            Utxo utxo = (Utxo) utxoList.get(spendUtxoList.size());
            sendTransaction = getSendTransaction(transactionModel.getWalllet(), checkMaxSize, spendUtxoList, addressMoneyInfos, fee, includeFee, utxo);
        } else {
            sendTransaction = getSendTransaction(transactionModel.getWalllet(), checkMaxSize, spendUtxoList, addressMoneyInfos, fee, includeFee);
        }
        return transactionModel.createNewTransaction(pwd, Utils.getCoutPointUtxoFromUtxoList(sendTransaction.spendUtxoList), sendTransaction.addressMoneyInfoList, sendTransaction.fee, vOutList, scriptList, new TxInfo[0]);
    }

    //m13297a
    public static void sortUtxoList(List<Utxo> list) {
        if (list != null) {
            Collections.sort(list, new Comparator<Utxo>() {
                /* renamed from: a */
                public int compare(Utxo utxo1, Utxo utxo2) {
                    int i = (utxo1.getValue() > utxo2.getValue() ? 1 : (utxo1.getValue() == utxo2.getValue() ? 0 : -1));
                    if (i > 0) {
                        return -1;
                    }
                    return i < 0 ? 1 : 0;
                }
            });
        }
    }
}
