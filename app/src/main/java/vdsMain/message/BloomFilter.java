package vdsMain.message;

import androidx.annotation.NonNull;

import com.google.common.base.Objects;
import generic.io.StreamWriter;
import net.bither.bitherj.utils.Utils;
import vdsMain.peer.BloomFilterInterface;
import vdsMain.peer.BloomUpdate;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Arrays;

//bkt
public class BloomFilter extends VMessage implements BloomFilterInterface {

    //f11023a
    private byte[] vData = null;

    //f11857b
    private long nHashFuncs = 0;

    //f11858h
    private long nTweak = 0;

    //f11859i
    private byte nFlags = 0;

    //m9824b
    private static int ROTL32(int x, int r) {
        return (x >>> (32 - r)) | (x << r);
    }

    public BloomFilter(@NonNull Wallet wallet) {
        super(wallet, "bloom");
    }

    public BloomFilter(@NonNull Wallet wallet, int nElements, double nFPRate, long nTweakIn, BloomUpdate bloomUpdate) {
        super(wallet, "bloom");
        double d_elements = (double) nElements;
        int min = Math.min((int) (((-1.0d / Math.pow(Math.log(2.0d), 2.0d)) * d_elements) * Math.log(nFPRate)), 288000) / 8;
        if (min <= 0) {
            min = 1;
        }
        this.vData = new byte[min];
        this.nHashFuncs = (long) Math.min((int) ((((double) (this.vData.length * 8)) / d_elements) * Math.log(2.0d)), 50);
        this.nTweak = nTweakIn;
        this.nFlags = (byte) (bloomUpdate.ordinal() & 255);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bloom Filter of size ");
        sb.append(this.vData.length);
        sb.append(" with ");
        sb.append(this.nHashFuncs);
        sb.append(" hash functions.");
        return sb.toString();
    }

    public void onDecodeSerialData() throws IOException {
        super.onDecodeSerialData();
        this.vData = readByteArray();
        if (((long) this.vData.length) <= 36000) {
            this.nHashFuncs = readUInt32();
            if (this.nHashFuncs <= 50) {
                this.nTweak = readUInt32();
                this.nFlags = readBytes(1)[0];
                return;
            }
            throw new IOException("Bloom filter hash function count out of range");
        }
        throw new IOException("Bloom filter out of size range.");
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onEncodeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableBytes(this.vData);
        streamWriter.writeUInt32T(this.nHashFuncs);
        streamWriter.writeUInt32T(this.nTweak);
        streamWriter.write(this.nFlags);
    }


    //m9823a
    private int hash(int nHashNum, byte[] vDataToHash) {
        int nHashSeed= (int) ((nHashNum* 0xFBA4C795) + this.nTweak);
        return MurmurHash3(nHashSeed,vDataToHash);
    }

    private int MurmurHash3(int nHashSeed, byte[] vDataToHash){
        int length = (vDataToHash.length / 4) * 4;
        int c1=0xcc9e2d51;
        int c2=0x1b873593;
        for (int i = 0; i < length; i += 4) {
            int k1= (((((vDataToHash[i] & 0xff) | ((vDataToHash[i + 1] & 0xff) << 8)) | ((vDataToHash[i + 2] & 0xff) << 16)) | ((vDataToHash[i + 3] & 0xff) << 24)) * c1);
            nHashSeed =(ROTL32((nHashSeed^ (ROTL32(k1, 15) * c2)), 13) * 5) + 0xe6546b64;
        }
        int k1 = 0;
        switch (vDataToHash.length & 3) {
            case 3:
                k1 ^=(((vDataToHash[length + 2] & 0xff) << 16));
            case 2:
                k1 ^= (((vDataToHash[length + 1] & 0xff) << 8));
            case 1:
                k1 ^=vDataToHash[length] & 0xff;
                k1*=c1;
                k1=ROTL32(k1,15);
                k1*=c2;
                nHashSeed^=k1;
                break;
        }
        nHashSeed^=vDataToHash.length;
        nHashSeed^=(nHashSeed>>>16);
        nHashSeed*=0x85ebca6b;
        nHashSeed^=(nHashSeed>>>13);
        nHashSeed*=0xc2b2ae35;
        //nHashSeed^=(nHashSeed>>>16);
        return (int)((((long)(nHashSeed^(nHashSeed>>>16)))&4294967295L)%((long)(vData.length*8)));
    }

    //mo42032a
    public boolean contains(byte[] vKey) {
        for (int i = 0; ((long) i) < this.nHashFuncs; i++) {
            if (!Utils.m3460e(this.vData, hash(i, vKey))) {
                return false;
            }
        }
        return true;
    }

    //mo42033b
    public void insert(byte[] vKey) {
        for (int i = 0; ((long) i) < this.nHashFuncs; i++) {
            Utils.m3461f(this.vData, hash(i, vKey));
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof BloomFilter) {
            BloomFilter bloomFilter = (BloomFilter) obj;
            if (bloomFilter.nHashFuncs == this.nHashFuncs && bloomFilter.nTweak == this.nTweak && Arrays.equals(bloomFilter.vData, this.vData)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.nHashFuncs, this.nTweak, Arrays.hashCode(this.vData));
    }
}
