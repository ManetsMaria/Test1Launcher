package com.example.marya.firsttestingproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by marya on 28.3.17.
 */
public class PageFragmentfor4 extends Fragment {
    //private RadioGroup mRadioOsGroup;
    public PageFragmentfor4() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragmentpagefor4, container, false);
       // mRadioOsGroup = (RadioGroup) getActivity().findViewById(R.id.radioV);
        //mRadioOsGroup.check(R.id.radioButton);
        return result;
    }
}
