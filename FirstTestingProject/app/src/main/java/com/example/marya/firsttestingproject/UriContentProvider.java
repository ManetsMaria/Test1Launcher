package com.example.marya.firsttestingproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.checkCallingPermission;

/**
 * Created by marya on 9.5.17.
 */

public class UriContentProvider extends ContentProvider {
    String lastUri;
    List<String> lastDayUri;
    List <String>allUri;
    private final UriMatcher sUriMatcher;
    SharedPreferences shareUri;

    public UriContentProvider() {
                Log.d("check", "here");
                sUriMatcher = new UriMatcher(0);
                sUriMatcher.addURI("com.example.marya.firsttestingproject", "uri/last_value", 1);
                sUriMatcher.addURI("com.example.marya.firsttestingproject", "uri/values", 2);
                sUriMatcher.addURI("com.example.marya.firsttestingproject", "uri", 3);
                sUriMatcher.addURI("com.example.marya.firsttestingproject", "uri/last_day_values", 4);
            }
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d("check", "n");
        lastUri="";
        allUri=new ArrayList<>();
        lastDayUri=new ArrayList<>();
        shareUri=getContext().getSharedPreferences("uries",MODE_PRIVATE);
        String str=shareUri.getString("allUri", "");
        List<String> fav = Arrays.asList(str.split(";"));
        for (int i=0; i<fav.size(); i++){
            List<String> stl=Arrays.asList(fav.get(i).split("uriTime:"));
            String name=stl.get(0);
            if (!name.equals("")){
                allUri.add(name);
                lastUri=name;
                Log.d("now1", String.valueOf(System.currentTimeMillis()/(1000 * 60 * 60 * 24)));
                Log.d("now2", stl.get(1));
                Log.d("now3",String.valueOf(Integer.valueOf(stl.get(1))==System.currentTimeMillis()/(1000 * 60 * 60 * 24)));
                if (Integer.valueOf(stl.get(1))==System.currentTimeMillis()/(1000 * 60 * 60 * 24)){
                    Log.d("now4", name);
                    lastDayUri.add(name);
                }
            }
        }
        List<String> result = new ArrayList<>();
                switch (sUriMatcher.match(uri)) {
                        case 1:
                                result.add(lastUri);
                                break;
                        case 2: {
                            if (getContext() != null && checkCallingPermission(getContext(),
                                        "com.example.marya.firsttestingproject.permission.READ_ALL",
                                        null) == PERMISSION_GRANTED)
                                    result = allUri;
                            else{
                                result=null;
                            }
                            break;
                        }
                        case 4:
                                result = lastDayUri;
                                break;
                        default:
                            result = null;
                                break;

                            }
                final List<String> returnValue = result;
                if (returnValue != null) {
                        Cursor cursor = new MatrixCursor(new String[]{""}) {{
                                for (int i = 0; i < returnValue.size(); i++) {
                                        newRow().add(returnValue.get(i));
                                    }
                            }
            };
                        return cursor;
                    }
                return null;
            }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(sUriMatcher.match(uri) == 3) {
                        String name = values.getAsString("value");
                        SharedPreferences.Editor ed=shareUri.edit();
                        String readPrevious=shareUri.getString("allUri", "");
                        readPrevious=readPrevious+name+"uriTime:"+String.valueOf(System.currentTimeMillis()/(1000 * 60 * 60 * 24))+";";
                        ed.putString("allUri", readPrevious);
                        ed.apply();
                        return  uri;
        }
                return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }
}
