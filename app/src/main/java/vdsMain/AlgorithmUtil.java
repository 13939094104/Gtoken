package vdsMain;

import vdsMain.tool.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

//baz
public class AlgorithmUtil {

    public static String m6841a(double d, int i) {
        String a = Util.m7017a(d, i);
        vdsMain.Log.LogDebug("formatDigitStr", a);
        return a;
    }

    /* renamed from: a */
    public static byte[] m6843a(ArrayList<String> arrayList) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(arrayList);
            objectOutputStream.flush();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public static ArrayList<String> m6842a(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            ArrayList<String> arrayList = (ArrayList) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
