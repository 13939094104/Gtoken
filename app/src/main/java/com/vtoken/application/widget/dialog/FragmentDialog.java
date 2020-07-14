package com.vtoken.application.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.vtoken.application.ApplicationLoader;
import vdsMain.tool.DeviceUtil;


//bbz
public class FragmentDialog extends DialogFragment {

    private View view;


    private Dialog dialog;

    /* renamed from: c */
    private boolean heightMatchParent = false;

    //
    private boolean widthMatchParent = true;

    /* renamed from: e */
    private DialogEvent dialogEvent;

    public int gravity= Gravity.CENTER;

    /* renamed from: BaseDialog$a */
    /* compiled from: FragmentDialog */
    public interface DialogEvent {

        void onCreateDialog(Dialog dialog);

        /* renamed from: a */
        void onDismiss(DialogInterface dialogInterface);


        void onActivityCreated(Dialog dialog);
    }

    public FragmentDialog() {
    }

    @SuppressLint({"ValidFragment"})
    public FragmentDialog(boolean widthMatchParent, boolean heightMatchParent) {
        this.widthMatchParent = widthMatchParent;
        this.heightMatchParent = heightMatchParent;
    }

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        ViewParent parent = this.view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this.view);
        }
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().getWindow().setGravity(gravity);
        return this.view;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.widthMatchParent && this.heightMatchParent) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ApplicationLoader.getSingleApplicationContext().getActivityHeightPixels() - DeviceUtil.dp2px((Context) ApplicationLoader.getSingleApplicationContext(), 20.0f));
        } else if (this.widthMatchParent) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (this.heightMatchParent) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ApplicationLoader.getSingleApplicationContext().getActivityHeightPixels() - DeviceUtil.dp2px((Context) ApplicationLoader.getSingleApplicationContext(), 20.0f));
        } else {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        DialogEvent dialogEvent = this.dialogEvent;
        if (dialogEvent != null) {
            dialogEvent.onActivityCreated(getDialog());
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.setCancelable(isCancelable());
        onCreateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        onCreateDialog.setCanceledOnTouchOutside(isCancelable());
        this.dialog = onCreateDialog;
        DialogEvent dialogEvent = this.dialogEvent;
        if (dialogEvent != null) {
            dialogEvent.onCreateDialog(onCreateDialog);
        }
        return onCreateDialog;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        //LanguageUtil.m6964a(context, LanguageUtil.m6962a());
    }

    //mo41265a
    public boolean getIsShowing() {
        Dialog dialog = this.dialog;
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    //mo41263a
    public void setView(View view) {
        this.view = view;
    }

    @Nullable
    public View getView() {
        return this.view;
    }

    //mo41264a
    public void setDialogEvent(DialogEvent event) {
        this.dialogEvent = event;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogEvent dialogEvent = this.dialogEvent;
        if (dialogEvent != null) {
            dialogEvent.onDismiss(dialogInterface);
        }
    }
}