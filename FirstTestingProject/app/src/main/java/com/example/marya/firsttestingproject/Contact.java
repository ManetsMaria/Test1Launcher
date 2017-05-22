package com.example.marya.firsttestingproject;

/**
 * Created by marya on 7.5.17.
 */

public class Contact {
    long id;
    String name;
    String phone;
    public Contact(int id, String name, String phone){
        this.id=id;
        this.name=name;
        this.phone=phone;
    }
    @Override
    public boolean equals(Object obj){
        Contact t=(Contact) obj;
        return name.equals(t.name);
    }
    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public void setName(String name){
        this.name=name;
    }
}
