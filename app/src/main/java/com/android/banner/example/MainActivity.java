package com.android.banner.example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.android.tqw.banner.library.BannerItemCallback;
import com.android.tqw.banner.library.ScrollBanner;

public class MainActivity extends AppCompatActivity {
    private ScrollBanner mBanner;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mBanner.setIndicatorColor(ContextCompat.getColor(getApplicationContext(),
                        android.R.color.holo_blue_light),
                        ContextCompat.getColor(getApplicationContext(),
                        android.R.color.holo_orange_dark));
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBanner = (ScrollBanner) findViewById(R.id.loop_banner);
        mBanner.initScroll(getSupportFragmentManager(), new BannerItemCallback() {
            @Override
            public Fragment getFragment(int position) {
                /*
                 * 创建展示内容的Fragment，如果是循环轮播，整个Banner的页面数量会比设置的多2个
                 * 所以，在循环轮播的情况下，第0页和倒数第2页的内容应该设置为一样
                 * 同理，第1页和最后一页的内容应该设置为一样
                 */

                PageItem pageFragment = new PageItem();

                Bundle bundle = new Bundle();
                int colorId;

                switch(position) {
                    case 0:
                    case 3:
                        colorId = android.R.color.holo_red_light;
                        break;

                    case 1:
                    case 4:
                        colorId = android.R.color.black;
                        break;

                    default:
                        colorId = android.R.color.holo_green_light;
                        break;
                }

                bundle.putInt(String.valueOf(pageFragment.hashCode()), colorId);
                pageFragment.setArguments(bundle);

                return pageFragment;
            }
        });

        mBanner.setShowItemCount(3, true);
        mBanner.startScroll();

        mHandler.sendEmptyMessageDelayed(0, 3000); //测试设置指示器颜色
    }

    @Override
    protected void onDestroy() {
        mBanner.stopScroll();
        super.onDestroy();
    }
}
