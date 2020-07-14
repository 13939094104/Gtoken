package vdsMain.message;

import androidx.annotation.NonNull;
import bitcoin.CPubKey;
import com.google.common.base.Ascii;
import generic.network.AddressInfo;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;

//bmq
public class VersionMessage extends VMessage implements VersionMessageInterface {

    //f11929a
    private long serviceInt = 0;

    //f11930b
    private long time = System.currentTimeMillis();

    //f11931h
    private BigInteger nonce = new BigInteger("0");

    //f11932i
    private AddressMessage addressMe = null;

    //f11933m
    private AddressMessage addressFrom = null;

    //f11934n
    private String subVersion = null;

    //f11935o
    private long height = 0;

    //f11936p
    private boolean relay = false;

    //f11937q
    private int protocalVersion = 0;

    //f11938r
    private CPubKey pubKey = null;

    //f11939s
    private byte[] dataSig = null;

    public VersionMessage(@NonNull Wallet wallet) {
        super(wallet, "version");
        this.addressMe = new AddressMessage(wallet, new byte[]{Ascii.DEL, 0, 0, 1}, 0);
        this.addressFrom = new AddressMessage(wallet, new byte[]{Ascii.DEL, 0, 0, 1}, 0);
        this.protocalVersion = wallet.getChainParams().protocalVersion;
    }

    public VersionMessage(@NonNull Wallet wallet, int i, int i2, String str, long j, long j2, AddressInfo addressInfo, AddressInfo addressInfo2, @NonNull CPubKey cPubKey, @NonNull byte[] bArr) {
        super(wallet, "version", i, i2);
        Wallet izVar2 = wallet;
        int i3 = i;
        int i4 = i2;
        this.mProtocalVersion = i3;
        this.subVersion = str;
        this.nonce = BigInteger.valueOf(j);
        this.height = j2;
        this.protocalVersion = wallet.getChainParams().protocalVersion;
        this.pubKey = cPubKey;
        this.dataSig = bArr;
        if (addressInfo2 == null) {
            this.addressFrom = new AddressMessage(wallet, new byte[]{Ascii.DEL, 0, 0, 1}, 0);
        } else {
            AddressMessage bcVar = this.addressFrom;
            if (bcVar == null) {
                this.addressFrom = new AddressMessage(wallet, addressInfo2.getInetAddress(), addressInfo2.getPort());
            } else {
                bcVar.mo41277a(addressInfo2.getInetAddress(), addressInfo2.getPort());
            }
        }
        if (addressInfo == null) {
            this.addressMe = new AddressMessage(wallet, new byte[]{Ascii.DEL, 0, 0, 1}, 0);
            return;
        }
        AddressMessage bcVar2 = this.addressMe;
        if (bcVar2 == null) {
            this.addressMe = new AddressMessage(wallet, addressInfo.getInetAddress(), addressInfo.getPort());
        } else {
            bcVar2.mo41277a(addressInfo.getInetAddress(), addressInfo.getPort());
        }
    }

    public void onDecodeSerialData() throws IOException {
        this.nonce = BigInteger.valueOf(0);
        this.addressMe = null;
        this.addressFrom = null;
        super.onDecodeSerialData();
        this.protocalVersion = (int) readUInt32();
        this.serviceInt = readUInt64().longValue();
        this.time = readUInt64().longValue() * 1000;
        this.addressMe = new AddressMessage(this.wallet);
        this.addressMe.mo44394a(getTempInput());
        this.pubKey = new CPubKey();
        this.pubKey.decodeSerialStream((SeriableData) this);
        this.dataSig = readVariableBytes();
        this.addressFrom = new AddressMessage(this.wallet);
        this.addressFrom.mo44394a(getTempInput());
        this.nonce = readUInt64();
        this.subVersion = readVariableString();
        this.height = readUInt32();
        readUInt32();
        if (hasReadableDataLeft()) {
            this.relay = readBoolean();
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T((long) this.protocalVersion);
        streamWriter.writeUInt64(this.serviceInt);
        streamWriter.writeUInt64(this.time / 1000);
        this.addressMe.onEncodeSerialData(streamWriter);
        this.pubKey.serialToStream(streamWriter);
        streamWriter.writeVariableBytes(this.dataSig);
        this.addressFrom.onEncodeSerialData(streamWriter);
        streamWriter.writeUInt64(this.nonce);
        streamWriter.writeVariableString(this.subVersion);
        streamWriter.writeUInt32T(this.height);
        streamWriter.writeUInt32T(0);
        streamWriter.writeBoolean(this.relay);
    }

    /* renamed from: a */
    public long getServiceInt() {
        return this.serviceInt;
    }

    /* renamed from: a */
    public void mo42596a(long j) {
        this.serviceInt = j;
    }

    /* renamed from: b */
    public long getTime() {
        return this.time;
    }

    /* renamed from: d */
    public BigInteger getNonce() {
        return this.nonce;
    }

    /* renamed from: e */
    public AddressMessage mo42599e() {
        return this.addressMe;
    }

    /* renamed from: f */
    public AddressMessage getAddressFrom() {
        return this.addressFrom;
    }

    //mo42601g
    public String getSubVersion() {
        return this.subVersion;
    }

    //mo42602h
    public long getHeight() {
        return this.height;
    }

    /* renamed from: i */
    public boolean mo42603i() {
        return this.relay;
    }

    /* renamed from: c */
    public int getSelfProtocalVersion() {
        return this.protocalVersion;
    }

    //mo42604o
    public CPubKey getPubKey() {
        return this.pubKey;
    }
}

