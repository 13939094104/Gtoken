package com.vtoken.application.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import java.util.List;

public class WheelPicker<T> extends View {

    private List<T> mDataList;

    private Paint mPaint;

    private Paint mSelectedItemPaint;

    private int mSelectedItemTextColor=Color.BLUE;

    /**
     * 第一个Item的绘制Text的坐标
     */
    private int mFirstItemDrawX, mFirstItemDrawY;

    /**
     * 最大的一个Item的文本的宽高
     */
    private int mTextMaxWidth, mTextMaxHeight;

    /**
     * 两个Item之间的高度间隔
     */
    private int mItemHeightSpace=0, mItemWidthSpace=10;

    private int mTextSize=36;

    int pos=0;

    /**
     * 显示的Item一半的数量（中心Item上下两边分别的数量）
     * 总显示的数量为 mHalfVisibleItemCount * 2 + 1
     */
    private int mHalfVisibleItemCount=0;

    /**
     * 输入的一段文字，可以用来测量 mTextMaxWidth
     */
    private String mItemMaximumWidthText;

    private int mItemHeight;

    /**
     * 整个控件的可绘制面积
     */
    private Rect mDrawnRect;

    private Scroller mScroller;

    /**
     * 当前的Item的位置
     */
    private int mCurrentPosition=0;

    /**
     * 选中的Item的Text大小
     */
    private int mSelectedItemTextSize=48;

    /**
     * 最后手指Down事件的Y轴坐标，用于计算拖动距离
     */
    private int mLastDownY;

    private int mTouchDownY;

    /**
     * 是否循环读取
     */
    private boolean mIsCyclic = true;

    /**
     * 该标记的作用是，令mTouchSlop仅在一个滑动过程中生效一次。
     */
    private boolean mTouchSlopFlag;

    /**
     * 中心的Item绘制text的Y轴坐标
     */
    private int mCenterItemDrawnY;

    /**
     * Y轴Scroll滚动的位移
     */
    private int mScrollOffsetY;

    private VelocityTracker mTracker;

    /**
     * 滚轮滑动时的最小/最大速度
     */
    private int mMinimumVelocity = 50, mMaximumVelocity = 12000;

    /**
     * 最大可以Fling的距离
     */
    private int mMaxFlingY, mMinFlingY;

    private Handler mHandler = new Handler();

    private OnWheelChangeListener<T> mOnWheelChangeListener;

    private Runnable mScrollerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                mScrollOffsetY = mScroller.getCurrY();
                postInvalidate();
                mHandler.postDelayed(this, 16);
            }
            if (mScroller.isFinished() || (mScroller.getFinalY() == mScroller.getCurrY()
                    && mScroller.getFinalX() == mScroller.getCurrX())) {

                if (mItemHeight == 0) {
                    return;
                }
                int position = -mScrollOffsetY / mItemHeight;
                position = fixItemPosition(position);
                if (mCurrentPosition != position) {
                    mCurrentPosition = position;
                    if (mOnWheelChangeListener == null) {
                        return;
                    }
                    mOnWheelChangeListener.onWheelSelected(mDataList.get(position),
                            position);
                }
            }
        }
    };

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WheelPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        mDrawnRect = new Rect();
        mScroller = new Scroller(context);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.WHITE);
        mSelectedItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mSelectedItemPaint.setStyle(Paint.Style.FILL);
        mSelectedItemPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedItemPaint.setColor(mSelectedItemTextColor);
        mSelectedItemPaint.setTextSize(mSelectedItemTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mTextMaxWidth + mItemWidthSpace;
        int height = (mTextMaxHeight + mItemHeightSpace) * getVisibleItemCount();

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureSize(specWidthMode, specWidthSize, width),
                measureSize(specHeightMode, specHeightSize, height));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemHeight = mDrawnRect.height() / getVisibleItemCount();
        mFirstItemDrawX = mDrawnRect.centerX();
        mFirstItemDrawY = (int) ((mItemHeight - (mSelectedItemPaint.ascent() + mSelectedItemPaint.descent())) / 2);
        mScrollOffsetY = -mItemHeight * mCurrentPosition;
        mCenterItemDrawnY = mFirstItemDrawY + mItemHeight * mHalfVisibleItemCount;
        computeFlingLimitY();
        //中间的Item边框
    }

    /**
     * 修正坐标值，让其回到dateList的范围内
     * @param position 修正前的值
     * @return  修正后的值
     */
    private int fixItemPosition(int position) {
        if (position < 0) {
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = mDataList.size() + (position % mDataList.size());

        }
        if (position >= mDataList.size()){
            //将数据集限定在0 ~ mDataList.size()-1之间
            position = position % mDataList.size();
        }
        return position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int drawnSelectedPos = - mScrollOffsetY / mItemHeight;
        for (int drawDataPos = drawnSelectedPos - mHalfVisibleItemCount - 1;drawDataPos <= drawnSelectedPos + mHalfVisibleItemCount + 1; drawDataPos++) {
            int position = drawDataPos;
            if (mIsCyclic) {
                position = fixItemPosition(position);
            } else {
                if (position < 0 || position > mDataList.size() - 1) {
                    continue;
                }
            }
            int itemDrawY = mFirstItemDrawY + (drawDataPos + mHalfVisibleItemCount) * mItemHeight + mScrollOffsetY;
            T data = mDataList.get(position);
            int distanceY = Math.abs(mCenterItemDrawnY - itemDrawY);
            if (distanceY < mItemHeight / 2) {
                canvas.drawText(data.toString(), mFirstItemDrawX, itemDrawY, mSelectedItemPaint);
            } else {
                canvas.drawText(data.toString(), mFirstItemDrawX, itemDrawY, mPaint);
            }
            canvas.drawText(data.toString(), mFirstItemDrawX, itemDrawY, mPaint);
        }
    }

    private void computeFlingLimitY() {
        mMinFlingY = mIsCyclic ? Integer.MIN_VALUE :
                - mItemHeight * (mDataList.size() - 1);
        mMaxFlingY = mIsCyclic ? Integer.MAX_VALUE : 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTracker.clear();
                mTouchDownY = mLastDownY = (int) event.getY();
                mTouchSlopFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchSlopFlag = false;
                float move = event.getY() - mLastDownY;
                mScrollOffsetY += move;     //滑动的偏移量
                mLastDownY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocity = (int) mTracker.getYVelocity();
                mScroller.fling(0, mScrollOffsetY, 0, velocity,
                        0, 0, mMinFlingY, mMaxFlingY);
                mScroller.setFinalY(mScroller.getFinalY() +
                        computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
                mHandler.post(mScrollerRunnable);
                mTracker.recycle();
                mTracker = null;
                break;
        }
        return true;
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mItemHeight / 2) {
            if (mScrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }


    /**
     * 显示的个数等于上下两边Item的个数+ 中间的Item
     * @return 总显示的数量
     */
    public int getVisibleItemCount() {
        return mHalfVisibleItemCount * 2 + 1;
    }

    public void computeTextSize() {
        mTextMaxWidth = mTextMaxHeight = 0;
        if (mDataList.size() == 0) {
            return;
        }
        //这里使用最大的,防止文字大小超过布局大小。
        mPaint.setTextSize(mSelectedItemTextSize> mTextSize ? mSelectedItemTextSize : mTextSize);

        if (!TextUtils.isEmpty(mItemMaximumWidthText)) {
            mTextMaxWidth = (int) mPaint.measureText(mItemMaximumWidthText);
        } else {
            mTextMaxWidth = (int) mPaint.measureText(mDataList.get(0).toString());
        }
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextMaxHeight = (int) (metrics.bottom - metrics.top);
    }

    /**
     *  计算实际的大小
     * @param specMode 测量模式
     * @param specSize 测量的大小
     * @param size     需要的大小
     * @return 返回的数值
     */
    private int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        if (dataList.size() == 0) {
            return;
        }
        computeTextSize();
        requestLayout();
        postInvalidate();
    }

    public void setOnWheelChangeListener(OnWheelChangeListener<T> onWheelChangeListener) {
        mOnWheelChangeListener = onWheelChangeListener;
    }

    public interface OnWheelChangeListener<T> {
        void onWheelSelected(T item, int position);
    }


}
