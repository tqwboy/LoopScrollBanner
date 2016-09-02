package com.android.tqw.banner.library;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hohenheim on 16/2/27.
 */
public class ScrollBanner extends FrameLayout {
    private ViewPager mBannerViewPage;
    private BannerPageAdapter mBannerAdapter;
    private LinearLayout mIndicatorLayout;
    private int mIndicatorNormalColor;
    private int mIndicatorSelectedColor;
    private ImageView[] mIndicatorViews;

    private long mExecuteTime = 5 * 1000;
    private UiThread mHandler;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean mScrolling;

    private int mCurrent;
    private boolean mTouchScroll;

    public ScrollBanner(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean indicatorInnerLayout = true;
        if(null != attrs) {
            Resources.Theme layoutTheme = context.getTheme();
            TypedArray attributeArray = layoutTheme.obtainStyledAttributes(attrs, R.styleable
                    .ScrollBanner, defStyleAttr, 0);

            int attributeCount = attributeArray.getIndexCount();
            for(int i=0; i<attributeCount; ++i) {
                int attr = attributeArray.getIndex(i);
                if(attr == R.styleable.ScrollBanner_indicator_relative)
                    indicatorInnerLayout = attributeArray.getBoolean(i, true);
            }

            attributeArray.recycle();
        }

        View rootView;
        if(indicatorInnerLayout)
            rootView = inflater.inflate(R.layout.layout_relative_loop_scroll_banner, this, true);
        else
            rootView = inflater.inflate(R.layout.layout_linear_loop_scroll_banner, this, true);

        mBannerViewPage = (ViewPager) rootView.findViewById(R.id.loop_banner_pager);
        mIndicatorLayout = (LinearLayout) rootView.findViewById(R.id.indicator_layout);

        mIndicatorNormalColor = ContextCompat.getColor(getContext(), android.R.color.darker_gray);
        mIndicatorSelectedColor = ContextCompat.getColor(getContext(), android.R.color.white);

        //设置页面选择监听事件
        mBannerViewPage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(null==mBannerAdapter || mBannerAdapter.getCount()<=1 || !mBannerAdapter.isLoop())
                    return;

                //滑动结束后，判断滑到的位置是否为第一个或者是最后一个
                if (positionOffset + positionOffsetPixels == 0 ) {
                    int itemCount = mBannerAdapter.getCount();

                    int current = -1;
                    if(position <= 0)
                        current = itemCount - 2;
                    else if(position >= itemCount - 1)
                        current = 1;

                    if(current >= 0) {
                        //延迟跳转，避免闪烁
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = new Integer(current);
                        mHandler.sendMessageDelayed(msg, 10);
                    }
                    else {
                        changeIndicator(position);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrent = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch(state) {
                    case ViewPager.SCROLL_STATE_IDLE: //滑动结束
                        //如果此次滑动为手指拖动，就在滑动结束后，恢复滚动计时器
                        if(mTouchScroll) {
                            mTouchScroll = false;
                            startScroll();
                        }
                        break;

                    case ViewPager.SCROLL_STATE_DRAGGING: //手势滑动
                        mTouchScroll = true; //记录此次滑动为手指拖动
                        stopScroll();
                        break;

                    default:
                        break;
                }
            }
        });

        mHandler = new UiThread(this);
    }

    private class BannerScrollTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    private static class UiThread extends Handler {
        public WeakReference<ScrollBanner> bannerRef;

        public UiThread(ScrollBanner banner) {
            bannerRef = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            ScrollBanner banner = bannerRef.get();
            if(null == banner)
                return;

            if(msg.what == 0) {
                int current = (banner.mCurrent + 1) % banner.mBannerAdapter.getCount();

                if(current < banner.mBannerAdapter.getCount())
                    banner.mBannerViewPage.setCurrentItem(current, true);
            }
            else {
                int current = (Integer) msg.obj;
                banner.mBannerViewPage.setCurrentItem(current, false);
                banner.changeIndicator(current);
            }
        }
    }

    /**
     * 初始化广告栏
     */
    public void initScroll(FragmentManager fm, BannerItemCallback pageItemOptCallback) {
        mBannerAdapter = new BannerPageAdapter(fm, pageItemOptCallback);
        mBannerViewPage.setAdapter(mBannerAdapter);
    }

    /**
     * 设置展示页面数量
     *
     * @param count 要显示的页面的数量
     * @param isLoop 是否要循环显示
     */
    public void setShowItemCount(int count, boolean isLoop) {
        if(null != mBannerAdapter) {
            //设置指示标示
            mIndicatorLayout.removeAllViews();
            if(count <= 1) {
                mIndicatorLayout.setVisibility(View.GONE);
            }
            else {
                mIndicatorLayout.setVisibility(View.VISIBLE);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                mIndicatorViews = new ImageView[count];

                for(int i=0; i<count; ++i) {
                    ImageView indicatorView = (ImageView) inflater.inflate(
                            R.layout.layout_circle_page_indicator, null);
                    mIndicatorViews[i] = indicatorView;

                    GradientDrawable bgShape = (GradientDrawable) indicatorView.getBackground();
                    if(i == 0)
                        bgShape.setColor(mIndicatorSelectedColor);
                    else
                        bgShape.setColor(mIndicatorNormalColor);

                    mIndicatorLayout.addView(indicatorView);
                }
            }

            //设置要显示的内容的数量
            mBannerAdapter.setItemCount(count, isLoop);
            if(count>1 && isLoop)
                mBannerViewPage.setCurrentItem(1);
        }
    }

    //变更页码指示器的颜色
    private void changeIndicator(int position) {
        if(null == mIndicatorViews)
            return;

        int indicatorPosition = getIndicatorPosition(position);

        for(int i=0; i<mIndicatorViews.length; ++i) {
            ImageView indicatorIv = mIndicatorViews[i];
            GradientDrawable bgShape = (GradientDrawable) indicatorIv.getBackground();
            if(i == indicatorPosition)
                bgShape.setColor(mIndicatorSelectedColor);
            else
                bgShape.setColor(mIndicatorNormalColor);
        }
    }

    private int getIndicatorPosition(int position) {
        int indicatorPosition = position;

        if(mBannerAdapter.getCount()>1 && mBannerAdapter.isLoop()) {
            if(position<=0 || position==mBannerAdapter.getCount()-2)
                indicatorPosition = mBannerAdapter.getCount() - 2 - 1;
            else if(position>=mBannerAdapter.getCount()-1 || position==1)
                indicatorPosition = 0;
            else
                indicatorPosition = position - 1;
        }

        return indicatorPosition;
    }

    /**
     * 设置滚动时间，单位：毫秒
     */
    public void setScrollTime(long scrollTime) {
        mExecuteTime = scrollTime;

        if(mScrolling) {
            stopScroll();
            startScroll();
        }
    }

    /**
     * 启动滚动，滚动间隔时间为已设置好的时间，如果没设置，就用默认时间，默认为5000毫秒
     *
     * @return 如果正在滚动状态、没有初始化、页面数量小于2，就不启动滚动，返回false
     */
    public boolean startScroll() {
        boolean startSuccess = false;

        if(!mScrolling && null!=mBannerAdapter && mBannerAdapter.getCount()>1) {
            mScrolling = true;
            startSuccess = true;

            mTimer = new Timer();
            mTimerTask = new BannerScrollTask();
            mTimer.schedule(mTimerTask, mExecuteTime, mExecuteTime);
        }

        return startSuccess;
    }

    /**
     * 启动滚动，并设置滚动间隔时间，设置的间隔时间小于1000毫秒，就放弃设置，使用原时间
     *
     * @return 启动自动滚动成功：true；启动自动滚动失败：false
     */
    public boolean startScroll(long executeTime) {
        if(executeTime >= 1000)
            setScrollTime(executeTime);

        return startScroll();
    }

    /**
     * 终止滚动
     */
    public void stopScroll() {
        if(mScrolling) {
            mScrolling = false;
            mTimerTask.cancel();
            mTimer.cancel();
        }
    }

    /**
     * 设置指示器颜色
     *
     * @param normalColor 未选中该页时，显示的颜色
     * @param selectedColor 选中该页时，显示的颜色
     */
    public void setIndicatorColor(int normalColor, int selectedColor) {
        mIndicatorNormalColor = normalColor;
        mIndicatorSelectedColor = selectedColor;
        changeIndicator(mCurrent);
    }

    public boolean isScrolling() {
        return mScrolling;
    }
}