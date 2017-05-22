package com.example.marya.firsttestingproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Setting extends AppCompatActivity {
    EditText editText;
    CheckBox checkBox;
    Button clearUri;
    Button clearFav;
    RadioGroup size;
    RadioGroup theme;
    SharedPreferences sPref;
    ImageView imageView;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        editText=(EditText) findViewById(R.id.numberUries);
        checkBox=(CheckBox) findViewById(R.id.hideFav);
        clearUri=(Button)findViewById(R.id.clearUri);
        size=(RadioGroup)findViewById(R.id.radioGroupSet);
        theme=(RadioGroup)findViewById(R.id.radioGroupSet2);
        imageView=(ImageView) findViewById(R.id.settingImage);
        sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);
        item=menu.getItem(0);
        item.setChecked(false);
        if (sPref.getBoolean("saveFav", false))
            secondFalse();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                Intent intent=new Intent(Setting.this, ProgrammList.class);
                                intent.putExtra("screneNumber", 0);
                                startActivity(intent);
                                break;
                            case R.id.action_settings:
                                break;
                            case R.id.action_navigation:
                                if (!sPref.getBoolean("saveFav", false)) {
                                    Intent i = new Intent(Setting.this, ProgrammList.class);
                                    i.putExtra("screneNumber", 1);
                                    startActivity(i);
                                }
                                break;
                        }
                        return true;
                    }
                });
        editText.setHint(String.valueOf(sPref.getInt("numberHints", 2)));
        int column=sPref.getInt("Column", R.integer.columnstandart);
        final int them=sPref.getInt("Theme", R.style.AppTheme);
        if (column==R.integer.columnstandart){
            RadioButton rb=(RadioButton)findViewById(R.id.radioButtonSet);
            rb.setChecked(true);
        }
        else{
            RadioButton rb=(RadioButton)findViewById(R.id.radioButtonSet2);
            rb.setChecked(true);
        }
        if (them==R.style.AppTheme){
            RadioButton rb=(RadioButton)findViewById(R.id.radioButtonSetTheme);
            rb.setChecked(true);
        }
        else{
            RadioButton rb=(RadioButton)findViewById(R.id.radioButtonSetTheme2);
            rb.setChecked(true);
        }
        boolean saveFav=sPref.getBoolean("saveFav", false);
        if (saveFav)
            checkBox.setChecked(true);
        else{
            checkBox.setChecked(false);
        }
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String str=editText.getText().toString();
                if (str.length()>0) {
                    int numb = Integer.valueOf(str);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putInt("numberHints", numb);
                    ed.apply();
                }
                else{
                    Toast.makeText(Setting.this,"input number",Toast.LENGTH_LONG).show();
                }
                editText.clearFocus();
                return false;
            }
        });
        imageView.setAlpha(0.5f);
        if (getIntent().getIntExtra("flag", 1)==1) {
            try {
                FileInputStream is = openFileInput("bitmap.png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Log.d("setting", "try");
                if (bitmap != null) {
                    Log.d("setting", "set");
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(bitmap);
                }
                is.close();
            } catch (IOException e) {
                Log.d("setting", "exception");
                e.printStackTrace();
            } catch (OutOfMemoryError a) {
                Toast.makeText(this, "сервис перегружен, пожалуйста, запустите приложение ещё раз", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void settingClick(View view){
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean("clearUri", true);
            ed.putBoolean("clearFav", true);
            ed.apply();
            Toast.makeText(this,"clear history",Toast.LENGTH_LONG).show();
    }
    public void radioSetClick(View view){
        RadioButton rb = (RadioButton)view;
        switch (rb.getId()) {
            case R.id.radioButtonSet:{
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Column", R.integer.columnstandart);
                ed.apply();
            }
            break;
            case R.id.radioButtonSet2:{
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Column", R.integer.columnbig);
                ed.apply();
            }
                break;
            case R.id.radioButtonSetTheme: {
                setTheme(R.style.AppTheme);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Theme", R.style.AppTheme);
                ed.apply();
            }
            break;
            case R.id.radioButtonSetTheme2:{
                setTheme(R.style.DarkAppTheme);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Theme", R.style.DarkAppTheme);
                ed.apply();
            }
                break;

            default:
                break;
        }
    }
    public void checkClick(View view){
        CheckBox checkbox=(CheckBox)view;
        if (checkbox.isChecked()){
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean("saveFav", true);
            ed.apply();
            secondFalse();
        }
        else{
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean("saveFav", false);
            ed.apply();
            //secondTrue();
        }
    }
    private void secondFalse(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setCheckable(false);
    }
    private void secondTrue(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setCheckable(true);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
