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
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProgrammViewHolder> {
    //static List<Programm> sortProgramm;
    static int column;
    static List<Programm> news;
    static List<Programm> programms;
    static List<Programm> popular;
    static List<Programm> favorites;
    PackageManager manager;
    public static class ProgrammViewHolder extends RecyclerView.ViewHolder{

        public TextView programmName;
        public ImageView programmPhoto;
        Context context;
        RelativeLayout cv;
        LinearLayout span;

        public ProgrammViewHolder(View itemView, Context context, int i) {
            super(itemView);
            this.context=context;
            Log.d("check", "constructor"+String.valueOf(i));
            if (i==3){
            span=(LinearLayout)itemView.findViewById(R.id.span);
               programmName=(TextView) itemView.findViewById(R.id.diveder_text);
            }
            else {
                cv = (RelativeLayout) itemView.findViewById(R.id.cv);
                programmPhoto = (ImageView) itemView.findViewById(R.id.programm_photo);
                programmName = (TextView) itemView.findViewById(R.id.programm_name);
            }
        }
    }
    Context context;
    RVAdapter(List<Programm> prog,List<Programm>pop,List<Programm> ne,List<Programm>fav, Context context, int col, PackageManager manager){
        this.context=context;
        this.manager=manager;
        column=col;
        programms=prog;
        if (pop.isEmpty()){
            popular=new ArrayList<>(programms);
        }
        else{
            popular=pop;
        }
        if (ne.isEmpty()) {
            news = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                news.add(programms.get(programms.size() - i));
            }
        }
        else{
            news=ne;
        }
        favorites=fav;
    }
    public List<Programm> getPopular(){
        return popular;
    }
    public List<Programm> getNews(){
        return news;
    }
    public List<Programm> getFavorites(){
        return favorites;
    }
    public List<Programm> getProgramms() {return programms;}
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProgrammViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.d("check", "create"+String.valueOf(i)+" "+String.valueOf(getItemViewType(i)));
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        //  ProgrammViewHolder pvh = new ProgrammViewHolder(v, context);
        int width = viewGroup.getMeasuredWidth() / (column);
        v.setMinimumWidth(width);
        //v.setMinimumHeight(0);
        if (i==3){
            v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.span, viewGroup,false);
        }
        return new ProgrammViewHolder(v, context, i);
    }
    @Override
    public int getItemViewType(int i){
        if (i==0 || i==column+1 || i==column*2+2)
            return 3;
        if (i<=column)
            return 0;
        if (i>=column && i<=column*2+1)
            return 1;
        return 2;
    }
    @Override
    public void onBindViewHolder(final ProgrammViewHolder personViewHolder, final int i) {
        int type=getItemViewType(i);
        switch (type) {
            case 3:{
                if (i==0)
                personViewHolder.programmName.setText("popular");
                if (i==column+1)
                    personViewHolder.programmName.setText("new");
                if (i==column*2+2)
                    personViewHolder.programmName.setText("my application");
                break;
            }
            case 2: {
                    personViewHolder.programmName.setText(programms.get(i-2*column-3).name);
                    personViewHolder.programmPhoto.setImageDrawable(programms.get(i-2*column-3).photoId);

                    personViewHolder.cv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle(R.string.select_action);
                        menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextDelete(programms.get(i-2*column-3));
                                return true;
                            }
                        });
                        menu.add(R.string.info).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextInfo(programms.get(i-2*column-3));
                                return true;
                            }
                        });
                        menu.add("to favorite").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextFavorite(programms.get(i-2*column-3));
                                return true;
                            }
                        });
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quikClick(programms.get(i-2*column-3));
                    }
                };
                personViewHolder.cv.setOnClickListener(listener);
                break;
            }
            case 0:{
                Log.d("here", "0");
                personViewHolder.programmName.setText(popular.get(i-1).name);
                personViewHolder.programmPhoto.setImageDrawable(popular.get(i-1).photoId);
                personViewHolder.cv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle(R.string.select_action);
                        menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextDelete(popular.get(i-1));
                                return true;
                            }
                        });
                        menu.add(R.string.info).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextInfo(popular.get(i-1));
                                return true;
                            }
                        });
                        menu.add("to favorite").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextFavorite(popular.get(i-1));
                                return true;
                            }
                        });
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quikClick(popular.get(i-1));
                    }
                };
                personViewHolder.cv.setOnClickListener(listener);
                break;
            }
            case 1:{
                    personViewHolder.programmName.setText(news.get(i-column-2).name);
                personViewHolder.programmPhoto.setImageDrawable(news.get(i-column-2).photoId);
                personViewHolder.cv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle(R.string.select_action);
                        menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextDelete(news.get(i-column-2));
                                return true;
                            }
                        });
                        menu.add(R.string.info).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextInfo(news.get(i-column-2));
                                return true;
                            }
                        });
                        menu.add("to favorite").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                contextFavorite(news.get(i-column-2));
                                return true;
                            }
                        });
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quikClick(news.get(i-column-2));
                    }
                };
                personViewHolder.cv.setOnClickListener(listener);
                break;
            }
        }
    }
    public void add(String label, List<Programm> prog){
        programms=prog;
        int i=indexOf(programms,new Programm("name",context.getResources().getDrawable(R.drawable.angrybirds2),label ));
        Programm p =programms.get(i);
        popular.add(p);
        news.remove(news.size()-1);
        news.add(0, p);
        notifyItemRangeChanged(column,getItemCount());
    }
    public void remove(String label){
        int i=indexOf(programms,new Programm("name",context.getResources().getDrawable(R.drawable.angrybirds2),label ));
        Programm p=programms.get(i);
        notifyItemRemoved(i+2*column+3);
        int index=indexOf(popular,p);
        int ind=indexOf(favorites,p);
        if (ind>=0)
            favorites.remove(ind);
        Log.d("delete",String.valueOf(index));
        if (index>0 && index<=column) {
            notifyItemRemoved(index);
        }
        if (index>=0){
            popular.remove(index);
        }
        programms.remove(i);
        if (indexOf(news, p)>=0){
            news.remove(p);
        }
        int k=programms.size()-1;
        while(indexOf(news,programms.get(k))>=0){
            k--;
        }
        news.add(programms.get(k));
        notifyItemRangeChanged(0,getItemCount());
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
    private void contextDelete(Programm p){
        Log.d("delete", "1");
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:"+p.label));
        context.startActivity(intent);
    }
    private void contextInfo(Programm p){
        int index=indexOf(popular,p);
        if (index>0){
            p=popular.get(index);
            popular.remove(index);
            if (index<column)
                notifyItemRangeChanged(1, column+1);
        }

        p.count++;
        int flag=-1;
        for (int i=0; i<=7; i++){
            if (popular.get(i).count<p.count){
                popular.add(i,p);
                flag=i;
                break;
            }
        }
        if (flag>0 && flag<=column){
            notifyItemRangeChanged(1, column+1);
        }
        else {
            popular.add(p);
        }
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+p.label));
        context.startActivity(intent);
    }
    private void quikClick(Programm p){
        int index=indexOf(popular,p);
        if (index>=0){
            p=popular.get(index);
            popular.remove(index);
            if (index<=column)
                notifyItemRangeChanged(1,column+1);
        }

        p.count++;
        int flag=-1;
        for (int i=0; i<=7; i++){
            if (popular.get(i).count<p.count){
                popular.add(i,p);
                flag=i;
                break;
            }
        }
        if (flag>=0 && flag<column){
            notifyItemRangeChanged(1, column+1);
        }
        else {
            popular.add(p);
        }
        Intent i = manager.getLaunchIntentForPackage(p.label);
        context.startActivity(i);
        //Toast.makeText(context, p.label, Toast.LENGTH_SHORT).show();
    }
    private void contextFavorite(Programm p){
        if (indexOf(favorites,p)<0)
            favorites.add(p);
        int index=indexOf(popular,p);
        if (index>=0){
            p=popular.get(index);
            popular.remove(index);
            if (index<=column)
                notifyItemRangeChanged(1, column+1);
        }

        p.count++;
        int flag=-1;
        for (int i=0; i<=7; i++){
            if (popular.get(i).count<p.count){
                popular.add(i,p);
                flag=i;
                break;
            }
        }
        if (flag>=0 && flag<column){
            for (int j=0; j<column; j++){
                notifyItemRangeChanged(1, column+1);
            }
        }
        else {
            popular.add(p);
        }

    }
    public void setNewPopular(List <Programm> popular){
        this.popular=popular;
    }
    public void setNewFavorites(List <Programm> favorites){
        this.favorites=favorites;
    }
    @Override
    public int getItemCount() {
        return programms.size()+2*column+3;
    }
}