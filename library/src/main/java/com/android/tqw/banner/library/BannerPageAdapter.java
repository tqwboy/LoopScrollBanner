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
        int dataPosition;
        if(mItemCount>1 && mIsLoop) {
            if(position == 0) {
                dataPosition = mRealItemCount - 1;
            }
            else if(position == mItemCount - 1) {
                dataPosition = 0;
            }
            else {
                dataPosition = position - 1;
            }
        }
        else {
            dataPosition = position;
        }

        return mItemCallback.getFragment(position, dataPosition);
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

        if(mIsLoop && mRealItemCount>1) {
            mItemCount += 2;
        }

        notifyDataSetChanged();
    }

    public boolean isLoop() {
        return mIsLoop;
    }

    public int getRealPosition(int position) {
        int realPosition = position;

        if (mItemCount>1 && mIsLoop) {
            if(position == 0) {
                realPosition = mItemCount - 2;
            }
            else if (position == mItemCount - 1) {
                realPosition = 1;
            }
        }

        return realPosition;
    }
}