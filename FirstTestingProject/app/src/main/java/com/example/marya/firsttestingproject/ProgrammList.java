package com.example.marya.firsttestingproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProgrammList extends Activity {
    static int numbers;
    RVAdapter adapter;
    FragmentList fragment;
    static int column;
    static int theme;
    List<Programm>news;
    private Receiver br;
    List<Programm> popular;
    List<Programm> favorites;
    private PackageManager manager;
    private RecyclerView rv;
    FavoritesAdapter favoritesAdapter;
    List<Programm> programms;
    EditText editSearch;
    List<String> uries;
    static int screne;
    static int numberHints;
    AutoCompleteTextView textView;
    SharedPreferences sPref;
    SharedPreferences shareUri;
    SharedPreferences shareContacts;
    DBHelper dbHelper;
    SQLiteDatabase db;
    String str;
    String readPrevious;
    private static List<Contact>forFavorite;
    private static List<Contact> contacts=new ArrayList<>();
    String previousContact;
    GridLayoutManager llm;
    BroadcastReceiver broadcastReceiver;
    Intent intent;
    long lastUpdate;
    private Toast toast;
    private ImageView backgroundImage;
    public Bitmap bitmap;

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table mytable1 ("
                    + "uries text" + ");");
            db.execSQL("create table mytable2 ("
                    + "popular text" + ");");
            db.execSQL("create table mytable3 ("
                    + "favorite text" + ");");
            db.execSQL("create table mytable4 ("
                    + "contacts text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        sPref = getSharedPreferences("mysettings",MODE_PRIVATE);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        shareUri=getSharedPreferences("uries",MODE_PRIVATE);
        shareContacts=getSharedPreferences("contacts",MODE_PRIVATE);
        readFromDatabaseUri();
        fragment=(FragmentList) getLastNonConfigurationInstance();
        initData();
            popular=new ArrayList<>();
            favorites=new ArrayList<>();
            news=new ArrayList<>();
            forFavorite=new ArrayList<>();
            Log.d("zaeb", "fragment false");
            if (sPref.getBoolean("clearFav", false)){
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean("clearFav", false);
                ed.apply();
                Log.d("zaeb", "fragment false clearTrue");
                db.delete("mytable3", null, null);
            }
            else{
                Log.d("zaeb", "fragment false database");
                Cursor c = db.query("mytable3", null, null, null, null, null, null);
                if (c.moveToFirst()) {
                    int index = c.getColumnIndex("favorite");
                    do {
                        str=c.getString(index);
                    } while (c.moveToNext());
                    List<String> fav = Arrays.asList(str.split(";"));
                    for (int i=0; i<fav.size(); i++){
                        int j=indexOf(programms, new Programm("h", getResources().getDrawable(R.drawable.ic_adb_black), fav.get(i)));
                        if (j>=0){
                            favorites.add(programms.get(j));
                        }
                    }
                } else
                    c.close();
            }
            Cursor c = db.query("mytable2", null, null, null, null, null, null);
            if (c.moveToFirst()) {
                int index = c.getColumnIndex("popular");
                do {
                    str=c.getString(index);
                } while (c.moveToNext());
                Log.d("dontknow", str);
                List<String> fav = Arrays.asList(str.split(";"));
                Log.d("dontknow", String.valueOf(fav.size()));
                Log.d("dontknow", fav.toString());
                for (int i=0; i<fav.size(); i++){
                    List<String> stl=Arrays.asList(fav.get(i).split(":"));
                    String name=stl.get(0);
                    int ind=Integer.valueOf(stl.get(1));
                    int j=indexOf(programms, new Programm("h", getResources().getDrawable(R.drawable.ic_adb_black), name));
                    Log.d("dontknow", String.valueOf(j));
                    if (j>=0){
                        Programm p=programms.get(j);
                        p.count=ind;
                        Log.d("dontknow", "add");
                        popular.add(p);
                    }
                }
            } else{
                c.close();
                Log.d("dontknow", "hbn");
            }
                previousContact=shareContacts.getString("allContacts","");
                if (!previousContact.equals("")) {
                List<String> fav = Arrays.asList(previousContact.split(";"));
                Log.d("gh", String.valueOf(fav.size()));
                Log.d("gh", fav.toString());
                    for (int i = 0; i < fav.size(); i++) {
                        int j = 0;
                        Log.d("gh", String.valueOf(fav.size()));
                        int id = Integer.valueOf(fav.get(i));
                        while (j < contacts.size() && contacts.get(j).id != id) {
                            j++;
                        }
                        if (j < contacts.size())
                            forFavorite.add(contacts.get(j));
                    }
                }

        /*else{
            Log.d("zaeb", "fragment true clearTrue");
            popular=fragment.getPopular();
            news=fragment.getNews();
            if (sPref.getBoolean("clearUri", false)){
                uries=new ArrayList<>();
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean("clearUri", false);
                ed.apply();
                Log.d("zaeb", "fragment true clearTrue");
            }
            else {
                uries = fragment.getUries();
                Log.d("zaeb", "fragment true getUry");
            }
            if (sPref.getBoolean("clearFav", false)){
                favorites=new ArrayList<>();
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean("clearFav", false);
                ed.apply();
                Log.d("zaeb", "fragment true clearTrue");
            }
            else {
                favorites = fragment.getFavorites();
                Log.d("zaeb", "fragment true getUry");
            }
        }*/
        Log.d("hueri", uries.toString());
        column=sPref.getInt("Column", R.integer.columnstandart);
        theme=sPref.getInt("Theme", R.style.AppTheme);
        intent=getIntent();
        screne=intent.getIntExtra("screneNumber", 0);
        lastUpdate = sPref.getLong("lastUpdate", 0);
        setTheme(theme);

        setContentView(R.layout.activity_programm_list);
        initBackground();
        if (checkUpdateTime()) {
            initUpdateBackground(0);
            //setPictureBackground();
        }
        rv = (RecyclerView) findViewById(R.id.rv);
        llm = new GridLayoutManager(this, getResources().getInteger(column));
        llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 3:
                        return getResources().getInteger(column);
                }
                return 1;
            }
        });
        rv.setLayoutManager(llm);
        Log.d("popular", popular.toString());
        adapter=new RVAdapter(programms,popular,news,favorites, ProgrammList.this, getResources().getInteger(column),manager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        if (sPref.getBoolean("saveFav", false))
            secondFalse();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                setfirst();
                                initBackground();
                                if (checkUpdateTime()) {
                                    initUpdateBackground(0);
                                    //setPictureBackground();
                                }
                                intent.putExtra("screneNumber", 0);
                                if (favoritesAdapter!=null) {
                                    popular=favoritesAdapter.getPopular();
                                    favorites=favoritesAdapter.getFavorites();
                                    adapter.setNewPopular(popular);
                                    adapter.setNewFavorites(favorites);
                                    adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                                }

                                rv.setAdapter(adapter);
                                break;
                            case R.id.action_settings:
                                if (checkUpdateTime()) {
                                    initUpdateBackground(0);
                                    //setPictureBackground();
                                }
                                Intent i=new Intent(ProgrammList.this, Setting.class);
                                startActivity(i);
                                break;
                            case R.id.action_navigation:
                                initBackground();
                                if (checkUpdateTime()) {
                                    initUpdateBackground(0);
                                    //setPictureBackground();
                                }
                                //Toast.makeText(ProgrammList.this, str, Toast.LENGTH_LONG).show();
                                intent.putExtra("screneNumber", 1);
                                if (!sPref.getBoolean("saveFav", false))
                                    makeSecond();
                                break;
                        }
                        return true;
                    }
                });
        //uries.add("test");
        textView = (AutoCompleteTextView) findViewById(R.id.editText);
        updateHints();
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                    readFromDatabaseUri();
                    updateHints();
                Log.d("hueri", "touch");
                    textView.showDropDown();
                    return false;
            }
        });
        editSearch = (EditText) textView;
        editSearch.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("hueri", "enter");
                    String uri=editSearch.getText().toString();
                Log.d("hueri", "uri without trim "+uri);
                    uri=uri.trim();
                    try {
                        Log.d("hueri", "try");
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        Log.d("hueri", uri);
                        Log.d("hueri", "before start act");
                        if (uri.length()>0) {
                            Log.d("make sure", String.valueOf(System.currentTimeMillis()/(1000 * 60 * 60 * 24)));
                            writeUri(uri);
                        }
                        startActivity(intent3);
                        Log.d("hueri", "after start act");
                    }
                    catch (ActivityNotFoundException e){
                        Toast.makeText(ProgrammList.this, "wrong uri", Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        Log.d("hueri", "finally");
                        updateHints();
                        editSearch.getText().clear();
                        editSearch.clearFocus();
                        Log.d("hueri", "clava");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                        return false;
                    }
            }
        });
        br = new Receiver();
        br.setMainActivityHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        this.registerReceiver(br, intentFilter);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left=2;
                outRect.right=2;
                outRect.bottom=20;
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (adapter.getItemViewType(position) == 3 &&
                            position % 2 == 1) {
                        outRect.left=0;
                        outRect.right=0;
                        outRect.bottom=0;
                    }
                }
            }

            Paint mPaint = new Paint();

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            }
        });
        Log.d("fav", String.valueOf(favorites.size()));
        if (screne==1){
            Log.d("fav", String.valueOf(favorites.size())+" screne1");
            makeSecond();
            llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
        }
    }
    public void setfirst(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        item=menu.getItem(2);
        item.setChecked(false);
        llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 3:
                        return getResources().getInteger(column);
                }
                return 1;
            }
        });
    }
    public void makeSecond(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);
        item=menu.getItem(0);
        item.setChecked(false);
        favorites=adapter.getFavorites();
        popular=adapter.getPopular();
        Log.d("fav", String.valueOf(favorites.size())+" favadapt");
        favoritesAdapter=new FavoritesAdapter(favorites,popular,ProgrammList.this,getResources().getInteger(column),manager, forFavorite);
        rv.setAdapter(favoritesAdapter);
        llm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
    }
    public Object onRetainNonConfigurationInstance() {
        fragment=new FragmentList();
        popular=adapter.getPopular();
        news=adapter.getNews();
        favorites=adapter.getFavorites();
        fragment.save(column,theme,popular, news, favorites,uries);
        return fragment;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        favorites=adapter.getFavorites();
        popular=adapter.getPopular();
        ContentValues cv = new ContentValues();
        String eba="";
        for (int i=0; i<popular.size();i++){
            eba=eba+popular.get(i).label+":"+popular.get(i).count+";";
        }
        cv.put("popular",eba);
        long rowID = db.insert("mytable2", null, cv);
        cv = new ContentValues();
        eba="";
        for (int i=0; i<favorites.size();i++){
            eba=eba+favorites.get(i).label+";";
        }
        cv.put("favorite",eba);
        rowID = db.insert("mytable3", null, cv);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("zaeb", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("zaeb", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("zaeb", "onStart");
    }

    @Override
    protected void onStop() {
        favorites=adapter.getFavorites();
        popular=adapter.getPopular();
        String eba="";
        ContentValues cv = new ContentValues();
        eba="";
        for (int i=0; i<popular.size();i++){
            eba=eba+popular.get(i).label+":"+popular.get(i).count+";";
        }
        cv.put("popular",eba);
        long rowID = db.insert("mytable2", null, cv);
        cv = new ContentValues();
        eba="";
        for (int i=0; i<favorites.size();i++){
            eba=eba+favorites.get(i).label+";";
        }
        cv.put("favorite",eba);
        rowID = db.insert("mytable3", null, cv);
        super.onStop();
    }
    public void initData(){
        manager = getPackageManager();
        programms = new ArrayList<Programm>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            Programm app = new Programm((String)ri.loadLabel(manager), ri.activityInfo.loadIcon(manager), ri.activityInfo.packageName);
            Log.d("packet",app.label);
            if (ri.activityInfo.packageName.compareTo("com.example.marya.firsttestingproject")!=0 && indexOf(programms, app)<0) {
                programms.add(app);
            }
        }
        try {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            startManagingCursor(cursor);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Contact contact = new Contact(Integer.valueOf(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                    contacts.add(contact);
                }
            }
        }
        catch(SecurityException e){
        }
    }
    public void callInserting(String name) {
            initData();
            adapter.add(name, programms);
    }

    public void callRemoving(String name) {
        Log.d("delete", name);
        adapter.remove(name);
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    private void updateHints(){
        numberHints=sPref.getInt("numberHints", 2);
        Log.d("hueri", uries.toString());
        int i=uries.size()-1;
        int k=0;
        ArrayList<String> str=new ArrayList<>();
        while(i>=0 && k<numberHints){
            str.add(uries.get(i));
            i--;
            k++;
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);
        Log.d("hueri", str.toString());
        textView.setAdapter(adapter);
    }
    private void secondFalse(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setCheckable(false);
    }
    protected int indexOf(List<Programm> pr, Programm p){
        Iterator<Programm> it=pr.iterator();
        int index=0;
        while(it.hasNext()){
            if (it.next().equals(p))
                return index;
            index++;
        }
        return -1;
    }
    /*private void saveUri(){
        String eba="";
        ContentValues cv = new ContentValues();
        for (int i=0; i<uries.size();i++){
            eba=eba+uries.get(i)+"uriTime:"+String.valueOf(System.currentTimeMillis()/(1000*3600*24))+";";
        }
        cv.put("uries", eba);
        long rowID = db.insert("mytable1", null, cv);
    }*/
    public void readFromDatabaseUri(){
        uries=new ArrayList<>();
        readPrevious="";
        if (sPref.getBoolean("clearUri", false)){
            SharedPreferences.Editor ed = sPref.edit();
            readPrevious="";
            ed.putBoolean("clearUri", false);
            ed.apply();
            SharedPreferences.Editor urEd=shareUri.edit();
            urEd.putString("allUri","");
            urEd.apply();
            Log.d("zaeb", "fragment false clearTrue");
            //db.delete("mytable1", null, null);
        }
        else{
            Log.d("zaeb", "fragment false database");
            str=shareUri.getString("allUri","");
            //Cursor c = db.query("mytable1", null, null, null, null, null, null);
            //if (c.moveToFirst()) {
                //int index = c.getColumnIndex("uries");
                //do {
                  //  str=c.getString(index);
                    readPrevious=str;
                //} while (c.moveToNext());
                List<String> fav = Arrays.asList(str.split(";"));
                for (int i=0; i<fav.size(); i++){
                    List<String> stl=Arrays.asList(fav.get(i).split("uriTime:"));
                    String name=stl.get(0);
                    if (!name.equals("")){
                        uries.add(name);
                    }
                }
            //} else
              //  c.close();
        }
    }
    public void writeUri(String uri){
        int index = uries.indexOf(uri);
        if (index < 0) {
            Log.d("hueri", "add uri");
            uries.add(uri);
        }
        else{
            Log.d("hueri", "remove and add");
            uries.remove(index);
            uries.add(uri);
        }
        Log.d("ur", readPrevious);
        //ContentValues cv = new ContentValues();
            readPrevious=readPrevious+uri+"uriTime:"+String.valueOf(System.currentTimeMillis()/(1000 * 60 * 60 * 24))+";";
        //cv.put("uries", readPrevious);
        //long rowID = db.insert("mytable1", null, cv);
        SharedPreferences.Editor ed = shareUri.edit();
        ed.putString("allUri", readPrevious);
        ed.apply();
    }
    public void click(View view){
        Intent i=new Intent(this,ContactList.class);
        startActivity(i);
    }
    private void initBackground() {
        backgroundImage = (ImageView) findViewById(R.id.background);
        backgroundImage.setAlpha(0.5f);
        setPictureBackground();
    }
    public void initUpdateBackground(int flag) {
        if (broadcastReceiver == null) {
            initBroadcast();
        }
        Log.d("init", "now");
        intent = new Intent(this, ForPictureService.class);
        //intent.putExtra("url", "http://api-fotki.yandex.ru/api/podhistory/poddate/?limit=96");
        startService(intent);
        if (flag==0) {
            bitmap = decodeBitmap();
            if (bitmap != null) {
                backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                backgroundImage.setImageBitmap(bitmap);
                sPref.edit().putLong("lastUpdate", System.currentTimeMillis()).apply();
            }
        }
    }
    private boolean checkUpdateTime() {
        Log.d("time",String.valueOf(lastUpdate ));
        Log.d("time",String.valueOf(System.currentTimeMillis()));
        Log.d("time",String.valueOf(getCurrentInterval()));
        return System.currentTimeMillis() - lastUpdate > getCurrentInterval();
    }
    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra("status", 0);
                if (status == 1) {
                }
                else if (status == 0) {
                }
            }
        };
        IntentFilter intFilt = new IntentFilter("Loading");
        registerReceiver(broadcastReceiver, intFilt);
    }
    private void showMessage(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void setPictureBackground() {
            //backgroundImage.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_cloud_black));
            bitmap = decodeBitmap();
       // byte[] array = Base64.decode(sPref.getString("image",""), Base64.DEFAULT);
       // bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            if (bitmap != null) {
                backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                backgroundImage.setImageBitmap(bitmap);
            }
    }
    @Nullable
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
            /*byte[] array = Base64.decode(sPref.getString("image",""), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
            backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            backgroundImage.setImageBitmap(bitmap);
            Log.d("picture", String.valueOf(array));*/
            //initUpdateBackground(1);
        }
        return null;
    }
    public long getCurrentInterval() {
        return (60 * 60 * 1000) / 4;
    }
}