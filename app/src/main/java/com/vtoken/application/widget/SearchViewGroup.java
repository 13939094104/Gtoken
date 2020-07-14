package com.vtoken.application.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.vtoken.application.R;
import com.vtoken.application.model.Word;
import com.vtoken.application.widget.dialog.FragmentDialog;

import java.util.ArrayList;

import vdsMain.tool.DeviceUtil;

public class SearchViewGroup extends ViewGroup {

    private Integer heightOffset;

    public Context context;


    public ArrayList<Word> wordList = new ArrayList<>();

    public ArrayList<View> selectedViewList = new ArrayList<>();

    public SearchViewGroupEvent radioViewGroupEvent;

    private PopupWindow popupWindow;

    public TextView curTextView;

    private FragmentDialog editDialog;

    private Drawable backDrawable;

    private int backResourceId;

    public interface SearchViewGroupEvent {

        void onWordClicked(String str);
    }

    public SearchViewGroup(Context context) {
        super(context);
    }

    public SearchViewGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SearchViewGroup);
        backDrawable = typedArray.getDrawable(R.styleable.SearchViewGroup_background);
        backResourceId=typedArray.getResourceId(R.styleable.SearchViewGroup_background,0);
        typedArray.recycle();
        init();
    }

    public SearchViewGroup(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* renamed from: b */
    private void init() {
        this.heightOffset = Integer.valueOf(DeviceUtil.dp2px(this.context, 10.0f));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int firstMeasureHeight = 0;
        int lastSumMeasureWidth = 0;
        for (int index = 0; index < this.wordList.size(); index++) {
            TextView textView = (TextView) getChildAt(index);
            measureChild(textView, widthMeasureSpec, heightMeasureSpec);
            if (index == 0) {
                firstMeasureHeight = textView.getMeasuredHeight();
            } else {
                lastSumMeasureWidth = lastSumMeasureWidth + getChildAt(index - 1).getMeasuredWidth() + DeviceUtil.dp2px(this.context, 10.0f);
                if (textView.getMeasuredWidth() + lastSumMeasureWidth > getMeasuredWidth()) {
                    firstMeasureHeight = firstMeasureHeight + textView.getMeasuredHeight() + this.heightOffset.intValue();
                    lastSumMeasureWidth = 0;
                }
            }
        }
        setMeasuredDimension(getMeasuredWidth(), firstMeasureHeight);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int curMeasuredWith;
        int curMeasuredHeight;
        int index = 0;
        int lastSumMeasuredWidth = 0;
        int nextMeasuredHeight = 0;
        int tempMeasuredHeight = 0;
        while (index < this.wordList.size()) {
            View child = getChildAt(index);
            if (index == 0) {
                curMeasuredWith = child.getMeasuredWidth();
                curMeasuredHeight = child.getMeasuredHeight();
            } else {
                lastSumMeasuredWidth = lastSumMeasuredWidth + getChildAt(index - 1).getMeasuredWidth() + DeviceUtil.dp2px(this.context, 10.0f);
                int measuredWidth = child.getMeasuredWidth() + lastSumMeasuredWidth;
                if (measuredWidth > getMeasuredWidth()) {
                    curMeasuredWith = child.getMeasuredWidth();
                    nextMeasuredHeight = nextMeasuredHeight + child.getMeasuredHeight() + this.heightOffset.intValue();
                    curMeasuredHeight = child.getMeasuredHeight() + nextMeasuredHeight;
                    lastSumMeasuredWidth = 0;
                } else {
                    curMeasuredHeight = tempMeasuredHeight;
                    curMeasuredWith = measuredWidth;
                }
            }
            getChildAt(index).layout(lastSumMeasuredWidth, nextMeasuredHeight, curMeasuredWith, curMeasuredHeight);
            index++;
            tempMeasuredHeight = curMeasuredHeight;
        }
    }

    public void setWordList(ArrayList<Word> arrayList) {
        this.wordList = arrayList;
        for (int i = 0; i < arrayList.size(); i++) {
            final String word = ((Word) arrayList.get(i)).getWord();
            final TextView wordView = getNewWordView(word);
            wordView.setTag(word);
            wordView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SearchViewGroup.this.radioViewGroupEvent.onWordClicked(word);
                }
            });
            addView(wordView);
        }
    }




    //mo40237a
    public void addNewWord(String str) {
        this.wordList.add(new Word(str));
        final TextView newWordView = getNewWordView(str);
        newWordView.setOnClickListener(view -> this.radioViewGroupEvent.onWordClicked(newWordView.getText().toString()));
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
        textView.setBackgroundResource(backResourceId);
        textView.setPadding(DeviceUtil.dp2px(this.context, 24.0f), DeviceUtil.dp2px(this.context, 12.0f), DeviceUtil.dp2px(this.context, 24.0f), DeviceUtil.dp2px(this.context, 12f));
        textView.setText(str);
        textView.setTag(str);
        textView.setTextColor(ContextCompat.getColor(this.context, R.color.resync_back_selected));
        textView.setTextSize(14.0f);
        textView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return textView;
    }

    public void setOnRadioViewGroupSelectListener(SearchViewGroupEvent aVar) {
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
