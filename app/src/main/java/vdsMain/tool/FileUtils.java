package vdsMain.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.widget.Toast;
import com.vtoken.application.ApplicationLoader;

import java.io.File;

//bbj
public class FileUtils {

    //m6947a
    public static Uri getFileUri(File file, Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context, "com.vdser.vdsecology.fileprovider", file);
        }
        return Uri.fromFile(file);
    }

    //m6949a
    @SuppressLint("WrongConstant")
    public static void startFileIntent(String str, Context context) {
        Intent intent;
        File file = new File(str);
        if (file.exists()) {
            Uri a = getFileUri(file, context);
            String lowerCase = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
            if (lowerCase.equals("m4a") || lowerCase.equals("mp3") || lowerCase.equals("mid") || lowerCase.equals("xmf") || lowerCase.equals("ogg") || lowerCase.equals("wav")) {
                intent = getAudioIntent(a);
            } else if (lowerCase.equals("3gp") || lowerCase.equals("mp4")) {
                intent = getAudioIntent(a);
            } else if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg") || lowerCase.equals("bmp")) {
                intent = getImageIntent(a);
            } else if (lowerCase.equals("apk")) {
                intent = getApkIntent(a);
            } else if (lowerCase.equals("ppt")) {
                intent = getPptIntent(a);
            } else if (lowerCase.equals("xls")) {
                intent = getExcelIntent(a);
            } else if (lowerCase.equals("doc")) {
                intent = getWordIntent(a);
            } else if (lowerCase.equals("pdf")) {
                intent = getPdfIntent(a);
            } else if (lowerCase.equals("chm")) {
                intent = getChmIntent(a);
            } else if (lowerCase.equals("txt") || lowerCase.equals("conf")) {
                intent = getTextIntent(str, false, context);
            } else {
                Toast.makeText(context, ApplicationLoader.getSingleApplicationContext().getStringFromLocal("toast_can_not_open_file"), Toast.LENGTH_SHORT).show();
                return;
            }
            intent.addFlags(1);
            context.startActivity(intent);
        }
    }

    @SuppressLint("WrongConstant")
    public static void startFileSendIntent(String str, Context context) {
        Intent intent;
        File file = new File(str);
        if (file.exists()) {
            Uri a = getFileUri(file, context);
            String lowerCase = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
            if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg") || lowerCase.equals("bmp")) {
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, a);
                context.startActivity(Intent.createChooser(intent, "分享"));
                //context.startActivity(intent);
            }
        }
    }

    //m6946a
    @SuppressLint("WrongConstant")
    public static Intent getTextIntent(String str, boolean z, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        if (z) {
            intent.setDataAndType(Uri.parse(str), "text/plain");
        } else {
            intent.setDataAndType(getFileUri(new File(str), context), "text/plain");
        }
        return intent;
    }

    //m6956g
    @SuppressLint("WrongConstant")
    public static Intent getChmIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //m6957h
    @SuppressLint("WrongConstant")
    public static Intent getPdfIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    //m6955f
    @SuppressLint("WrongConstant")
    public static Intent getWordIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //m6954e
    @SuppressLint("WrongConstant")
    public static Intent getExcelIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //m6951b
    @SuppressLint("WrongConstant")
    public static Intent getAudioIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //m6952c
    @SuppressLint("WrongConstant")
    public static Intent getImageIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        //intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.addFlags(1);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //m6945a
    @SuppressLint("WrongConstant")
    public static Intent getApkIntent(Uri uri) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    //m6953d
    @SuppressLint("WrongConstant")
    public static Intent getPptIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435459);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

}
