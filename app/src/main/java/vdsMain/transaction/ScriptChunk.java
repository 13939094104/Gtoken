package vdsMain.transaction;

import bitcoin.script.Standard;
import com.google.common.base.Preconditions;
import net.bither.bitherj.utils.Utils;
import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;
import vdsMain.tool.ScriptOpCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

public class ScriptChunk {

    /* renamed from: a */
    //f12307a
    public int opWord;

    /* renamed from: b */
    //f12308b
    public byte[] bytes;

    public ScriptChunk() {
    }

    public ScriptChunk(ScriptChunk csVar) {
        if (csVar != null) {
            this.opWord = csVar.opWord;
            this.bytes = DataTypeToolkit.bytesCopy(csVar.bytes);
        }
    }

    public ScriptChunk(int i, byte[] bArr) {
        this(i, bArr, -1);
    }

    public ScriptChunk(int i, byte[] bArr, int i2) {
        this.opWord = i;
        this.bytes = bArr;
    }

    /* renamed from: a */
    //mo43183a
    public boolean isNotValuePushingWord() {
        return this.opWord > 78;
    }

    /* renamed from: b */
    //mo43186b
    public boolean isValuePushingWord() {
        return this.opWord <= 96;
    }

    /* renamed from: a */
    //mo43184a
    public boolean isLengthBigger(int i) {
        byte[] bArr = this.bytes;
        if (bArr != null && bArr.length >= i) {
            return true;
        }
        return false;
    }

    /* renamed from: a */
    //m10815a
    public static boolean makeOPResult(Iterator<ScriptChunk> it, OPResult cpVar) {
        if (!it.hasNext()) {
            return false;
        }
        return ((ScriptChunk) it.next()).makeOPResult(cpVar);
    }

    /* JADX WARNING: type inference failed for: r0v9, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r0v10, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v10, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r0v9, types: [byte[]] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* renamed from: a */
    //mo43185a
    public boolean makeOPResult(OPResult opResult) {
        opResult.scriptChunk = this;
        int opWordOrDataLength = this.opWord;
        opResult.opWord = opWordOrDataLength;
        opResult.bytes = null;
        opResult.bytes = null;
        if (opWordOrDataLength <= 78) {
            int srcPos = 4;
            if (opWordOrDataLength < 76) {
                srcPos = 0;
            } else if (opWordOrDataLength == 76) {
                byte[] r0 = this.bytes;
                if (r0==null || r0.length < 1) {
                    return false;
                }
                opWordOrDataLength = r0[0];
                srcPos = 1;
            } else if (opWordOrDataLength == 77) {
                byte[] bArr = this.bytes;
                if (bArr == null || bArr.length < 2) {
                    return false;
                }
                opWordOrDataLength = vdsMain.Utils.m13314b(bArr, 0);
                srcPos = 2;
            } else if (opWordOrDataLength == 78) {
                byte[] bArr2 = this.bytes;
                if (bArr2 == null || bArr2.length < 4) {
                    return false;
                }
                opWordOrDataLength = (int) Utils.m3444a(bArr2, 0);
            } else {
                opWordOrDataLength = 0;
                srcPos = 0;
            }
            if (opWordOrDataLength > 0) {
                byte[] bArr3 = this.bytes;
                if (bArr3 == null || srcPos + opWordOrDataLength > bArr3.length) {
                    return false;
                }
                if (srcPos == 0) {
                    opResult.bytes = bArr3;
                } else {
                    opResult.bytes = new byte[opWordOrDataLength];
                    System.arraycopy(bArr3, srcPos, opResult.bytes, 0, opWordOrDataLength);
                }
            }
        } else {
            opResult.bytes = this.bytes;
        }
        return true;
    }

    /* renamed from: a */
    //mo43182a
    public void writeScriptChunkToSteam(OutputStream outputStream) throws IOException {
        boolean isEmpty = true;
        if (isNotValuePushingWord()) {
            if (this.bytes != null) {
                isEmpty = false;
            }
            Preconditions.checkState(isEmpty);
            outputStream.write(this.opWord);
            return;
        }
        byte[] bArr = this.bytes;
        if (bArr != null) {
            Preconditions.checkNotNull(bArr);
            int opWord = this.opWord;
            if (opWord < 76) {
                outputStream.write(opWord);
            } else if (opWord == 76) {
                if (this.bytes.length > 255) {
                    isEmpty = false;
                }
                Preconditions.checkState(isEmpty);
                outputStream.write(76);
                outputStream.write(this.bytes.length);
            } else if (opWord == 77) {
                if (this.bytes.length > 65535) {
                    isEmpty = false;
                }
                Preconditions.checkState(isEmpty);
                outputStream.write(77);
                outputStream.write(this.bytes.length & 255);
                outputStream.write((this.bytes.length >> 8) & 255);
            } else if (opWord == 78) {
                if (this.bytes.length > 520) {
                    isEmpty = false;
                }
                Preconditions.checkState(isEmpty);
                outputStream.write(78);
                Utils.writeLong((long) this.bytes.length, outputStream);
            } else {
                throw new RuntimeException("Unimplemented");
            }
            outputStream.write(this.bytes);
            return;
        }
        outputStream.write(this.opWord);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNotValuePushingWord()) {
            sb.append(ScriptOpCodes.m10831a(this.opWord));
        } else if (this.bytes != null) {
            sb.append(ScriptOpCodes.m10833b(this.opWord));
            sb.append("[");
            sb.append(StringToolkit.bytesToString(this.bytes));
            sb.append("]");
        } else if (isOpWordEqual79OrZero()) {
            sb.append(Script.decodeOP_N(this.opWord));
        } else {
            sb.append("[error]");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ScriptChunk)) {
            return false;
        }
        ScriptChunk csVar = (ScriptChunk) obj;
        return this.opWord == csVar.opWord && Arrays.equals(this.bytes, csVar.bytes);
    }

    public int hashCode() {
        int i = this.opWord * 31;
        byte[] bArr = this.bytes;
        return i + (bArr != null ? Arrays.hashCode(bArr) : 0);
    }

    //mo43187c
    public boolean opWordIsNA() {
        int i = this.opWord;
        //TODO 先注释掉最后一个
        //return i > 0 && i < 76 && isLengthBigger(i) && vdsMain.Utils.m13316b(this.bytes);
        return i > 0 && i < 76 && isLengthBigger(i);
    }

    /* renamed from: d */
    public boolean mo43188d() {
        boolean z = false;
        if (!isValuePushingWord()) {
            return false;
        }
        byte[] bArr = this.bytes;
        if (bArr == null || bArr.length < 4) {
            return false;
        }
        if (Standard.m532a(bArr[0])) {
            byte[] bArr2 = this.bytes;
            if (Standard.m532a(bArr2[bArr2.length - 2])) {
                byte[] bArr3 = this.bytes;
                if (DataTypeToolkit.m11505b((int) bArr3[bArr3.length - 1], 174)) {
                    z = true;
                }
            }
        }
        return z;
    }

    /* renamed from: e */
    public byte[] mo43189e() {
        if (!opWordIsNA()) {
            return null;
        }
        byte[] bArr = this.bytes;
        int i = this.opWord;
        if (bArr[i - 1] != 1) {
            return bArr;
        }
        byte[] bArr2 = new byte[(i - 1)];
        System.arraycopy(bArr, 0, bArr2, 0, i - 1);
        return bArr2;
    }

    //mo43191f
    public boolean isDataOutput() {
        int i = this.opWord;
        //TODO 先改掉
        //return i > 0 && i <= 78 && isLengthBigger(i) && CPubKey.m417c(this.bytes);
        return i > 0 && i <= 78 && isLengthBigger(i);
    }

    //mo43192g
    public boolean isOpWordEqual20() {
        int i = this.opWord;
        return i == 20 && isLengthBigger(i);
    }

    //mo43193h
    public boolean isOpWordEqual79OrZero() {
        int i = this.opWord;
        if (i == 0) {
            return true;
        }
        if ((i < 81 || i > 96) && this.opWord != 79) {
            return false;
        }
        return true;
    }

    /* renamed from: i */
    //mo43195i
    public int getOP_N() {
        int i = this.opWord;
        if (i == 0) {
            return i;
        }
        if (i < 81 || i > 96) {
            return this.opWord == 79 ? -1 : -2;
        }
        return (i + 1) - 81;
    }

    /* renamed from: j */
    public int mo43196j() {
        int i = 4;
        if (isNotValuePushingWord()) {
            return this.bytes == null ? 0 : 4;
        }
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return 4;
        }
        int i2 = this.opWord;
        if (i2 < 76) {
            if (bArr.length != i2) {
                return 0;
            }
        } else if (i2 == 76) {
            if (bArr.length > 255) {
                return 0;
            }
            i = 8;
        } else if (i2 == 77) {
            if (bArr.length > 65535) {
                return 0;
            }
            i = 12;
        } else if (i2 != 78 || bArr.length > 520) {
            return 0;
        } else {
            i = 8;
        }
        return i + this.bytes.length;
    }

    /* renamed from: k */
    //mo43197k
    public int getBytesLength() {
        byte[] bArr = this.bytes;
        if (bArr == null) {
            return 0;
        }
        return bArr.length;
    }
}
