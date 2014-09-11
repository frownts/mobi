package com.join.mobi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 下午5:34
 */
public class ExamViewPagerAdapter extends PagerAdapter {

    List<ExamView> pagerView;

    public ExamViewPagerAdapter(List<ExamView> _pagerView) {
        this.pagerView = _pagerView;
    }

    @Override
    public int getCount() {
        return pagerView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pagerView.get(position).getView());
        return pagerView.get(position).getView();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pagerView.get(position).getView());
    }



}


