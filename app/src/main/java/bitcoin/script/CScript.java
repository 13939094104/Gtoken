package bitcoin.script;

import bitcoin.CPubKey;
import com.google.common.base.Preconditions;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.transaction.Script;
import vdsMain.transaction.ScriptChunk;
import com.vc.libcommon.util.Integer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class CScript extends SeriableData {

    //f413a
    public BytesArrayBuffer arrayBuffer = new BytesArrayBuffer(28);

    /* renamed from: bitcoin.script.CScript$a */
    //迭代器
    //C0566a
    public class ScriptByteIterator implements Iterator<Byte> {

        /* renamed from: b */
        //f415b
        private int pIndex = 0;

        /* renamed from: a */
        public ScriptByteIterator clone() {
            return new ScriptByteIterator(this);
        }

        public ScriptByteIterator(ScriptByteIterator aVar) {
            this.pIndex = aVar.pIndex;
        }

        public ScriptByteIterator(int i) {
            this.pIndex = i;
        }

        /* renamed from: a */
        public void mo9544a(ScriptByteIterator scriptByteIterator) {
            this.pIndex = scriptByteIterator.pIndex;
        }

        //mo9546b
        public int getRemainingLength() {
            //return CScript.this.Data.getLength() - this.pIndex;
            return CScript.this.arrayBuffer.getWritePos() - this.pIndex;
        }

        public boolean hasNext() {
            //return this.pIndex < CScript.this.Data.getLength();
            return this.pIndex < CScript.this.arrayBuffer.getWritePos();
        }

        /* renamed from: c */
        public Byte next() {
            CScript cScript = CScript.this;
            int i = this.pIndex;
            this.pIndex = i + 1;
            return Byte.valueOf(cScript.mo9531c(i));
        }

        /* renamed from: a */
        //mo9545a
        public byte[] next(int i) {
            byte[] bArr = new byte[i];
            //CScript.this.Data.writeData(bArr, this.pIndex, i)
            CScript.this.arrayBuffer.mo43645b(bArr, this.pIndex, i);
            this.pIndex += i;
            return bArr;
        }

        /* renamed from: b */
        //mo9547b
        public void skip(int i) {
            this.pIndex += i;
        }

        /* renamed from: d */
        //mo9550d
        public int getPointedIndex() {
            return this.pIndex;
        }

        /* renamed from: e */
        //getDataBytes
        public byte[] mo9551e() {
            //return CScript.this.Data.getBytes();
            return CScript.this.arrayBuffer.getBytes();
        }
    }

    private static native byte[] GetScriptForDestination(int i, byte[] bArr);

    public CScript() {
    }

    public CScript(byte[] bArr) {
        if (bArr != null) {
            this.arrayBuffer.writeAllBytes(bArr);
        }
    }

    public CScript(SeriableData seriableData) {
        super(seriableData);
        initWithSeriableData(seriableData);
    }

    //mo9523a
    public void initWithSeriableData(SeriableData seriableData) {
        this.arrayBuffer.resetArr();
        if (seriableData != null && seriableData != this) {
            if (seriableData instanceof CScript) {
                this.arrayBuffer.writeAllByteArrayBufferToSelf(((CScript) seriableData).arrayBuffer);
            } else if (seriableData instanceof Script) {
                //this.Data.writeBytes(((Script) seriableData).mo43159c());
                this.arrayBuffer.writeAllBytes(((Script) seriableData).getScriptChunksBytes());
            }
        }
    }

    /* renamed from: a */
    //mo9522a
    public CScript writeAllBytes(byte[] bArr) {
        //this.Data.writeBytes(bArr);
        this.arrayBuffer.writeAllBytes(bArr);
        return this;
    }

    /* renamed from: a */
    //mo9521a
    public CScript writeOnByte(long j) {
        this.arrayBuffer.writeOnByte((byte) ((int) j));
        return this;
    }

    /* renamed from: a */
    //getEnd
    public byte mo9519a() {
        //int a = this.Data.getLength();
        int a = this.arrayBuffer.getWritePos();
        if (a == 0) {
            return 0;
        }
        //return this.Data.getBytes()[a - 1];
        return this.arrayBuffer.getBytes()[a - 1];
    }

    /* renamed from: a */
    //https://en.bitcoin.it/wiki/Script
    //mo9520a
    public CScript checkAndAddOpCode(int i) {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalid opcode (%d)", new Object[]{java.lang.Integer.valueOf(i)}));
        }
        //append((long) i);
        writeOnByte((long) i);
        return this;
    }

    /* renamed from: b */
    //appendNumber
    public CScript mo9527b(int i) {
        //appendWord(putNumber(i));
        checkAndAddOpCode(m487e(i));
        return this;
    }

    //mo9529b
    public CScript writeAllDataBytes(byte[] bArr) {
        return writeDataBytes(bArr);
    }

    /* renamed from: c */
    public CScript mo9532c(byte[] bArr) {
        return writeDataBytes(bArr);
    }

    /* renamed from: d */
    //mo9537d
    public CScript writeDataBytes(byte[] bArr) {
        if (bArr.length < 76) {
            //append((long) bArr.length);
            writeOnByte((long) bArr.length);
        } else if (bArr.length <= 255) {
            //append(76);//OP_PUSHDATA1
            //append((long) bArr.length);
            writeOnByte(76);
            writeOnByte((long) bArr.length);
        } else if (bArr.length <= 65535) {
            //append(77);//OP_PUSHDATA2
            writeOnByte(77);
            byte[] bArr2 = new byte[2];
            Utils.m3453b(bArr.length, bArr2, 0);
            //writeBytes(bArr2);
            writeAllBytes(bArr2);
        } else {
            //append(78);//OP_PUSHDATA4
            writeOnByte(78);
            byte[] bArr3 = new byte[4];
            Utils.uint32ToByteArrayLE((long) bArr.length, bArr3, 0);
            //writeBytes(bArr3);
            writeAllBytes(bArr3);
        }
        //this.Data.writeBytes(bArr);
        this.arrayBuffer.writeAllBytes(bArr);
        return this;
    }

    /* renamed from: b */
    //appendLong
    public CScript mo9528b(long j) {
        writeDataBytes(CScriptNum.serialize(j));
        return this;
    }

    /* renamed from: b */
    //mo9530b
    public byte[] copyToNewBytes() {
        return this.arrayBuffer.copyToNewBytes();
    }

    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        //streamWriter.writeVariableInt((long) this.Data.getLength());
        //streamWriter.write(this.Data.getBytes(), 0, this.Data.getLength());
        streamWriter.writeVariableInt((long) this.arrayBuffer.getWritePos());
        streamWriter.write(this.arrayBuffer.getBytes(), 0, this.arrayBuffer.getWritePos());
    }

    public void onDecodeSerialData() {
        //this.Data.writeBytes(readBytes(readVariableInt().mo9482b()));
        this.arrayBuffer.writeAllBytes(readBytes(readVariableInt().getIntValue()));
    }

    /* renamed from: a */
    //mo9526a
    //把操作码写入integer中，把数据写入gdVar中，从第三位开始（第一位为操作码，第二位为数据长度）
    public boolean IsWitnessProgram(Integer integer, BytesArrayBuffer bytesArrayBuffer) {
        //if (getDatalength() < 4 || getDatalength() > 42) {
        if (getArrayBufferWritePos() < 4 || getArrayBufferWritePos() > 42) {
            return false;
        }
        //if ((this.Data.getBytes()[0] != 0 && (this.Data.getBytes()[0] < 81 || this.Data.getBytes()[0] > 96)) || this.Data.getBytes()[1] + 2 != getDatalength()) {
        if ((this.arrayBuffer.getBytes()[0] != 0 && (this.arrayBuffer.getBytes()[0] < 81 || this.arrayBuffer.getBytes()[0] > 96)) || this.arrayBuffer.getBytes()[1] + 2 != getArrayBufferWritePos()) {
            return false;
        }
        //integer.set(OP_WORDtoInt(this.Data.getBytes()[0]));
        integer.set(checkIsOP_N(this.arrayBuffer.getBytes()[0]));
        //gdVar.mo43644a(this.Data.getBytes(), 2, this.Data.getLength()());
        bytesArrayBuffer.writeBytes(this.arrayBuffer.getBytes(), 2, this.arrayBuffer.getWritePos());
        return true;
    }

    /* renamed from: c */
    //mo9533c
    public boolean IsPushOnly() {
        return IsPushOnly(getNewScriptByteIterator());
    }

    /* renamed from: a */
    //mo9524a
    public boolean IsPushOnly(ScriptByteIterator pc) {
        ScriptChunk scriptChunk = new ScriptChunk();
        while (pc.hasNext()) {
            if (!GetOp(pc, scriptChunk, false)) {
                return false;
            }
            if (scriptChunk.opWord > 96) {//说明不是value-pushing words
                return false;
            }
        }
        return true;
    }

    public void clean() {
        //this.Data.clean();
        this.arrayBuffer.resetArr();
    }

    /* renamed from: c */
    //getByteAt(i)
    public byte mo9531c(int i) {
        //if (i < this.Data.getLength()) {
        if (i < this.arrayBuffer.getWritePos()) {
            //return this.arrayBuffer.getBytes()[i];
            return this.arrayBuffer.getBytes()[i];
        }
        throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Index %d of of bounds %d", new Object[]{java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(this.arrayBuffer.getWritePos())}));
    }

    /* renamed from: d */
    //mo9535d
    public ScriptByteIterator getNewScriptByteIterator() {
        return new ScriptByteIterator(0);
    }

    //mo9525a
    //根据传入的迭代器，检查下一个data，z为真时该data存入csVar，
    public boolean GetOp(ScriptByteIterator scriptByteIterator, ScriptChunk scriptChunk, boolean z) {
        int i;
        scriptChunk.opWord = 255;
        scriptChunk.bytes = null;
        if (!scriptByteIterator.hasNext()) {
            return false;
        }
        scriptChunk.opWord = scriptByteIterator.next().intValue() & 0xff;
        /**
         Word	        Opcode	Hex	        Input	    Output	        Description
         OP_0, OP_FALSE	0	    0x00	    Nothing.	(empty value)	An empty array of bytes is pushed onto the stack. (This is not a no-op: an item is added to the stack.)
         N/A	            1-75	0x01-0x4b	(special)	data	        The next opcode bytes is data to be pushed onto the stack
         OP_PUSHDATA1	76	    0x4c	    (special)	data	        The next byte contains the number of bytes to be pushed onto the stack.
         OP_PUSHDATA2	77	    0x4d	    (special)	data	        The next two bytes contain the number of bytes to be pushed onto the stack in little endian order.
         OP_PUSHDATA4	78	    0x4e	    (special)	data	        The next four bytes contain the number of bytes to be pushed onto the stack in little endian order.

         aVar.mo9546b()迭代器剩余长度
         */
        if (scriptChunk.opWord <= 78) {
            if (scriptChunk.opWord < 76) {
                i = scriptChunk.opWord;
            } else if (scriptChunk.opWord == 76) {
                if (scriptByteIterator.getRemainingLength() < 1) {
                    return false;
                }
                i = 255 & scriptByteIterator.next();
            } else if (scriptChunk.opWord == 77) {
                if (scriptByteIterator.getRemainingLength() < 2) {
                    return false;
                }
                i = Utils.m3456c(scriptByteIterator.mo9551e(), scriptByteIterator.getPointedIndex());
                scriptByteIterator.skip(2);
            } else if (scriptChunk.opWord != 78) {
                i = 0;
            } else if (scriptByteIterator.getRemainingLength() < 4) {//csVar.opWord == 78
                return false;
            } else {//csVar.opWord == 78
                i = (int) Utils.m3444a(scriptByteIterator.mo9551e(), scriptByteIterator.getPointedIndex());
                scriptByteIterator.skip(4);
            }
            if (scriptByteIterator.getRemainingLength() < 0 || scriptByteIterator.getRemainingLength() < i) {
                return false;
            }
            if (z) {
                scriptChunk.bytes = scriptByteIterator.next(i);
            } else {
                scriptByteIterator.skip(i);
            }
        }
        return true;
    }

    /* renamed from: a */
    //根据交易中的来源信息实例化脚本
    public static CScript m484a(CTxDestination cTxDestination) {
        if (cTxDestination instanceof SaplingPaymentAddress) {
            return null;
        }
        return new CScript(GetScriptForDestination(cTxDestination.getCTxDestinationType().getValue(), cTxDestination.data()));
    }

    /* renamed from: a */
    //createCHECKSIGScript
    public static CScript m483a(CPubKey cPubKey) {
        if (cPubKey.getTypeLength() == 65) {
            CScript cScript = new CScript();
            cScript.writeDataBytes(cPubKey.getByteArr());
            //cScript.appendWord(172);//OP_CHECKSIG
            cScript.checkAndAddOpCode(172);
            return cScript;
        }
        throw new IllegalArgumentException();
    }

    /* renamed from: b */
    //根据公钥信息实例化脚本
    public static CScript m486b(CPubKey cPubKey) {
        if (cPubKey instanceof CMultisigPubkey) {
            CScript cScript = new CScript(((CMultisigPubkey) cPubKey).getByteArr());
            if (cScript.mo9540f()) {
                return cScript;
            }
            throw new IllegalArgumentException("Pubkey was not MS script");
        }
        throw new IllegalArgumentException("Pubkey was not multisig pubkey");
    }

    /* renamed from: a */
    //createCHECKMULTISIGScript
    public static CScript m482a(int i, List<byte[]> list) {
        if (i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thresholds ");
            sb.append(i);
            sb.append(" must bigger than 0.");
            throw new IllegalArgumentException(sb.toString());
        } else if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Pubkeys is empty");
        } else if (i <= list.size()) {
            CScript cScript = new CScript();
            cScript.mo9536d(i);
            for (byte[] c : list) {
                cScript.mo9532c(c);
            }
            cScript.mo9536d(list.size());
            //cScript.appendWord(174);//OP_CHECKMULTISIG
            cScript.checkAndAddOpCode(174);
            return cScript;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Thresholds ");
            sb2.append(i);
            sb2.append(" must smaller than pubkey size ");
            sb2.append(list.size());
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    //mo9538e
    public boolean IsPayToScriptHash() {
        //if (getDatalength() != 23 || !DataTypeToolkit.m11505b((int) this.Data.getBytes()[0], 169) || !DataTypeToolkit.m11505b((int) this.Data.getBytes()[1], 20) || !DataTypeToolkit.m11505b((int) this.Data.getBytes()[22], 135)) {
        if (getArrayBufferWritePos() != 23 || !DataTypeToolkit.m11505b((int) this.arrayBuffer.getBytes()[0], 169) || !DataTypeToolkit.m11505b((int) this.arrayBuffer.getBytes()[1], 20) || !DataTypeToolkit.m11505b((int) this.arrayBuffer.getBytes()[22], 135)) {
            return false;
        }
        return true;
    }

    /* renamed from: f */
    public boolean mo9540f() {
        return Standard.m533a(this, new Integer(), new Vector());
    }

    /* renamed from: d */
    //appendNumber2
    public CScript mo9536d(int i) {
        //appendWord(putNumber(i));
        checkAndAddOpCode(m487e(i));
        return this;
    }

    /* renamed from: a */
    //m481a
    public static int checkIsOP_N(byte b) {
        if (b == 0) {
            return 0;
        }
        if (b >= 81 && b <= 96) {
            return b - 80;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal OP_N ");
            sb.append(b);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* renamed from: e */
    //putNumber
    /*
            Word	        Opcode	Hex	        Input	    Output	Description
    i==-1:  OP_1NEGATE	    79	    0x4f	    Nothing.	-1	The number -1 is pushed onto the stack.
    i==0:   OP_0, OP_FALSE	0	    0x00	    Nothing.	(empty value)	An empty array of bytes is pushed onto the stack. (This is not a no-op: an item is added to the stack.)
    i==1:   OP_1, OP_TRUE	81	    0x51	    Nothing.	1	The number 1 is pushed onto the stack.
    i==2~16:OP_2-OP_16	    82-96	0x52-0x60	Nothing.	2-16	The number in the word name (2-16) is pushed onto the stack.
    */
    public static int m487e(int i) {
        boolean z = i >= -1 && i <= 16;
        StringBuilder sb = new StringBuilder();
        sb.append("encodeToOpN called for ");
        sb.append(i);
        sb.append(" which we cannot encode in an opcode.");
        Preconditions.checkArgument(z, sb.toString());
        if (i == 0) {
            //OP_0, OP_FALSE
            return 0;
        }
        if (i == -1) {
            //OP_1NEGATE
            return 79;
        }
        return (i - 1) + 81;
    }

    public String toString() {
        return StringToolkit.bytesToString(this.arrayBuffer.copyToNewBytes());
    }

    public String hexString() {
        return StringToolkit.bytesToString(this.arrayBuffer.copyToNewBytes());
    }

    /* renamed from: g */
    //mo9541g
    public int getArrayBufferWritePos() {
        return this.arrayBuffer.getWritePos();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CScript) {
            return this.arrayBuffer.equals(((CScript) obj).arrayBuffer);
        }
        return false;
    }
}

