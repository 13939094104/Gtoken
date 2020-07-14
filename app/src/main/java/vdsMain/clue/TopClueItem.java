package vdsMain.clue;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

//bjy
public class TopClueItem extends SeriableData {

    //f11817a
    public String vAddress = null;

    //f11818b
    public String btcAddress = null;

    //f11819c
    public int inviteNumber = 0;

    //f11820d
    public BigInteger reward = null;

    //f11821e
    public double weight;

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableString(this.vAddress);
        streamWriter.writeVariableString(this.btcAddress);
        streamWriter.writeVariableInt((long) this.inviteNumber);
        streamWriter.writeUInt64(this.reward);
        streamWriter.writeDouble(this.weight);
    }

    public void onDecodeSerialData() throws UnsupportedEncodingException {
        this.vAddress = readVariableString();
        this.btcAddress = readVariableString();
        this.inviteNumber = (int) (readUInt32() & -1);
        this.reward = readUInt64();
        this.weight = readDouble();
    }
}
