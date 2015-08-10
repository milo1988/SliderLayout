package com.example.dongjunkun.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjunkun on 2015/8/9.
 */
public class SliderLayout extends RelativeLayout implements android.os.Handler.Callback {

    public static final int DELAY_MILLIS = 4000;
    private ViewPager pager;
    //指示器容器
    private LinearLayout indicatorContainer;

    private Drawable unSelectedDrawable;
    private Drawable selectedDrawable;

    private int WHAT_AUTO_PLAY = 1000;
    private Handler handler;

    private boolean isAutoPlay = false;

    private int itemCount = 0;

    public SliderLayout(Context context) {
        super(context);
        initView();
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        handler = new Handler(this);
        //初始化pager
        pager = new ViewPager(getContext());
        //添加viewpager到SliderLayout
        addView(pager);
        int period = 1000;
        setSliderTransformDuration(period, null);

        //初始化indicatorContainer
        indicatorContainer = new LinearLayout(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //设置指示器的方位，如右下
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //设置margin
        params.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        //添加指示器容器布局到SliderLayout
        addView(indicatorContainer, params);

        //绘制未选中状态图形
        LayerDrawable unSelectedLayerDrawable;
        LayerDrawable selectedLayerDrawable;
        GradientDrawable unSelectedGradientDrawable;
        unSelectedGradientDrawable = new GradientDrawable();
        unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
        unSelectedGradientDrawable.setColor(Color.GRAY);
        unSelectedGradientDrawable.setSize(dpToPx(8), dpToPx(8));
        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        unSelectedDrawable = unSelectedLayerDrawable;
        //绘制选中状态图形
        GradientDrawable selectedGradientDrawable;
        selectedGradientDrawable = new GradientDrawable();
        selectedGradientDrawable.setShape(GradientDrawable.OVAL);
        selectedGradientDrawable.setColor(Color.RED);
        selectedGradientDrawable.setSize(dpToPx(8), dpToPx(8));
        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        selectedDrawable = selectedLayerDrawable;
    }

    //添加本地图片路径
    public void setViewRes(List<Integer> viewRes) {
        List<View> views = new ArrayList<>();
        itemCount = viewRes.size();
        //主要是解决当item为小于3个的时候滑动有问题，这里将其拼凑成3个以上
        if (itemCount < 1) {//当item个数0
            throw new IllegalStateException("item count not equal zero");
        } else if (itemCount < 2) {//当item个数为1
            views.add(getImageView(viewRes.get(0)));
            views.add(getImageView(viewRes.get(0)));
            views.add(getImageView(viewRes.get(0)));
        } else if (itemCount < 3) {//当item个数为2
            views.add(getImageView(viewRes.get(0)));
            views.add(getImageView(viewRes.get(1)));
            views.add(getImageView(viewRes.get(0)));
            views.add(getImageView(viewRes.get(1)));
        } else {
            for (Integer res : viewRes) {
                views.add(getImageView(res));
            }
        }
        setViews(views);
    }

    @NonNull
    private ImageView getImageView(Integer res) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(res).into(imageView);
        return imageView;
    }

    //添加网络图片路径
    public void setViewUrls(List<String> urls) {
        List<View> views = new ArrayList<>();
        itemCount = urls.size();
        //主要是解决当item为小于3个的时候滑动有问题，这里将其拼凑成3个以上
        if (itemCount < 1) {//当item个数0
            throw new IllegalStateException("item count not equal zero");
        } else if (itemCount < 2) { //当item个数为1
            views.add(getImageView(urls.get(0)));
            views.add(getImageView(urls.get(0)));
            views.add(getImageView(urls.get(0)));
        } else if (itemCount < 3) {//当item个数为2
            views.add(getImageView(urls.get(0)));
            views.add(getImageView(urls.get(1)));
            views.add(getImageView(urls.get(0)));
            views.add(getImageView(urls.get(1)));
        } else {
            for (String url : urls) {
                views.add(getImageView(url));
            }
        }
        setViews(views);
    }

    @NonNull
    private ImageView getImageView(String url) {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext()).load(url).into(imageView);
        return imageView;
    }

    //添加任意View视图
    private void setViews(final List<View> views) {
        //初始化指示器，并添加到指示器容器布局
        for (int i = 0; i < itemCount; i++) {
            ImageView indicator = new ImageView(getContext());
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            indicator.setPadding(dpToPx(3), dpToPx(3), dpToPx(3), dpToPx(3));
            indicatorContainer.addView(indicator);
        }
        LoopPagerAdapter pagerAdapter = new LoopPagerAdapter(views);
        pager.setAdapter(pagerAdapter);
        //设置当前item到Integer.MAX_VALUE中间的一个值，看起来像无论是往前滑还是往后滑都是ok的
        //如果不设置，用户往左边滑动的时候已经划不动了
        int currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % itemCount;
        pager.setCurrentItem(currentItem);
        //设置动画
        pager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position >= -1 && position <= 1) {
                    page.setScaleX(1.0f - Math.abs(position) * .3f);
                    page.setScaleY(1.0f - Math.abs(position) * .3f);
                }

            }
        });
        switchIndicator(currentItem % itemCount);
        //添加监听器
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchIndicator(position % itemCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startAutoPlay();

    }

    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), interpolator, period);
            mScroller.set(pager, scroller);
        } catch (Exception e) {


        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == WHAT_AUTO_PLAY) {
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
        }
        return false;
    }

    /**
     * 开始自动轮播
     */
    public void startAutoPlay() {
        if (!isAutoPlay) {
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
            isAutoPlay = true;
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        if (isAutoPlay) {
            handler.removeMessages(WHAT_AUTO_PLAY);
            isAutoPlay = false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 切换指示器状态
     *
     * @param currentPosition 当前位置
     */
    private void switchIndicator(int currentPosition) {
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            ((ImageView) indicatorContainer.getChildAt(i)).setImageDrawable(i == currentPosition ? selectedDrawable : unSelectedDrawable);
        }
    }

    /**
     * 单位转换将dp转化为px
     *
     * @param dp dp值
     * @return 返回px
     */
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics) + 0.5);
    }

}


