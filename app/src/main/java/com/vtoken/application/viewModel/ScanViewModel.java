package com.vtoken.application.viewModel;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableField;
import android.provider.MediaStore;
import vdsMain.Constants;

//bcu
public class ScanViewModel extends BaseViewModel {

    /* renamed from: x */
    public ObservableField<String> f9691x = new ObservableField<>();

    public ScanViewModel(Context context) {
        super(context);
        if (getActivity().getIntent().hasExtra("postion") && getActivity().getIntent().hasExtra("size")) {
            String stringExtra = getActivity().getIntent().getStringExtra("postion");
            String stringExtra2 = getActivity().getIntent().getStringExtra("size");
            this.f9691x.set(String.format(getStringRescourcesByResName("scan_page_str"), new Object[]{stringExtra, stringExtra2}));
        }
    }

    //mo39924a
    public void back() {
        finish();
    }

    //mo39929b
    public void startPickPhoto() {
        startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), getStringRescourcesByResName("scan_open_album")), Constants.f274f.intValue());
    }
}
