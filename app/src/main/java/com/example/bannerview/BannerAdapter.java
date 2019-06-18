package com.example.bannerview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<String> mBannerDatas;
    private SparseArray<View> mViews;

    public BannerAdapter(List<String> bannerDatas){
        this.mBannerDatas = bannerDatas;
        mViews = new SparseArray<>();
    }

    public void notifyData(List<String> bannerDatas){
        this.mBannerDatas = bannerDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mBannerDatas == null)
            return 0;
        return mBannerDatas.size() <=1 ?mBannerDatas.size() : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public Object instantiateItem(final ViewGroup container,int position){
        View view = mViews.get(position);
        if(view == null){
            position %= mBannerDatas.size();
            final String url = mBannerDatas.get(position);
            view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner,container,false);
            ImageView imageView = view.findViewById(R.id.imageview);
            Glide.with(imageView.getContext()).load(url).into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mViews.put(position,view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        mViews.clear();
        return POSITION_NONE;
    }
}
