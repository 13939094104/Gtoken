package generic.network;

import vdsMain.DataTypeToolkit;
import vdsMain.StringToolkit;

import java.net.InetAddress;

public class AddressInfo {

    //f694a
    private InetAddress inetAddress;

    //f695b
    private int port;

    /* renamed from: c */
    private String f696c;

    public AddressInfo(InetAddress inetAddress, int i) {
        this.inetAddress = inetAddress;
        this.port = i;
    }

    //mo19002a
    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    //mo19004b
    public int getPort() {
        return this.port;
    }

    /* renamed from: c */
    public String mo19005c() {
        return this.f696c;
    }

    /* renamed from: a */
    public void mo19003a(String str) {
        this.f696c = str;
    }

    public String toString() {
        if (this.inetAddress != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.inetAddress.getHostAddress());
            sb.append(":");
            sb.append(this.port);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("0.0.0.0:");
        sb2.append(this.port);
        return sb2.toString();
    }

    /* renamed from: d */
    public boolean mo19006d() {
        if (!this.inetAddress.isMulticastAddress() && !this.inetAddress.isAnyLocalAddress() && !this.inetAddress.isLoopbackAddress() && !this.inetAddress.isLinkLocalAddress() && !this.inetAddress.isSiteLocalAddress() && !this.inetAddress.isMCGlobal() && !this.inetAddress.isMCNodeLocal() && !this.inetAddress.isMCLinkLocal() && !this.inetAddress.isMCSiteLocal() && !this.inetAddress.isMCOrgLocal() && !this.inetAddress.getHostAddress().contains("255")) {
            return true;
        }
        return false;
    }

    public String getKeyHex() {
        byte[] address = this.inetAddress.getAddress();
        byte[] bArr = new byte[4];
        DataTypeToolkit.IntegerToBytes(this.port, bArr);
        return StringToolkit.bytesToString(DataTypeToolkit.mergeArr(bArr, address));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AddressInfo)) {
            return false;
        }
        AddressInfo addressInfo = (AddressInfo) obj;
        if (!this.inetAddress.equals(addressInfo.inetAddress) || this.port != addressInfo.port) {
            return false;
        }
        return true;
    }
}