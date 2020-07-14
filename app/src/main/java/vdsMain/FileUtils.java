package vdsMain;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//acg
public class FileUtils {
    /* renamed from: a */
    public static boolean m193a(String str, String str2) {
        String[] list;
        File file;
        try {
            File file2 = new File(str2);
            if (file2.exists() || file2.mkdirs()) {
                for (String str3 : new File(str).list()) {
                    if (str.endsWith(File.separator)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(str3);
                        file = new File(sb.toString());
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(File.separator);
                        sb2.append(str3);
                        file = new File(sb2.toString());
                    }
                    if (file.isDirectory()) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        sb3.append("/");
                        sb3.append(str3);
                        String sb4 = sb3.toString();
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str2);
                        sb5.append("/");
                        sb5.append(str3);
                        m193a(sb4, sb5.toString());
                    } else if (!file.exists()) {
                        android.util.Log.i("--Method--", "copyFolder:  oldFile not exist.");
                        return false;
                    } else if (!file.isFile()) {
                        android.util.Log.i("--Method--", "copyFolder:  oldFile not file.");
                        return false;
                    } else if (!file.canRead()) {
                        android.util.Log.i("--Method--", "copyFolder:  oldFile cannot read.");
                        return false;
                    } else {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(str2);
                        sb6.append("/");
                        sb6.append(file.getName());
                        FileOutputStream fileOutputStream = new FileOutputStream(sb6.toString());
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileInputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
                return true;
            }
            Log.e("--Method--", "copyFolder: cannot create directory.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //m192a
    public static boolean isFileExist(String str) {
        File file = new File(str);
        if (!file.exists() || !file.isFile() || !file.delete()) {
            return false;
        }
        return true;
    }

    //915 m195b
    //m194b
    public static boolean checkAndDeleteDirectory(String path) {
        if (!path.endsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(path);
            sb.append(File.separator);
            path = sb.toString();
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        File[] listFiles = file.listFiles();
        boolean isExist = true;
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isFile()) {
                isExist = isFileExist(listFiles[i].getAbsolutePath());
                if (!isExist) {
                    break;
                }
            } else if (listFiles[i].isDirectory()) {
                isExist = checkAndDeleteDirectory(listFiles[i].getAbsolutePath());
                if (!isExist) {
                    break;
                }
            }
        }
        if (isExist && file.delete()) {
            return true;
        }
        return false;
    }
}
