package com.example.marya.firsttestingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by marya on 29.3.17.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ProgrammViewHolder> {
    //static List<Programm> sortProgramm;
    static int column;
    static List<Programm> favorites;
    static List<Programm>popular;
    PackageManager manager;
    public static class ProgrammViewHolder extends RecyclerView.ViewHolder{

        public TextView programmName;
        public ImageView programmPhoto;
        Context context;
        RelativeLayout cv;

        public ProgrammViewHolder(View itemView, Context context) {
            super(itemView);
            this.context=context;
            cv = (RelativeLayout) itemView.findViewById(R.id.cv);
            programmName = (TextView)itemView.findViewById(R.id.programm_name);
            programmPhoto = (ImageView)itemView.findViewById(R.id.programm_photo);
        }
    }
    Context context;
    FavoritesAdapter(List<Programm>fav,List<Programm>popular, Context context, int col, PackageManager manager){
        this.context=context;
        this.manager=manager;
        column=col;
        favorites=fav;
        this.popular=popular;
    }
    public List<Programm> getFavorites(){
        return favorites;
    }
    public List<Programm> getPopular(){return popular;}
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProgrammViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        //  ProgrammViewHolder pvh = new ProgrammViewHolder(v, context);
        int width = viewGroup.getMeasuredWidth() / (column);
        v.setMinimumWidth(width);
        //v.setMinimumHeight(0);
        return new ProgrammViewHolder(v, context);
    }
    @Override
    public void onBindViewHolder(final ProgrammViewHolder personViewHolder, final int i) {
                Log.d("here", "1");
                    personViewHolder.programmName.setText(favorites.get(i).name);
                personViewHolder.programmPhoto.setImageDrawable(favorites.get(i).photoId);
                personViewHolder.cv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle(R.string.select_action);
                        menu.add("delete from favorites").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                favorites.remove(favorites.get(i));
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(0,getItemCount());
                                return true;
                            }

                        });
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Programm p = favorites.get(i);
                        int index=indexOf(popular,p);
                        if (index>=0){
                            p=popular.get(index);
                            popular.remove(index);
                        }
                        p.count++;
                        int flag=-1;
                        for (int i=0; i<7; i++){
                            if (popular.get(i).count<p.count){
                                popular.add(i,p);
                                flag=i;
                                break;
                            }
                        }
                        if (flag>=0){
                            popular.add(p);
                        }
                        Intent i = manager.getLaunchIntentForPackage(p.label);
                        context.startActivity(i);
                        //Toast.makeText(context, p.label, Toast.LENGTH_SHORT).show();
                    }
                };
                personViewHolder.cv.setOnClickListener(listener);
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
    @Override
    public int getItemCount() {
        return favorites.size();
    }
}