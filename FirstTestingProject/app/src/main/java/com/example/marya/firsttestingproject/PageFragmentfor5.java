package com.example.marya.firsttestingproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by marya on 28.3.17.
 */
public class PageFragmentfor5 extends Fragment {
    //private RadioGroup mRadioOsGroup;
    public PageFragmentfor5() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragmentfor5, container, false);

        return result;
    }
}