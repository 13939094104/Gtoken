package com.vtoken.application.widget;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.vtoken.application.R;
import vdsMain.Constants;
import vdsMain.tool.DeviceUtil;

public class ScrapeView extends View {

    //f4510a
    int backWidth;

    //f4511b
    int backHeight;

    //f4512c
    boolean isFinish;

    //f4513d
    boolean isInit;

    //f4514e
    private Paint paint;

    //f4515f
    private Path path;
    /* access modifiers changed from: private */

    //f4516g
    public ScrapeViewEvent scrapeViewEvent;

    //f4517h
    private Bitmap backBitmap;
    /* access modifiers changed from: private */

    //f4518i
    public Bitmap pathBitmap;

    //f4519j
    private Canvas canvas;

    //f4520k
    private Runnable runnable = new Runnable() {

        //f4522b
        private int[] pixelArr;

        public void run() {
            float area = (float) (ScrapeView.this.backWidth * ScrapeView.this.backHeight);
            this.pixelArr = new int[(ScrapeView.this.backWidth * ScrapeView.this.backHeight)];
            ScrapeView.this.pathBitmap.getPixels(this.pixelArr, 0, ScrapeView.this.backWidth, 0, 0, ScrapeView.this.backWidth, ScrapeView.this.backHeight);
            int i = 0;
            float blankArea = 0.0f;
            while (i < ScrapeView.this.backWidth) {
                float tempNumber = blankArea;
                for (int j = 0; j < ScrapeView.this.backHeight; j++) {
                    if (this.pixelArr[(ScrapeView.this.backWidth * j) + i] == 0) {
                        tempNumber += 1.0f;
                    }
                }
                i++;
                blankArea = tempNumber;
            }
            if (blankArea > 0.0f && area > 0.0f && ((int) ((blankArea * 100.0f) / area)) >= 50) {
                ScrapeView scrapeView = ScrapeView.this;
                scrapeView.isFinish = true;
                if (scrapeView.scrapeViewEvent != null) {
                    ScrapeView.this.scrapeViewEvent.onScrapeFinish();
                }
                ScrapeView.this.postInvalidate();
            }
        }
    };

    //C2893a
    public interface ScrapeViewEvent {

        //mo39739a
        void onScrapeFinish();
    }

    public ScrapeView(Context context) {
        super(context);
    }

    public ScrapeView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ScrapeView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    //m4601a
    private void init() {
        this.paint = new Paint();
        this.paint.setAlpha(0);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeWidth((float) DeviceUtil.dp2px(getContext(), (float) Constants.scrapeWidth));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.path = new Path();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.path.reset();
                this.path.moveTo(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (this.isInit) {
                    new Thread(this.runnable).start();
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.path.lineTo(motionEvent.getX(), motionEvent.getY());
                break;
        }
        this.canvas.drawPath(this.path, this.paint);
        invalidate();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.pathBitmap, 0.0f, 0.0f, null);
        if (this.isFinish) {
            setVisibility(GONE);
            ScrapeViewEvent aVar = this.scrapeViewEvent;
            if (aVar != null) {
                aVar.onScrapeFinish();
            }
        }
    }

    public ScrapeViewEvent getOnScrapeListener() {
        return this.scrapeViewEvent;
    }

    public void setOnScrapeListener(ScrapeViewEvent scrapeViewEvent) {
        this.scrapeViewEvent = scrapeViewEvent;
    }

    //mo40255a
    public void initBitmap(int width, int height) {
        if (!this.isInit) {
            this.backWidth = width;
            this.backHeight = height;
            this.backBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.scrape), width, height, true);
            this.pathBitmap = Bitmap.createBitmap(this.backBitmap.getWidth(), this.backBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(this.pathBitmap);
            this.canvas.drawBitmap(this.backBitmap, 0.0f, 0.0f, null);
            setLayoutParams(new FrameLayout.LayoutParams(width, height));
            this.isInit = true;
        }
    }
}
