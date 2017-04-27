package com.example.marya.firsttestingproject;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yandex.metrica.YandexMetrica;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sPref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YandexMetrica.activate(getApplicationContext(), "4d56d3ed-8a27-4d06-b65f-a908b0e713ed");
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(getApplication());
        sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
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
}
