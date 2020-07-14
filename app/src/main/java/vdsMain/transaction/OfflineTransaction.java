package vdsMain.transaction;

import androidx.annotation.NonNull;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.NotMatchException;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.BitcoinMultiSigAddress;
import vdsMain.CTxDestination;
import vdsMain.StringToolkit;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class OfflineTransaction extends SeriableData {

    /* renamed from: a */
    public Transaction f13338a;

    /* renamed from: b */
    public VOutList f13339b;

    /* renamed from: c */
    public ScriptList f13340c;

    /* renamed from: d */
    public BLOCK_CHAIN_TYPE f13341d;

    /* renamed from: e */
    protected Wallet f13342e;

    /* renamed from: f */
    public long f13343f;

    /* renamed from: g */
    public long f13344g;

    /* renamed from: h */
    public int f13345h;

    /* renamed from: i */
    public int f13346i = 2;

    /* renamed from: j */
    public CTxDestination f13347j;

    public OfflineTransaction(@NonNull Wallet izVar) {
        this.f13341d = izVar.getBlockChainType();
        mo44665a();
        this.f13342e = izVar;
        this.f13338a = izVar.getSelfWalletHelper().getNewTransaction();
        this.f13339b = new VOutList(izVar);
        this.f13340c = new ScriptList();
        this.f13343f = 0;
        this.f13344g = 0;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo44665a() {
        if (this.f13341d != BLOCK_CHAIN_TYPE.BITCOIN) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate Offline tx type: ");
            sb.append(this.f13341d);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeBytes(new byte[]{(byte) this.f13341d.getChainType()});
        streamWriter.writeBytes(this.f13338a.serialSelfToBytes());
        this.f13339b.serialToStream(streamWriter);
        this.f13340c.writeSerialData(streamWriter);
        streamWriter.writeUInt64(this.f13343f);
        streamWriter.writeUInt64(this.f13344g);
        streamWriter.writeUInt16(this.f13345h);
        writeVariableInt((long) this.f13346i);
        CTxDestination oVar = this.f13347j;
        if (oVar != null) {
            oVar.writeTypeAndData(streamWriter);
        }
    }

    public void onDecodeSerialData() {
        this.f13341d = BLOCK_CHAIN_TYPE.getChainType(readByte());
        this.f13338a.decodeSerialItem(this.mTempBuffer);
        this.f13338a.recomputeByTxs(null);
        this.f13339b.decodeSerialStream(this.mTempBuffer);
        this.f13340c.decodeSerialStream(this.mTempBuffer);
        this.f13343f = readUInt64().longValue();
        this.f13344g = readUInt64().longValue();
        this.f13345h = readUInt16();
        if (hasReadableDataLeft()) {
            this.f13346i = readVariableInt().getIntValue();
        }
        try {
            if (this.f13346i >= 2) {
                this.f13347j = CTxDestinationFactory.m910a((SeriableData) this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public TxSignatureResult mo44664a(@NonNull CharSequence charSequence) throws AddressFormatException, NotMatchException, ScriptException {
        return new TxSignatureResult(this, this.f13338a.mo42826a(charSequence, this.f13339b, this.f13340c));
    }

    /* renamed from: b */
    public String mo44667b() {
        try {
            return StringToolkit.bytesToString(serialToStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: c */
    public int mo44668c() {
        Transaction dhVar = this.f13338a;
        if (dhVar == null) {
            return 0;
        }
        return dhVar.mo43235G();
    }

    /* renamed from: a */
    public List<String> mo44663a(ChainParams uVar) {
        Vector vector = new Vector(this.f13339b.mo44704a());
        for (TxOut l : this.f13339b.txOutList) {
            vector.add(AddressUtils.getAddressString(l.getScriptCTxDestination(), uVar));
        }
        return vector;
    }

    /* renamed from: d */
    public int mo44669d() {
        this.f13345h = 0;
        try {
            for (CScript crVar : this.f13340c.cScriptList) {
                Script crVar2 = new Script(crVar);
                switch (crVar2.getTxnOutType()) {
                    case TX_PUBKEY:
                    case TX_PUBKEYHASH:
                        this.f13345h++;
                        break;
                    case TX_MULTISIG:
                        this.f13345h += crVar2.mo43174o();
                        break;
                    case TX_SCRIPTHASH:
                        this.f13345h += ((BitcoinMultiSigAddress) this.f13342e.getAddressByCTxDestinationFromArrayMap(crVar2.mo43169j())).mo40878h();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.f13345h = 0;
        }
        Transaction dhVar = this.f13338a;
        if (dhVar != null) {
            this.f13345h /= dhVar.getSelfTxInList().size();
        }
        return this.f13345h;
    }

    /* renamed from: a */
    public void mo44666a(CTxDestination oVar) {
        this.f13347j = oVar;
    }
}
