package vdsMain;


import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

//bbo
public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    /* renamed from: a */
    private MediaScannerConnection f9411a;

    /* renamed from: b */
    private File f9412b;

    public MediaScanner(Context context, File file) {
        this.f9412b = file;
        this.f9411a = new MediaScannerConnection(context, this);
        this.f9411a.connect();
    }

    public void onMediaScannerConnected() {
        this.f9411a.scanFile(this.f9412b.getAbsolutePath(), null);
    }

    public void onScanCompleted(String str, Uri uri) {
        this.f9411a.disconnect();
    }
}
