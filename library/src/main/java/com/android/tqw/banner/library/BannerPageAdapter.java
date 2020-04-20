package com.android.tqw.banner.library;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Hohenheim on 16/2/27.
 */
public class BannerPageAdapter extends FragmentStatePagerAdapter {
    private BannerItemCallback mItemCallback;

    private int mItemCount;
    private int mRealItemCount;
    private boolean mIsLoop;

    public BannerPageAdapter(FragmentManager fm, BannerItemCallback itemCallback) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

    public int getRealCount() {
        return mRealItemCount;
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