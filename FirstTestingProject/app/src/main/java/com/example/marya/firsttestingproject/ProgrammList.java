package com.example.marya.firsttestingproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProgrammList extends Activity {
    static int numbers;
    RVAdapter adapter;
    //private static String FRAGMENT_INSTANCE_NAME = "fragment";
    FragmentList fragment;
    static int column;
    final Random random = new Random();
    private static TreeSet<Integer> ss;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        column = this.getResources().getInteger(intent.getIntExtra("column", 0));
        setTheme(intent.getIntExtra("theme", 0));
        setContentView(R.layout.activity_programm_list);
        rv = (RecyclerView) findViewById(R.id.rv);
        GridLayoutManager llm = new GridLayoutManager(this, column);
        rv.setLayoutManager(llm);
        fragment=(FragmentList) getLastNonConfigurationInstance();
        if (fragment==null){
            fragment=new FragmentList();
        }
        initializeAdapter();
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            Paint mPaint = new Paint();
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left=2;
                outRect.right=2;
                outRect.bottom=20;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            }
        });
    }
    public Object onRetainNonConfigurationInstance() {
        fragment.news=adapter.getNews();
        fragment.popular=adapter.getPopular();
        fragment.deleting=adapter.getProgramms();
        return fragment;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private void initializeAdapter(){
        adapter = new RVAdapter(fragment.popular, fragment.news,fragment.deleting,ProgrammList.this, column);
        rv.setAdapter(adapter);
    }
}
