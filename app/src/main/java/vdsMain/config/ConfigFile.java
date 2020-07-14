package vdsMain.config;

import java.io.*;
import java.util.*;

public class ConfigFile {
    //f12717a
    //915 f13365a
    private HashMap<String, String> configMap = new HashMap<>();

    //m11358a
    private boolean isBlank(char c) {
        return (c == ' ' || c == 13) ? false : true;
    }

    public ConfigFile() {
    }

    public ConfigFile(String str) {
        excuteConfigFile(new File(str));
    }

    //mo43555a
    //915 mo43858a
    public void excuteConfigFile(File file) {
        this.configMap.clear();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                checkAndAddToMap(subNotBlankStr(readLine));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //915 mo43856a
    public String getValueFromMap(String str) {
        return (String) this.configMap.get(str);
    }

    /* renamed from: a */
    public String getStringValue(String str, String str2) {
        String str3 = (String) this.configMap.get(str);
        return (str3 == null || str3.isEmpty()) ? str2 : str3;
    }

    //mo43556a
    //915 mo43859a
    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = (String) this.configMap.get(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        String lowerCase = value.toLowerCase(Locale.getDefault());
        if (lowerCase.equals("0") || lowerCase.equals("false")) {
            return false;
        }
        if (lowerCase.equals("1") || lowerCase.equals("true")) {
            return true;
        }
        return defaultValue;
    }

    //mo43551a
    public int getIntergerValue(String str, int defaultValue) {
        String str2 = (String) this.configMap.get(str);
        return (str2 == null || str2.isEmpty()) ? defaultValue : Integer.parseInt(str2);
    }

    //mo43552a
    public long getLongValue(String str, long j) {
        String str2 = (String) this.configMap.get(str);
        return (str2 == null || str2.isEmpty()) ? j : Long.parseLong(str2);
    }

    //m11359c
    private void checkAndAddToMap(String str) {
        if (!str.isEmpty() && str.charAt(0) != '#') {
            int indexOf = str.indexOf("=");
            if (indexOf != -1) {
                String key = str.substring(0, indexOf).trim();
                String value = getFirstBlankToLastBlank(str.substring(indexOf + 1));
                if (!key.isEmpty()) {
                    String trimValue = getFirstBlankToLastBlank(value);
                    if (trimValue.isEmpty()) {
                        this.configMap.remove(key);
                    } else {
                        this.configMap.put(key, trimValue);
                    }
                }
            }
        }
    }

    //截取正数第一个空格到倒数第一个空格间的字符串
    //m11360d
    private String getFirstBlankToLastBlank(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return "";
        }
        int i = length - 1;
        int i2 = 0;
        int i3 = 0;

        while (true) {
            if (i2 >= length) {
                i2 = i3;
                break;
            } else if (isBlank(str.charAt(i2))) {
                break;
            } else {
                i3 = i2 + 1;
                i2 = i3;
            }
        }
        //i2为第一个空的位置或者末尾
        int i4 = i;
        int i5 = i4;
        while (true) {
            if (i4 <= -1) {
                break;
            } else if (isBlank(str.charAt(i4))) {
                i5 = i4 + 1;
                break;
            } else {
                i5 = i4 - 1;
                i4--;
            }
        }
        //i5为倒数第一个空后一位或者最前面
        if (i2 >= length) {
            return "";
        }
        if (i5 <= 0) {
            return "";
        }
        if (i2 >= i5) {
            return "";
        }
        return (i2 == 0 && i5 == i) ? str : str.substring(i2, i5);
    }

    //m11361e
    private String subNotBlankStr(String str) {
        int length = str.length();
        int i = 0;
        while (i < length) {
            if (!isBlank(str.charAt(i))) {
                i++;
            } else if (i == 0) {
                return str;
            } else {
                return str.substring(i);
            }
        }
        return "";
    }

    //mo43554a
    public List<String[]> getKeyValueList(String str) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : this.configMap.entrySet()) {
            if (((String) entry.getKey()).contains(str)) {
                arrayList.add(new String[]{(String) entry.getKey(), (String) entry.getValue()});
            }
        }
        return arrayList;
    }

    //mo43557b
    public HashMap<String, String[]> splitAndAddAddressToMap(String str) {
        List<String[]> a = getKeyValueList(str);
        if (a == null) {
            return null;
        }
        HashMap<String, String[]> hashMap = new HashMap<>();
        for (String[] strArr : a) {
            String[] f = splitAddressToList(strArr[1]);
            if (strArr != null) {
                hashMap.put(strArr[0], f);
            }
        }
        return hashMap;
    }

    //m11362f
    private String[] splitAddressToList(String str) {
        if (str != null && str.length() >= 2) {
            if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
                String[] split = str.substring(1, str.length() - 1).split(",");
                for (int i = 0; i < split.length; i++) {
                    split[i] = getFirstBlankToLastBlank(split[i]);
                }
                return split;
            }
        }
        return null;
    }

    public String toString() {
        return this.configMap.toString();
    }
}
