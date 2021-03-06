package com.example.marya.firsttestingproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    static int column;
    static int theme;
    static ViewPager pager;
    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theme=R.style.AppTheme;

        column=R.integer.columnstandart;
        pager=(ViewPager)findViewById(R.id.view_pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        pager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }
    public void nextClick(View view){
        Button button=(Button) view;
        int page= pager.getCurrentItem();
        if (page==3)
            button.setText(R.string.finish);
        if (page<4)
            pager.setCurrentItem(page+1, true);
        else{
            button.setText(R.string.finish);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("Column", column);
                    ed.putInt("Theme", theme);
                    ed.putBoolean("isWelcome", false);
                    ed.apply();
                    Intent intent = new Intent(this, SplashActivity.class);
                    startActivity(intent);
            }
    }
    public void radioClick(View view){
        RadioButton rb = (RadioButton)view;
        switch (rb.getId()) {
            case R.id.radioButton:{
                theme=R.style.AppTheme;
            }
                break;
            case R.id.radioButton2: theme=R.style.DarkAppTheme;
                break;
            case R.id.radioButtonforsize: {column=R.integer.columnstandart;}
                break;
            case R.id.radioButtonforsize2: column=R.integer.columnbig;
                break;

            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
