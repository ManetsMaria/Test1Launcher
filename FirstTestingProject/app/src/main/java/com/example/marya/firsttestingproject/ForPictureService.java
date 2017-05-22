package com.example.marya.firsttestingproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marya on 21.5.17.
 */

public class ForPictureService extends Service {

    private ImageLoader mImageLoader;
    private AsyncTask<Void, Void, Bitmap> mAsyncTask;

    public int onStartCommand(Intent intent, int flags, int startId) {
        //String url = intent.getStringExtra("url");
        mImageLoader = new ImageLoader();
       // Log.d("Yaservice", url);
        if (startId == 1) {
            mAsyncTask = new MyAsyncTask().execute();
        }
        else {
            stopSelf();
            sendIntent(0);
            return START_NOT_STICKY;
        }
        return START_STICKY;
    }
    private void sendIntent(int status) {
        Intent intent = new Intent("Loading");
        intent.putExtra("status", status);
        sendBroadcast(intent);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Nullable
    private Bitmap loadImage() {
        Bitmap bitmapDrawable = null;
        final String imageUrl = mImageLoader.getImageUrl(getApplicationContext());
        if (TextUtils.isEmpty(imageUrl) == false) {
            final Bitmap bitmap = mImageLoader.loadBitmap(imageUrl);
            bitmapDrawable = bitmap;
        }

        return bitmapDrawable;
    }
    private class MyAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private SharedPreferences sharedPreferences;

        @Override
        protected void onPreExecute() {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            long time = System.currentTimeMillis();
            if (isNewDay(time)) {
                sharedPreferences.edit().putLong("day", calcDay(time)).apply();
                sharedPreferences.edit().putInt("counter", 0).apply();
               // mImageLoader.getImageUrls(getApplicationContext());
            }
        }

        private long calcDay(long time) {
            return (time - 9 *1000*3600 ) / 1000*3600*24;
        }
        private boolean isNewDay(long time) {
            long day = sharedPreferences.getLong("day", 0);
            return calcDay(time) > day;
        }
        @Override
        protected Bitmap doInBackground(final Void... params) {
            Bitmap drawable = null;
            if (isCancelled() == false) {
                drawable = loadImage();
            }

            return drawable;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmapDrawable) {
            try {
                if (bitmapDrawable!=null) {
                    Log.d("urls","null");
                    FileOutputStream stream = openFileOutput("bitmap.png", Context.MODE_PRIVATE);
                    bitmapDrawable.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();
                    ByteArrayOutputStream str = new ByteArrayOutputStream();
                    bitmapDrawable.compress(Bitmap.CompressFormat.PNG, 100, str);
                    byte[] byteArray = str.toByteArray();
                    String saveThis = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    SharedPreferences sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
                    sPref.edit().putString("image", saveThis);
                    Log.d("picture", String.valueOf(byteArray));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException a){
                a.printStackTrace();
            }
            //super.onPostExecute(bitmapDrawable);
        }
    }
}
