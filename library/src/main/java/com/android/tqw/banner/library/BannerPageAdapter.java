package com.android.tqw.banner.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Hohenheim on 16/2/27.
 */
public class BannerPageAdapter extends FragmentStatePagerAdapter {
    private BannerItemCallback mItemCallback;

    private int mItemCount;
    private int mRealItemCount;
    private boolean mIsLoop;

    public BannerPageAdapter(FragmentManager fm, BannerItemCallback itemCallback) {
        super(fm);
        mItemCallback = itemCallback;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if(null != mItemCallback)
            fragment = mItemCallback.getFragment(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 设置显示页面的数量
     *
     * @param itemCount 要显示的页面的数量
     * @param isLoop 是否要循环显示
     */
    public void setItemCount(int itemCount, boolean isLoop) {
        mIsLoop = isLoop;
        mRealItemCount = itemCount;
        mItemCount = mRealItemCount;

        if(mIsLoop && mRealItemCount>1)
            mItemCount += 2;

        notifyDataSetChanged();
    }

    public boolean isLoop() {
        return mIsLoop;
    }
}