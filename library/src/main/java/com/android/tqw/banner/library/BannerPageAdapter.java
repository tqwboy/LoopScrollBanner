package com.android.tqw.banner.library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Hohenheim on 16/2/27.
 */
public class BannerPageAdapter extends FragmentStatePagerAdapter {
    private int mDefaultImgResId;
    private ArrayList<String> mImgUrlList;

    public BannerPageAdapter(FragmentManager fm) {
        super(fm);
        mImgUrlList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        BannerItem bannerItem = new BannerItem();
        String imgUrl = mImgUrlList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString(String.valueOf(bannerItem.hashCode()), imgUrl);

        bannerItem.setArguments(bundle);
        return bannerItem;
    }

    @Override
    public int getCount() {
        return mImgUrlList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 设置默认图片资源ID
     */
    public void setDefaultImgResId(int resId) {
        mDefaultImgResId = resId;
    }

    /**
     * 设置图片Url
     */
    public boolean setImgUrlList(ArrayList<String> imgUrlList) {
        boolean setResult = false;

        if(null!=imgUrlList && imgUrlList.size()>0) {
            if(imgUrlList.size() <= 1) {
                mImgUrlList = imgUrlList;
            }
            else {
                mImgUrlList.clear();
                mImgUrlList.addAll(imgUrlList);
                mImgUrlList.add(0, imgUrlList.get(imgUrlList.size() - 1));
                mImgUrlList.add(mImgUrlList.size(), imgUrlList.get(0));
            }

            notifyDataSetChanged();
            setResult = true;
        }

        return setResult;
    }
}