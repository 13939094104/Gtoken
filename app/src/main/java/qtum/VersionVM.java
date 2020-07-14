package qtum;

import generic.utils.Defines;

public class VersionVM {
    public short flagOptions;
    public byte format;
    public byte rootVM;
    public byte vmVersion;

    public static native VersionVM fromRaw(int i);

    public native int toRaw();

    public VersionVM() {
        if (Defines.isLittleEndial()) {
            this.format = 2;
            this.rootVM = 6;
            return;
        }
        this.rootVM = 6;
        this.format = 2;
    }

    public static VersionVM GetNoExec() {
        VersionVM versionVM = new VersionVM();
        versionVM.flagOptions = 0;
        versionVM.rootVM = 0;
        versionVM.format = 0;
        versionVM.vmVersion = 0;
        return versionVM;
    }

    public static VersionVM GetEVMDefault() {
        VersionVM versionVM = new VersionVM();
        versionVM.flagOptions = 0;
        versionVM.rootVM = 1;
        versionVM.format = 0;
        versionVM.vmVersion = 0;
        return versionVM;
    }
}
