package vdsMain.transaction;

import bitcoin.UInt256;
import bitcoin.VariableInteger;
import generic.keyid.CTxDestinationFactory;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.CTxDestination;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CHashWriter extends StreamWriter {

    //f13144a
    private CHash256 ctx = new CHash256();

    //f13145b
    private int nType;

    //f13146c
    private int nVersion;

    public CHashWriter() {
    }

    public CHashWriter(int nTypeIn, int nVersionIn) {
        this.nType = nTypeIn;
        this.nVersion = nVersionIn;
    }

    //mo44359a
    public CHashWriter writeString(String str) {
        this.ctx.writeAllBytes(new VariableInteger(str.length()).encodeNativeValue());
        try {
            this.ctx.writeAllBytes(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void writeBytes(byte[] bArr, int start, int length) {
        this.ctx.mo44126a(bArr, start, length);
    }

    public void writeObject(StreamWriter streamWriter) throws IOException {
        if (streamWriter instanceof CTxDestination) {
            CTxDestinationFactory.m914a((CTxDestination) streamWriter, (StreamWriter) this);
        } else if (streamWriter instanceof SeriableData) {
            ((SeriableData) streamWriter).serialToStream((StreamWriter) this);
        } else {
            throw new IOException("Data was not instance of HashSerializedInterface");
        }
    }

    //mo44358a
    public CHashWriter writeToSteamWriter(SeriableData seriableData) {
        try {
            seriableData.serialToStream((StreamWriter) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    //mo44357a
    public UInt256 GetHash() {
        UInt256 result = new UInt256();
        this.ctx.Finalize(result);
        return result;
    }
}