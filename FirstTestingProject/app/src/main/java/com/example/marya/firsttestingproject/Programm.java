package com.example.marya.firsttestingproject;

/**
 * Created by marya on 29.3.17.
 */
class Programm implements Comparable <Programm> {
    int name;
    int photoId;
    int count;
    Programm(int name, int photoId) {
        this.name = name;
        this.photoId = photoId;
        count=0;
    }
    @Override
    public int compareTo(Programm t) {
        return count-t.count;
    }

    public boolean equals(Object other){
        if (!super.equals(other))
           return false;
        if(this==other)
            return true;
        if (other==null) return false;
        if (this.getClass()!=other.getClass())
            return false;
        Programm t=(Programm) other;
        return name==t.name;
    }
    public int hashCode(){
        return name;
    }
}