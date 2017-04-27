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
    private int column;
    private int theme;
    private List<Programm>popular;
    private List<Programm>news;
    private List<Programm>favorites;
    private List<String> uries;
    public FragmentList() {
    }
    public void save(int column, int theme, List<Programm>popular, List<Programm>news, List<Programm>favorites, List<String>uries){
        this.column=column;
        this.theme=theme;
        this.popular=popular;
        this.news=news;
        this.favorites=favorites;
        this.uries=uries;
    }
    public int getTheme(){
        return theme;
    }
    public int getColumn(){
        return column;
    }
    public List<Programm> getPopular(){
        return popular;
    }
    public List<Programm> getNews(){
        return news;
    }
    public List<Programm> getFavorites(){
        return favorites;
    }
    public List<String> getUries(){
        return uries;
    }
}
