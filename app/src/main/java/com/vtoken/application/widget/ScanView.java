package com.vtoken.application.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.vtoken.application.R;
import vdsMain.tool.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class ScanView extends ViewfinderView {

    /* renamed from: o */
    int f4508o = DeviceUtil.dp2px(getContext(), 3.0f);

    /* renamed from: p */
    private final Bitmap f4509p = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_frame);

    public ScanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (this.framingRect != null && this.previewFramingRect != null) {
            Rect rect = this.framingRect;
            Rect rect2 = this.previewFramingRect;
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
            float f = (float) width;
            canvas.drawRect(0.0f, 0.0f, f, (float) (rect.top + 1), this.paint);
            canvas.drawRect(0.0f, (float) (rect.top + 1), (float) rect.left, (float) (rect.bottom + 1), this.paint);
            float f2 = f;
            canvas.drawRect((float) (rect.right + 1), (float) (rect.top + 1), f2, (float) (rect.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (rect.bottom + 1), f2, (float) height, this.paint);
            Rect rect3 = new Rect(this.framingRect);
            rect3.left -= this.f4508o;
            rect3.top -= this.f4508o;
            rect3.right += this.f4508o;
            rect3.bottom += this.f4508o;
            canvas.drawBitmap(this.f4509p, null, rect3, this.paint);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(160);
                canvas.drawBitmap(this.resultBitmap, null, rect, this.paint);
            } else {
                this.paint.setColor(this.laserColor);
                this.paint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
                this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
                int height2 = (rect.height() / 2) + rect.top;
                canvas.drawRect((float) (rect.left + 2), (float) (height2 - 1), (float) (rect.right - 1), (float) (height2 + 2), this.paint);
                float width2 = ((float) rect.width()) / ((float) rect2.width());
                float height3 = ((float) rect.height()) / ((float) rect2.height());
                List<ResultPoint> list = this.possibleResultPoints;
                List<ResultPoint> list2 = this.lastPossibleResultPoints;
                int i = rect.left;
                int i2 = rect.top;
                if (list.isEmpty()) {
                    this.lastPossibleResultPoints = null;
                } else {
                    this.possibleResultPoints = new ArrayList(5);
                    this.lastPossibleResultPoints = list;
                    this.paint.setAlpha(160);
                    this.paint.setColor(this.resultPointColor);
                    for (ResultPoint resultPoint : list) {
                        canvas.drawCircle((float) (((int) (resultPoint.getX() * width2)) + i), (float) (((int) (resultPoint.getY() * height3)) + i2), 6.0f, this.paint);
                    }
                }
                if (list2 != null) {
                    this.paint.setAlpha(80);
                    this.paint.setColor(this.resultPointColor);
                    for (ResultPoint resultPoint2 : list2) {
                        canvas.drawCircle((float) (((int) (resultPoint2.getX() * width2)) + i), (float) (((int) (resultPoint2.getY() * height3)) + i2), 3.0f, this.paint);
                    }
                }
                postInvalidateDelayed(80, rect.left - 6, rect.top - 6, rect.right + 6, rect.bottom + 6);
            }
        }
    }
}