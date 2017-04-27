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
    @Override
    public int compareTo(Programm t) {
        return count-t.count;
    }

    public boolean equals(Object other){
        Programm t=(Programm) other;
        return label.equals(t.label);
    }
}