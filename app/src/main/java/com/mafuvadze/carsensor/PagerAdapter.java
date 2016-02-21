package com.mafuvadze.carsensor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by Anesu on 2/21/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    Fragment[] tabs;
    Context context;
    int[] icons = new int[]{R.mipmap.ic_temp, R.mipmap.ic_graph, R.mipmap.ic_settings};

    public PagerAdapter(FragmentManager fm, Context context, Fragment[] tabs) {
        super(fm);
        this.tabs = tabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable ic = context.getResources().getDrawable(icons[position]);
        ic.setBounds(0,0,50,50);
        ImageSpan imgSpan = new ImageSpan(ic);
        SpannableString spannable = new SpannableString(" ");
        spannable.setSpan(imgSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}
