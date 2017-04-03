package com.example.marya.firsttestingproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marya on 28.3.17.
 */
public class FragmentList extends Fragment {
    public List<Programm> popular;
    public List<Programm>news;
    public List<Programm> deleting;
    public FragmentList() {
        news=new ArrayList<>();
        popular=new ArrayList<>();
        deleting=new ArrayList<>();
        this.setRetainInstance(true);
    }

}
