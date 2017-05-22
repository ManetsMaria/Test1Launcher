package com.example.marya.firsttestingproject;

import android.graphics.drawable.Drawable;

/**
 * Created by marya on 29.3.17.
 */
class Programm implements Comparable <Programm> {
    String name;
    Drawable photoId;
    int count;
    String label;
    Programm(String name, Drawable photoId, String label) {
        this.name = name;
        this.photoId = photoId;
        this.label=label;
        count=0;
    }
    Programm(String name, String label, int count) {
        this.name = name;
        this.photoId = null;
        this.label=label;
        this.count=count;
    }
    @Override
    public int compareTo(Programm t) {
        return count-t.count;
    }

    @Override
    public boolean equals(Object other){
        Programm t=(Programm) other;
        return label.equals(t.label);
    }
    public String getName(){
        return name;
    }
    public Drawable getPhotoId(){
        return photoId;
    }
    public int getCount(){
        return count;
    }
    public String getLabel(){
        return label;
    }
    @Override
    public String toString(){
        return name+" "+String.valueOf(count);
    }

}