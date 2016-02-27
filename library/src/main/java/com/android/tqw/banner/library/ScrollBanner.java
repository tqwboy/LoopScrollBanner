package com.android.tqw.banner.library;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hohenheim on 16/2/27.
 */
public class ScrollBanner extends FrameLayout {
    private ViewPager mBannerViewPage;
    private BannerPageAdapter mBannerAdapter;
    private LinearLayout mIndicatorLayout;

    private int mDefaultImgResId;
    private long mExecuteTime = 6 * 1000;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean mScrolling;

    public ScrollBanner(Context context) {
        super(context);
        init(context);
    }

    public ScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.layout_loop_scroll_banner, this, true);
        mBannerViewPage = (ViewPager) rootView.findViewById(R.id.loop_banner_pager);
        mIndicatorLayout = (LinearLayout) rootView.findViewById(R.id.indicator_layout);
    }

    private class BannerScrollTask extends TimerTask {
        @Override
        public void run() {

        }
    }

    /**
     * 设置默认图片
     */
    public void setDefaultImg(int resId) {
        mDefaultImgResId = resId;

        if(null != mBannerAdapter)
            mBannerAdapter.setDefaultImgResId(resId);
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
     * 设置要显示的图片Url
     */
    public void setBannerImgList(FragmentManager fm, ArrayList<String> imgList) {
        stopScroll();

        if(null == mBannerAdapter)
            mBannerAdapter = new BannerPageAdapter(fm);
        if(mDefaultImgResId > 0)
            mBannerAdapter.setDefaultImgResId(mDefaultImgResId);

        if(mBannerAdapter.setImgUrlList(imgList) && imgList.size()>1)
            startScroll();
    }


    public void startScroll() {
        if(!mScrolling) {
            mScrolling = true;
        }
    }

    public void startScroll(long executeTime) {
        if(executeTime >= 1000)
            mExecuteTime = executeTime;
        startScroll();
    }

    public void stopScroll() {
        if(mScrolling) {
            mScrolling = false;
        }
    }
}