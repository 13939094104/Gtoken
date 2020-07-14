package com.vtoken.application.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;

//bbr
public class PhotoUtil {

    public static int f9415a = 612;

    //m6977a
    public static String getFilePathByUri(Context context, Uri uri) throws Throwable {
        Uri uri2 = null;
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                return m6978a(context, uri, null, null);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (m6981a(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append("/");
                sb.append(split[1]);
                return sb.toString();
            }
        } else if (m6983b(uri)) {
            return m6978a(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
        } else if (m6984c(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String str = split2[0];
            if ("image".equals(str)) {
                uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(str)) {
                uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(str)) {
                uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return m6978a(context, uri2, "_id=?", new String[]{split2[1]});
        }
        return null;
    }

    public static String m6979a(String str) {
        Bitmap a = m6974a(new File(str), f9415a);
        if (a == null) {
            return null;
        }
        return m6982b(a);
    }

    public static Bitmap m6973a(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel = copy.getPixel(i, i2);
                int i3 = -16777216 & pixel;
                int i4 = 255;
                if (((int) ((((double) ((float) ((16711680 & pixel) >> 16))) * 0.3d) + (((double) ((float) ((65280 & pixel) >> 8))) * 0.59d) + (((double) ((float) (pixel & 255))) * 0.11d))) <= 95) {
                    i4 = 0;
                }
                copy.setPixel(i, i2, (i4 << 16) | i3 | (i4 << 8) | i4);
            }
        }
        return copy;
    }

    private static String m6982b(Bitmap bitmap) {
        Bitmap a = m6973a(bitmap);
        int width = a.getWidth();
        int height = a.getHeight();
        int[] iArr = new int[(width * height)];
        a.getPixels(iArr, 0, width, 0, 0, width, height);
        a.recycle();
        QRCodeReader qRCodeReader = new QRCodeReader();
        EnumMap enumMap = new EnumMap(DecodeHintType.class);
        enumMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        try {
            Result decode = qRCodeReader.decode(new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(width, height, iArr))), enumMap);
            bitmap.recycle();
            return decode.getText();
        } catch (Exception e) {
            e.printStackTrace();
            int width2 = bitmap.getWidth();
            int height2 = bitmap.getHeight();
            int[] iArr2 = new int[(width2 * height2)];
            bitmap.getPixels(iArr2, 0, width2, 0, 0, width2, height2);
            try {
                Result decode2 = qRCodeReader.decode(new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(width2, height2, iArr2))), enumMap);
                bitmap.recycle();
                return decode2.getText();
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    private static int m6972a(int i, int i2) {
        if (i > i2 * 2) {
            int i3 = 0;
            do {
                i3++;
            } while (i / i3 > i2);
            int i4 = 1;
            for (int i5 = 1; i5 <= i3; i5++) {
                if (Math.abs((i / i5) - i2) <= Math.abs((i / i4) - i2)) {
                    i4 = i5;
                }
            }
            return i4;
        } else if (i <= i2) {
            return 1;
        } else {
            return 2;
        }
    }


    public static Bitmap m6974a(File file, int i) {
        if (file != null) {
            try {
                if (file.exists()) {
                    if (file.length() == 0) {
                        file.delete();
                        return null;
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    options.inSampleSize = m6972a(Math.min(options.outHeight, options.outWidth), i);
                    options.inJustDecodeBounds = false;
                    options.inPurgeable = true;
                    options.inInputShareable = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static String m6978a(Context context, Uri uri, String str, String[] strArr) throws Throwable {
        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Throwable th) { ;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th2) {
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th2;
        }
    }

    public static boolean m6981a(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean m6983b(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /* renamed from: c */
    public static boolean m6984c(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void saveBitmapToAlbum(Context context,Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        }else if (Build.BRAND.equals("Huawei")){
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
// 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }
}
