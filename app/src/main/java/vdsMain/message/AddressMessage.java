package vdsMain.message;

import androidx.annotation.NonNull;
import generic.io.StreamWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressMessage extends Message {

    /* renamed from: a */
    private long f9436a;

    /* renamed from: b */
    private long f9437b;

    /* renamed from: h */
    private InetAddress f9438h;

    /* renamed from: i */
    private int f9439i;

    public AddressMessage(@NonNull Wallet izVar) {
        super(izVar, "network");
        this.f9436a = System.currentTimeMillis();
        this.f9437b = 0;
        this.f9438h = null;
        this.f9439i = 0;
        this.mProtocalVersion = 0;
    }

    public AddressMessage(@NonNull Wallet izVar, InetAddress inetAddress, int i) {
        super(izVar, "network");
        this.f9436a = System.currentTimeMillis();
        this.f9437b = 0;
        this.f9438h = null;
        this.f9439i = 0;
        this.mProtocalVersion = 0;
        this.f9438h = inetAddress;
        this.f9439i = i;
    }

    public AddressMessage(@NonNull Wallet izVar, byte[] bArr, int i) {
        super(izVar, "network");
        this.f9436a = System.currentTimeMillis();
        this.f9437b = 0;
        this.f9438h = null;
        this.f9439i = 0;
        this.f9439i = i;
        this.mProtocalVersion = 0;
        try {
            this.f9438h = InetAddress.getByAddress(bArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDecodeSerialData() {
        if (this.mProtocalVersion >= 31402) {
            this.f9436a = readUInt32() * 1000;
        }
        this.f9437b = readUInt64().longValue();
        try {
            this.f9438h = InetAddress.getByAddress(readBytes(16));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            this.f9438h = null;
        }
        this.f9439i = readUInt16();
    }

    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        if (this.mProtocalVersion >= 31402) {
            streamWriter.writeUInt32T(this.f9436a / 1000);
        }
        streamWriter.writeUInt64(new BigInteger(Long.toString(this.f9437b), 10));
        byte[] address = this.f9438h.getAddress();
        if (address.length == 4) {
            byte[] bArr = new byte[16];
            System.arraycopy(address, 0, bArr, 12, 4);
            bArr[10] = -1;
            bArr[11] = -1;
            address = bArr;
        }
        streamWriter.write(address);
        streamWriter.writeUInt16(this.f9439i);
    }

    /* renamed from: a */
    public void mo41276a(long j) {
        this.f9436a = j;
    }

    /* renamed from: b */
    public void mo41279b(long j) {
        this.f9437b = j;
    }

    /* renamed from: a */
    public void mo41277a(InetAddress inetAddress, int i) {
        this.f9438h = inetAddress;
        this.f9439i = i;
    }

    /* renamed from: a */
    public InetAddress mo41275a() {
        return this.f9438h;
    }

    /* renamed from: b */
    public int mo41278b() {
        return this.f9439i;
    }

    /* renamed from: c */
    public int getSelfProtocalVersion() {
        return this.mProtocalVersion;
    }
}