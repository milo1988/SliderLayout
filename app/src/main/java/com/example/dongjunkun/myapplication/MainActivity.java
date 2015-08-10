package com.example.dongjunkun.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private SliderLayout sliderLayout;
    private List<Integer> viewRes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderLayout = (SliderLayout) findViewById(R.id.sliderLayout);
        viewRes.add(R.mipmap.img1);
        viewRes.add(R.mipmap.img2);
        viewRes.add(R.mipmap.img3);
        sliderLayout.setViewRes(viewRes);
    }
}
