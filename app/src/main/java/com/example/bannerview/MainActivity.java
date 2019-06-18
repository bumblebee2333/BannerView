package com.example.bannerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mBannerList = new ArrayList<>();
    private BannerViewPager mViewPager;
    private BannerAdapter mBannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        mViewPager = findViewById(R.id.bannerview);
        mBannerAdapter = new BannerAdapter(mBannerList);
        mViewPager.setAdapter(mBannerAdapter);
        mViewPager.setOffscreenPageLimit(3);//缓存页面为3个
        mViewPager.setCurrentItem(1000*mBannerList.size());//点进源码发现 当item>list.size() item = list.size()-1 即在第一个item时可向前轮播
        mBannerAdapter.notifyData(mBannerList);
    }


    private void initList(){
        mBannerList.add("https://img1.doubanio.com/view/photo/l/public/p2558070117.webp");
        mBannerList.add("https://img3.doubanio.com/view/photo/l/public/p2554603942.webp");
        mBannerList.add("https://img3.doubanio.com/view/photo/l/public/p2557832194.webp");
    }

    public void onResume(){
        super.onResume();
        mViewPager.start();
    }

    public void onPause(){
        super.onPause();
        mViewPager.stop();
    }
}
