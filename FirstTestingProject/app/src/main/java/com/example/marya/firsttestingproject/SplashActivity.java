package com.example.marya.firsttestingproject;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.io.FileInputStream;
import java.io.IOException;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sPref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YandexMetrica.activate(getApplicationContext(), "4d56d3ed-8a27-4d06-b65f-a908b0e713ed");
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(getApplication());
        sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
       // Intent i = new Intent(this, ForPictureService.class);
        //startService(i);
        if (checkUpdateTime()) {
            initUpdateBackground();
            //setPictureBackground();
        }
        boolean isWelcome = sPref.getBoolean("isWelcome", true);
        if (!isWelcome){
            Intent intent = new Intent(this, ProgrammList.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
    public void initUpdateBackground() {

        Intent intent = new Intent(this, ForPictureService.class);
        //intent.putExtra("url", "http://api-fotki.yandex.ru/api/podhistory/poddate/?limit=96");
        startService(intent);
        Bitmap bitmap = decodeBitmap();
        if (bitmap != null) {
            sPref.edit().putLong("lastUpdate", System.currentTimeMillis()).apply();
        }
       //
    }
    private boolean checkUpdateTime() {
        Long lastUpdate = sPref.getLong("lastUpdate", 0);
        return System.currentTimeMillis() - lastUpdate > getCurrentInterval();
    }

    public long getCurrentInterval() {
        return (60 * 60 * 1000) / 4;
    }
    public Bitmap decodeBitmap() {
        try {
            FileInputStream is = openFileInput("bitmap.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError a){
            Toast.makeText(this, "сервис перегружен, пожалуйста, запустите приложение ещё раз", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
