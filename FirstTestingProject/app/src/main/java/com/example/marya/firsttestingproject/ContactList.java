package com.example.marya.firsttestingproject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactList extends ListActivity {
    private static List<Contact> contacts=new ArrayList<>();
    SharedPreferences shareContacts;
    String previousContact;
    private static List<Contact>forFavorite=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    String[]str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareContacts=getSharedPreferences("contacts",MODE_PRIVATE);
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
        int k=0;
        str=new String[contacts.size()-forFavorite.size()];
        for (int i=0; i<contacts.size(); i++){
            int j=0;
            while(j<forFavorite.size() && !forFavorite.get(j).name.equals(contacts.get(i).name))
                j++;
            if (j==forFavorite.size()) {
                str[k]=contacts.get(i).name;
                k++;
            }
        }
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, str);
        setListAdapter(mAdapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //
        Log.d("Click", "0");
        int i=0;
        while(!contacts.get(i).name.equals(str[position]))
            i++;
        Log.d("Click", "1");
        previousContact=previousContact+String.valueOf(contacts.get(i).id)+";";
        Log.d("Click", "2");
        SharedPreferences.Editor ed = shareContacts.edit();
        Log.d("Click", "3");
        ed.putString("allContacts", previousContact);
        Log.d("Click", "4");
        ed.apply();
        Log.d("Click", "5");
        Intent intent=new Intent(this, ProgrammList.class);
        intent.putExtra("screneNumber", 1);
        Log.d("Click", "6");
        startActivity(intent);
        Log.d("Click", "7");
        super.onListItemClick(l, v, position, id);
    }
}
