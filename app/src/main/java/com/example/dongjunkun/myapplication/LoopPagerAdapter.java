package com.example.dongjunkun.myapplication;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dongjunkun on 2015/8/9.
 */
public class LoopPagerAdapter extends PagerAdapter {
    private List<View> views;

    public LoopPagerAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        System.out.println("container = [" + container + "], position = [" + position + "]");
        System.out.println("position = " + position % views.size());
        View view = views.get(position % views.size());
        if (container.equals(view.getParent())) {
            container.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
