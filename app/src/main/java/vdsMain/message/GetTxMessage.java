package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.script.CScript;
import generic.io.StreamWriter;
import vcash.script.VStandard;
import vdsMain.AddressType;
import vdsMain.CTxDestination;
import vdsMain.Log;
import vdsMain.transaction.SolveResult;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

//blo
public class GetTxMessage extends VMessage {

    //f11887a
    public long beginBlock = 1;

    //f11888b
    public long endBlock = -1;

    //f11889h
    public List<CScript> cScriptList = new Vector();

    public GetTxMessage(@NonNull Wallet izVar) {
        super(izVar, "getx");
    }

    public GetTxMessage(@NonNull Wallet izVar, long j, long j2, @NonNull List<Address> list, List<CTxDestination> list2) {
        super(izVar, "getx");
        setAndCheckBeginEndBlock(j, j2);
        initCScriptLsit(list, list2);
    }

    //mo42567a
    public void setAndCheckBeginEndBlock(long j, long j2) {
        this.beginBlock = j;
        if (this.beginBlock < 0) {
            this.beginBlock = 0;
        }
        this.endBlock = j2;
        if (this.endBlock <= 0) {
            this.endBlock = this.wallet.getMaxBlockNo();
        }
        if (this.beginBlock > this.endBlock) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Begin block %d must smaller than end block %d", new Object[]{Long.valueOf(this.beginBlock), Long.valueOf(this.endBlock)}));
        }
    }

    //mo42568a
    public void initCScriptLsit(@NonNull List<Address> addressList, List<CTxDestination> cTxDestinationList) {
        this.cScriptList.clear();
        try {
            for (Address address : addressList) {
                AddressType addressType = address.getAddressType();
                if (addressType != AddressType.ANONYMOUS) {
                    if (addressType != AddressType.UNKNOWN) {
                        this.cScriptList.add(CScript.m484a(address.getCTxDestination()));
                    }
                }
            }
            if (cTxDestinationList != null && !cTxDestinationList.isEmpty()) {
                for (CTxDestination cTxDestination : cTxDestinationList) {
                    this.cScriptList.add(CScript.m484a(cTxDestination));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.beginBlock);
        streamWriter.writeUInt32T(this.endBlock);
        streamWriter.writeVariableInt((long) this.cScriptList.size());
        for (CScript serialToStream : this.cScriptList) {
            serialToStream.serialToStream(streamWriter);
        }
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.beginBlock = readUInt32();
        this.endBlock = readUInt32();
        int b = readVariableInt().getIntValue();
        SolveResult dbVar = new SolveResult();
        for (int i = 0; i < b; i++) {
            CScript cScript = new CScript(readVariableBytes());
            if (!VStandard.setSloveResultTypeAndCheckIsNotTxNonStandard(cScript, dbVar, new boolean[0])) {
                Log.infoObject((Object) this, String.format(Locale.getDefault(), "Error occured when decode message %s", new Object[]{getTypeString()}));
                return;
            }
            this.cScriptList.add(cScript);
        }
    }
}
