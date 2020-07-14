package com.vtoken.application.widget;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.R;
import com.vtoken.application.model.Word;
import com.vtoken.application.widget.dialog.FragmentDialog;
import vdsMain.tool.DeviceUtil;
import java.util.ArrayList;

public class RadioViewGroup extends ViewGroup {

    //f4452a
    private Integer heightOffset;
    /* access modifiers changed from: private */

    //f4453b
    public Context context;
    /* access modifiers changed from: private */

    //f4454c
    public ArrayList<Word> wordList = new ArrayList<>();
    /* access modifiers changed from: private */

    //f4455d
    public ArrayList<View> selectedViewList = new ArrayList<>();
    /* access modifiers changed from: private */

    //f4456e
    public RadioViewGroupEvent radioViewGroupEvent;

    /* renamed from: f */
    private PopupWindow popupWindow;
    /* access modifiers changed from: private */

    //f4458g
    public TextView curTextView;

    //f4459h
    private FragmentDialog editDialog;

    //C2891a
    public interface RadioViewGroupEvent {
        //mo39728a
        void onDeleteWord(int i);

        //mo39729a
        void onNewWordConfirm(int i, String str);

        //mo39730a
        void onWordClicked(String str, boolean isSelected);
    }

    public RadioViewGroup(Context context) {
        super(context);
    }

    public RadioViewGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        m4579b();
    }

    public RadioViewGroup(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* renamed from: b */
    private void m4579b() {
        this.heightOffset = Integer.valueOf(DeviceUtil.dp2px(this.context, 10.0f));
        initPopupWindow();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < this.wordList.size(); i5++) {
            TextView textView = (TextView) getChildAt(i5);
            measureChild(textView, i, i2);
            if (i5 == 0) {
                i3 = textView.getMeasuredHeight();
            } else {
                i4 = i4 + getChildAt(i5 - 1).getMeasuredWidth() + DeviceUtil.dp2px(this.context, 5.0f);
                if (textView.getMeasuredWidth() + i4 > getMeasuredWidth()) {
                    i3 = i3 + textView.getMeasuredHeight() + this.heightOffset.intValue();
                    i4 = 0;
                }
            }
        }
        setMeasuredDimension(getMeasuredWidth(), i3);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (i7 < this.wordList.size()) {
            View childAt = getChildAt(i7);
            if (i7 == 0) {
                i5 = childAt.getMeasuredWidth();
                i6 = childAt.getMeasuredHeight();
            } else {
                i8 = i8 + getChildAt(i7 - 1).getMeasuredWidth() + DeviceUtil.dp2px(this.context, 5.0f);
                int measuredWidth = childAt.getMeasuredWidth() + i8;
                if (measuredWidth > getMeasuredWidth()) {
                    i5 = childAt.getMeasuredWidth();
                    i9 = i9 + childAt.getMeasuredHeight() + this.heightOffset.intValue();
                    i6 = childAt.getMeasuredHeight() + i9;
                    i8 = 0;
                } else {
                    i6 = i10;
                    i5 = measuredWidth;
                }
            }
            getChildAt(i7).layout(i8, i9, i5, i6);
            i7++;
            i10 = i6;
        }
    }

    public void setWordList(ArrayList<Word> arrayList) {
        this.wordList = arrayList;
        for (int i = 0; i < arrayList.size(); i++) {
            final String word = ((Word) arrayList.get(i)).getWord();
            final TextView wordView = getNewWordView(word);
            wordView.setTextColor(getResources().getColor(R.color.word));
            wordView.setTag(word);
            wordView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    wordView.setVisibility(GONE);
                    TextView textView = wordView;
                    textView.setSelected(!textView.isSelected());
                    if (wordView.isSelected()) {
                        RadioViewGroup.this.selectedViewList.add(wordView);
                    }
                    RadioViewGroup.this.radioViewGroupEvent.onWordClicked(word, wordView.isSelected());
                }
            });
            addView(wordView);
        }
    }


    public void revokeOne(){
        int lastIndex=this.wordList.size()-1;
        if(lastIndex<=-1){
            return;
        }
        this.wordList.remove(lastIndex);
        removeView(getChildAt(lastIndex));
        radioViewGroupEvent.onDeleteWord(lastIndex);
    }

    //m4581c
    private void initPopupWindow() {
        View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.pop_up_recovery_click, null, false);
        this.popupWindow = new PopupWindow(inflate, -2, -2, true);
        TextView textView = (TextView) inflate.findViewById(R.id.delete_text);
        TextView textView2 = (TextView) inflate.findViewById(R.id.replace_text);
        TextView textView3 = (TextView) inflate.findViewById(R.id.cancel_text);
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int indexOf = RadioViewGroup.this.wordList.indexOf(new Word(RadioViewGroup.this.curTextView.getText().toString()));
                RadioViewGroup.this.wordList.remove(indexOf);
                RadioViewGroup radioViewGroup = RadioViewGroup.this;
                radioViewGroup.removeView(radioViewGroup.getChildAt(indexOf));
                RadioViewGroup.this.dismissPopupWindow();
                RadioViewGroup.this.radioViewGroupEvent.onDeleteWord(indexOf);
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RadioViewGroup.this.showEditDialog("ReplaceWordDialog", false);
                RadioViewGroup.this.dismissPopupWindow();
            }
        });
        textView3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RadioViewGroup.this.dismissPopupWindow();
            }
        });
    }

    //mo40237a
    public void addNewWord(String str) {
        this.wordList.add(new Word(str));
        final TextView newWordView = getNewWordView(str);
        newWordView.setOnClickListener(view -> RadioViewGroup.this.showPopupWindow(newWordView));
        addView(newWordView);
    }

    //mo40235a
    public void clearWords() {
        this.wordList.clear();
        removeAllViews();
    }

    /* access modifiers changed from: private */
    //m4573a
    public void showPopupWindow(TextView textView) {
        this.curTextView = textView;
        this.popupWindow.showAsDropDown(textView, DeviceUtil.dp2px(getContext(), -10.0f), DeviceUtil.dp2px(getContext(), -5.0f), 5);
    }

    /* access modifiers changed from: private */
    //m4583d
    public void dismissPopupWindow() {
        PopupWindow popupWindow = this.popupWindow;
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }


    /* access modifiers changed from: private */
    //m4574a
    public void showEditDialog(String str, boolean cancelable) {
        this.editDialog = new FragmentDialog();
        View inflate = LayoutInflater.from(ApplicationLoader.getSingleApplicationContext()).inflate(R.layout.dialog_edit, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.edit_text);
        TextView textView = (TextView) inflate.findViewById(R.id.cancel_text);
        ((TextView) inflate.findViewById(R.id.confirm_text)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim = editText.getText().toString().trim();
                if (trim == null || "".equals(trim)) {
                    Toast makeText = Toast.makeText(RadioViewGroup.this.context, ApplicationLoader.getSingleApplicationContext().getStringFromLocal("toast_intro_recovery_no_word"), Gravity.AXIS_X_SHIFT);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                    return;
                }
                int indexOf = RadioViewGroup.this.wordList.indexOf(new Word(RadioViewGroup.this.curTextView.getText().toString()));
                ((TextView) RadioViewGroup.this.getChildAt(indexOf)).setText(trim);
                ((Word) RadioViewGroup.this.wordList.get(indexOf)).setWord(trim);
                RadioViewGroup.this.dismissEditDialog();
                RadioViewGroup.this.radioViewGroupEvent.onNewWordConfirm(indexOf, trim);
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RadioViewGroup.this.dismissEditDialog();
            }
        });
        this.editDialog.setView(inflate);
        this.editDialog.setCancelable(cancelable);
        this.editDialog.show(((Activity) this.context).getFragmentManager(), str);
    }

    /* access modifiers changed from: private */
    //m4584e
    public void dismissEditDialog() {
        FragmentDialog fragmentDialog = this.editDialog;
        if (fragmentDialog != null) {
            fragmentDialog.dismiss();
        }
    }

    //m4577b
    private TextView getNewWordView(String str) {
        TextView textView = new TextView(getContext());
        textView.setBackgroundResource(R.drawable.word_radius);
        textView.setPadding(DeviceUtil.dp2px(this.context, 16.0f), DeviceUtil.dp2px(this.context, 5.0f), DeviceUtil.dp2px(this.context, 15.0f), DeviceUtil.dp2px(this.context, 6.5f));
        textView.setText(str);
        textView.setTag(str);
        textView.setTextColor(ContextCompat.getColor(this.context, R.color.word));
        textView.setTextSize(14.0f);
        return textView;
    }

    public void setOnRadioViewGroupSelectListener(RadioViewGroupEvent aVar) {
        this.radioViewGroupEvent = aVar;
    }

    /* renamed from: a */
    public void mo40236a(int i) {
        removeViewAt(i);
        this.wordList.remove(i);
    }

    public ArrayList<Word> getWordList() {
        return this.wordList;
    }

    public ArrayList<View> getSelectedViewList() {
        return this.selectedViewList;
    }
}
