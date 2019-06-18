package com.example.bannerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class BannerViewPager extends ViewPager {

    private Handler mHandler;
    private TaskRunnable mTaskRunnable;
    private BannerViewPager instance;
    public static boolean mIsRunning = false;//是否正在执行

    public BannerViewPager(@NonNull Context context) {
        super(context);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        addOnPageChangeListener(mOnPageChangeListener);
        setViewPagerDuration();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state){
                case ViewPager.SCROLL_STATE_IDLE://空闲状态
                    start();
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING://手指拖动状态
                    stop();
                    break;
                case ViewPager.SCROLL_STATE_SETTLING://手指松开状态
                    break;
            }
        }
    };

    private void startTimingTask(){
        if (mHandler == null && !mIsRunning){
            mHandler = new Handler();
            mTaskRunnable = new TaskRunnable(instance);
            //六秒后调用此runnbale对象//类似于run();为什么还要进行一次延时？
            mHandler.postDelayed(mTaskRunnable,8000);
            //mTaskRunnable.run();//用这个的话直接会跳到第二张图不知道为什么
            mIsRunning = true;
        }
    }

    private void stopTimingTask(){
        if(mHandler!=null && mIsRunning){
            mHandler.removeCallbacks(mTaskRunnable);
            mHandler = null;
            mIsRunning = false;
        }
    }

    public void start(){
        startTimingTask();
    }

    public void stop(){
        stopTimingTask();
    }

    private static class TaskRunnable implements Runnable{
        private WeakReference<BannerViewPager> weakReference;


        public TaskRunnable(BannerViewPager bannerViewPager){
            this.weakReference = new WeakReference<>(bannerViewPager);
        }

        @Override
        public void run() {
            BannerViewPager instance = weakReference.get();
            if(instance == null){
                return;
            }
            instance.setCurrentItem();
        }
    }

    private void setCurrentItem(){
        setCurrentItem(getCurrentItem()+1,true);
        //实现每六秒实现一次的定时器操作
        mHandler.postDelayed(mTaskRunnable,8000);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setLayoutFirst(false);
        start();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    private void setLayoutFirst(boolean isFirstLayout){
        try {
            Class<?> clazz = Class.forName("android.support.v4.view.ViewPager");
            Field field = clazz.getDeclaredField("mFirstLayout");
            field.setAccessible(true);
            field.setBoolean(this,isFirstLayout);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    private class FixedSpeedScroll extends Scroller{
        private int mDuration = 750;//ms

        public FixedSpeedScroll(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy);
        }
    }

    private void setViewPagerDuration(){
        try {
            Class<?> clazz = Class.forName("android.support.v4.view.ViewPager");
            FixedSpeedScroll fixedSpeedScroll = new FixedSpeedScroll(getContext());
            Field field = clazz.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this,fixedSpeedScroll);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
