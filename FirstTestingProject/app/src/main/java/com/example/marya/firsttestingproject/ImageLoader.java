package com.example.marya.firsttestingproject;

/**
 * Created by marya on 22.5.17.
 */

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.util.Log;
        import android.util.Xml;

        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserException;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLConnection;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Locale;
        import java.util.Random;

        import static android.content.Context.MODE_PRIVATE;

class ImageLoader {

    private static final Object mLock = new Object();
    private static volatile ImageLoader sInstance;
    List<String> mImageUrls = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Nullable
    private String processEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                    && "img".equals(parser.getName())) {
                for (int i = 1; i < parser.getAttributeCount(); i++) {
                    if ("size".equals(parser.getAttributeName(i))) {
                        if ("XXXL".equals(parser.getAttributeValue(i))) {
                            return parser.getAttributeValue(i-1);
                        }
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    Bitmap loadBitmap(String srcUrl) {
        try {
            URL url = new URL(srcUrl);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte [] bitmap = buffer.toByteArray();
            Log.d("array", "here");
            return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("array", "here");
        }
        catch (OutOfMemoryError a){

        }
        return null;
    }

    @Nullable
    String getImageUrl(Context context) {
        sharedPreferences = context.getSharedPreferences("mysettings",MODE_PRIVATE);
        int counter=sharedPreferences.getInt("counter", 0);
        mImageUrls=getImageUrls();
        if (!mImageUrls.isEmpty()) {
            //int index = new Random().nextInt(mImageUrls.size());
            String irl = mImageUrls.get(counter);
            counter++;
            sharedPreferences.edit().putInt("counter", counter).apply();
            Log.d("urls",irl);
            Log.d("urls",String.valueOf(sharedPreferences.getInt("counter", 0)));
            return irl;
        }
        return null;
    }

    @NonNull
    public List<String> getImageUrls() {
        if (mImageUrls.isEmpty()) {
            try {
                Log.d("urls","обновилось");
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                final String formattedDate = dateFormat.format(calendar.getTime());

                final String stringUrl = "http://api-fotki.yandex.ru/api/podhistory/poddate;" + formattedDate + "T12:00:00Z/?limit=100";
                final URL url = new URL(stringUrl);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    final InputStream stream = connection.getInputStream();
                    final XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream, null);
                    String imgUrl;

                    while ((imgUrl = processEntry(parser)) != null) {
                        mImageUrls.add(imgUrl);

                    }

                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        return mImageUrls;
    }
}
