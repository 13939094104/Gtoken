package com.vtoken.application.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ScrollingView;

import com.airbnb.lottie.LottieAnimationView;
import com.orhanobut.logger.Logger;
import com.vtoken.application.R;

import vdsMain.tool.DeviceUtil;

public class RefreshableView extends LinearLayout  {

    private boolean isChildScrollView=false;
    /**
     * 下拉头的View
     */
    private View header;

    private LottieAnimationView loadingView;

    private int headerHeight;   //头高度

    private int lastHeaderPadding; //最后一次调用Move Header的Padding

    /**
     * 需要去下拉刷新的View
     */
    private ViewGroup refreshView;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    private boolean isBack; //从Release 转到 pull

    private int interval;

    private float maxInterval= 250;

    private int headerState = DONE;
    static final private int RELEASE_To_REFRESH = 0;
    static final private int PULL_To_REFRESH = 1;
    static final private int REFRESHING = 2;
    static final private int DONE = 3;

    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        header = LayoutInflater.from(context).inflate(R.layout.layout_refreshable, null, false);
//        tipsTxt=(TextView)header.findViewById(R.id.loadingText);
        loadingView=header.findViewById(R.id.loading);
        loadingView.setProgress(0);
        setOrientation(VERTICAL);
        //LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DeviceUtil.dp2px(context,240f));
        addView(header, 0);
    }


    /**
     * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            headerHeight = header.getMeasuredHeight();
            lastHeaderPadding = (-1*headerHeight); //最后一次调用Move Header的Padding
            header.setPadding(0, lastHeaderPadding, 0, 0);
            header.invalidate();
            refreshView = (ViewGroup) getChildAt(1);
            if(refreshView instanceof ScrollingView)
            {
                isChildScrollView=true;
                refreshView.setOnTouchListener(new OnTouchListener() {
                    private int beginY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                //sc.getScrollY == 0  scrollview 滑动到头了
                                //lastHeaderPadding > (-1*headerHeight) 表示header还没完全隐藏起来时
                                //headerState != REFRESHING 当正在刷新时
                                if((refreshView.getScrollY() == 0 || lastHeaderPadding > (-1*headerHeight)) && headerState != REFRESHING) {
                                    //拿到滑动的Y轴距离
                                    Logger.d("interval: "+interval);
                                    interval = (int) (event.getY() - beginY);
                                    //是向下滑动而不是向上滑动
                                    if (interval > 0) {
                                        interval = interval/2;//下滑阻力
                                        lastHeaderPadding = interval + (-1*headerHeight);
                                        header.setPadding(0, lastHeaderPadding, 0, 0);
                                        if(lastHeaderPadding > 0) {
                                            //txView.setText("我要刷新咯");
                                            headerState = RELEASE_To_REFRESH;
                                            //是否已经更新了UI
                                            if(!isBack) {
                                                isBack = true;  //到了Release状态，如果往回滑动到了pull则启动动画
                                                changeHeaderViewByState();
                                            }
                                        } else {
                                            headerState = PULL_To_REFRESH;
                                            changeHeaderViewByState();
                                            //txView.setText("看到我了哦");
                                            //sc.scrollTo(0, headerPadding);
                                        }
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_DOWN:
                                //加上下滑阻力与实际滑动距离的差（大概值）
                                beginY = (int) ((int) event.getY() + refreshView.getScrollY()*1.5);
                                break;
                            case MotionEvent.ACTION_UP:
                                if (headerState != REFRESHING) {
                                    switch (headerState) {
                                        case DONE:
                                            //什么也不干
                                            break;
                                        case PULL_To_REFRESH:
                                            headerState = DONE;
                                            lastHeaderPadding = -1*headerHeight;
                                            header.setPadding(0, lastHeaderPadding, 0, 0);
                                            changeHeaderViewByState();
                                            break;
                                        case RELEASE_To_REFRESH:
                                            isBack = false; //准备开始刷新，此时将不会往回滑动
                                            headerState = REFRESHING;
                                            changeHeaderViewByState();
                                            onRefresh();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                break;
                        }
                        //如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
                        if(lastHeaderPadding > (-1*headerHeight) && headerState != REFRESHING) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
            }
            loadOnce = true;
        }
    }


    private void changeHeaderViewByState() {

        switch (headerState) {
            case PULL_To_REFRESH:
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    //arrowImg.startAnimation(reverseAnimation);
                    //tipsTxt.setText("下拉刷新");
                }
                //tipsTxt.setText("下拉刷新");
               // Logger.d("pull"+interval);
                loadingView.setProgress(Math.min(interval/maxInterval,1f));
                break;
            case RELEASE_To_REFRESH:
                //Logger.d("release"+interval);
                loadingView.setProgress(Math.min(interval/maxInterval,1f));
                //loadingView.playAnimation();
                //loadingView.setProgress();
                //arrowImg.setVisibility(View.VISIBLE);
                //headProgress.setVisibility(View.GONE);
                //tipsTxt.setVisibility(View.VISIBLE);
                //lastUpdateTxt.setVisibility(View.VISIBLE);
                //arrowImg.clearAnimation();
                //arrowImg.startAnimation(tipsAnimation);
                //tipsTxt.setText("松开刷新");
                break;
            case REFRESHING:
                lastHeaderPadding = 0;
                header.setPadding(0, lastHeaderPadding, 0, 0);
                header.invalidate();
                //headProgress.setVisibility(View.VISIBLE);
                //arrowImg.clearAnimation();
                //arrowImg.setVisibility(View.INVISIBLE);
                //tipsTxt.setText("正在刷新...");
                //lastUpdateTxt.setVisibility(View.VISIBLE);
                break;
            case DONE:
                lastHeaderPadding = -1 * headerHeight;
                header.setPadding(0, lastHeaderPadding, 0, 0);
                header.invalidate();
                //headProgress.setVisibility(View.GONE);
                //arrowImg.clearAnimation();
                //arrowImg.setVisibility(View.VISIBLE);
                //tipsTxt.setText("下拉刷新");
                //lastUpdateTxt.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void onRefresh() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                onRefreshComplete();
            }

        }.execute();
    }

    public void onRefreshComplete() {
        headerState = DONE;
        //lastUpdateTxt.setText("最近更新:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }
}
