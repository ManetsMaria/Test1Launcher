package com.example.marya.firsttestingproject;

/**
 * Created by marya on 28.3.17.
 */
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
import android.view.View;

public class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager mgr) {
        super(mgr);
    }
    @Override
    public int getCount() {
        return(5);
    }
    @Override
    public Fragment getItem(int position) {

        if (position<3)
            return(PageFragment.newInstance(position));
        if (position==3)
            return (new PageFragmentfor4());
        return (new PageFragmentfor5());
    }
}