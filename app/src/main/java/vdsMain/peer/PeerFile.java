package vdsMain.peer;

import android.text.TextUtils;
import generic.network.AddressInfo;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeerFile {

    //f13288a
    private File mFile;

    //f13289b
    private Map<String, AddressInfo> stringAddressInfoMap = new HashMap();

    public PeerFile(String str) {
        this.mFile = new File(str);
        try {
            m13094a(this.mFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //mo44598a
    public List<AddressInfo> getAddressInfoList() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.stringAddressInfoMap.values());
        return arrayList;
    }

    //mo44599a
    public void addAddressInfo(AddressInfo addressInfo) {
        if (!this.mFile.exists()) {
            this.stringAddressInfoMap.clear();
        }
        if (!this.stringAddressInfoMap.containsKey(addressInfo.getKeyHex())) {
            try {
                writeAddressInfoToFile(this.mFile, addressInfo);
                this.stringAddressInfoMap.put(addressInfo.getKeyHex(), addressInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: a */
    public boolean mo44600a(String str) {
        return this.stringAddressInfoMap.containsKey(str);
    }

    /* renamed from: a */
    private void m13094a(File file) throws IOException {
        if (file.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                try {
                    String[] split = readLine.split("\\s+");
                    AddressInfo addressInfo = new AddressInfo(InetAddress.getByName(split[0]), Integer.valueOf(split[1]).intValue());
                    if (split.length == 3) {
                        addressInfo.mo19003a(split[2]);
                    }
                    this.stringAddressInfoMap.put(addressInfo.getKeyHex(), addressInfo);
                } catch (Exception unused) {
                } catch (Throwable th) {
                    bufferedReader.readLine();
                    throw th;
                }
            }
            bufferedReader.close();
            if (this.stringAddressInfoMap.size() > 10) {
                file.delete();
                return;
            }
            return;
        }
        file.createNewFile();
    }

    //m13095a
    private void writeAddressInfoToFile(File file, AddressInfo addressInfo) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
        bufferedWriter.write(addressInfo.getInetAddress().getHostAddress());
        bufferedWriter.write(" ");
        bufferedWriter.write(String.valueOf(addressInfo.getPort()));
        if (!TextUtils.isEmpty(addressInfo.mo19005c())) {
            bufferedWriter.write(" ");
            bufferedWriter.write(addressInfo.mo19005c());
        }
        bufferedWriter.write(IOUtils.LINE_SEPARATOR_UNIX);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
