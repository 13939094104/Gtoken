package vdsMain;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

public final class FileToolkit {


    //m11517a
    //915 m12069a
    public static final String getVaildPath(String str) {
        char[] charArray;
        StringBuffer stringBuffer = new StringBuffer();
        char c = 0;
        for (char c2 : str.toCharArray()) {
            if (c2 != '\\' && c2 != '/') {
                stringBuffer.append(c2);
                c = c2;
            } else if (!(c == 0 || c == '\\' || c == '/')) {
                stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
            }
        }
        int length = stringBuffer.length();
        if (length > 0) {
            length--;
            if (stringBuffer.charAt(length) == '/') {
                stringBuffer.deleteCharAt(length);
                length--;
            }
        }
        if (length < 1) {
            stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
        }
        return stringBuffer.toString();
    }

    //m11519a
    //915 m12071a
    public static final boolean checkDirectory(String str, boolean isRecursive) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            return file.mkdirs();
        }
        if (isRecursive) {
            return recursiveCheckFile(file);
        }
        if (file.isDirectory()) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" is exist, but not a folder");
        throw new IOException(sb.toString());
    }

    /* renamed from: b */
//    public static String m11520b(String str) {
//        if (str.isEmpty()) {
//            return "/";
//        }
//        char[] charArray = str.toCharArray();
//        int length = charArray.length;
//        while (true) {
//            length--;
//            if (length <= -1) {
//                return "/";
//            }
//            if (length == charArray.length - 1 || !(charArray[length] == '/' || charArray[length] == '\\')) {
//            }
//        }
//        return str.substring(0, length);
//    }

    /* renamed from: c */
//    public static boolean m11521c(String str) {
//        File file = new File(m11520b(str));
//        if (file.exists()) {
//            return file.isDirectory();
//        }
//        return file.mkdirs();
//    }

    //m11518a
    public static final boolean recursiveCheckFile(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return true;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                if (!recursiveCheckFile(file2) || !file2.delete()) {
                    return false;
                }
            } else if (!file2.delete()) {
                return false;
            }
        }
        return true;
    }
}
