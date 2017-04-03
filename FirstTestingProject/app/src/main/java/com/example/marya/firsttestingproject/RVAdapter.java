package com.example.marya.firsttestingproject;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
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
    static List<Integer> previous;
    //static List<Programm> deleting;
    static List<Programm> programms;
    static  List<Programm> popular;
    private static TreeSet<Integer> ss;
    final Random random = new Random();
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
    RVAdapter(List<Programm>pop,List<Programm> ne, List<Programm> prog,Context context, int col){
        this.context=context;
        ss=new TreeSet<>();
        column=col;
        if (prog.isEmpty()) {
            news = new ArrayList<Programm>();
            previous = new ArrayList<>(column);
            programms=new ArrayList<Programm>();
            for (int i = 0; i < 5000; i++) {
                programms.add(new Programm(i, image(i)));
            }
            popular = new ArrayList<>(programms);
            for (int i=0; i<7; i++){
                news.add(programms.get(i));
            }
        }
        else{
            news=ne;
            popular=pop;
            programms=prog;
        }
    }
    public List<Programm> getPopular(){
        return popular;
    }
    public List<Programm> getNews(){
        return news;
    }
    public List<Programm> getProgramms() {return programms;}
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
    public int getItemViewType(int i){
        if (i<column)
            return 0;
        if (i>=column && i<column*2)
            return 1;
        return 2;
    }
    @Override
    public void onBindViewHolder(final ProgrammViewHolder personViewHolder, final int i) {
        int type=getItemViewType(i);
        switch (type) {
            case 2: {
                if(i-2*column<programms.size()){
                    if (i-2*column==0) {
                        personViewHolder.programmName.setText(context.getString(R.string.myAp)+" "+name(programms.get(i - 2 * column).name));
                    }
                    else{
                        personViewHolder.programmName.setText(name(programms.get(i-2*column).name));
                    }
                    personViewHolder.programmPhoto.setImageResource(programms.get(i-2*column).photoId);
                }
                else{
                    Log.d("here", "2");
                    int next=0;
                    if (!programms.isEmpty()) {
                        next = programms.get(programms.size() - 1).name + 1;
                    }
                    int image=image(next);
                    programms.add(new Programm(next,image));
                    personViewHolder.programmName.setText(name(next));
                    personViewHolder.programmPhoto.setImageResource(image);
                }
                personViewHolder.cv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v,
                                                    ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle(R.string.select_action);
                        menu.add(R.string.delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                /*notifyItemRemoved(personViewHolder.getAdapterPosition());
                                int index=indexOf(popular,new Programm(personViewHolder.programmName.getText().toString(),1));
                                if (index>=0 && index<column){
                                    notifyItemRemoved(index);
                                }
                                if (index>0){
                                    popular.remove(index);
                                }
                                deleting.add(new Programm(personViewHolder.programmName.getText().toString(),1));*/
                               // sortProgramm.remove()
                                //notifyItemRemoved(i);
                                //programms.remove(i-2*column);
                                //notifyItemRangeChanged(i, getItemCount());
                                //sortProgramm.remove(new Programm(name(i-2*column),1));
                                Log.d("delete","start");
                                notifyItemRemoved(i);
                                Log.d("delete",String.valueOf(i-2*column));
                                Log.d("delete",String.valueOf(personViewHolder.getAdapterPosition()));
                                int index=indexOf(popular,programms.get(i-2*column));
                                Log.d("delete",String.valueOf(index));
                                if (index>=0 && index<column) {
                                    notifyItemRemoved(index);
                                }
                                if (index>=0){
                                    popular.remove(index);
                                }
                                programms.remove(i-2*column);
                                notifyItemRangeChanged(0,getItemCount());
                                return true;
                            }
                        });
                        menu.add(R.string.info).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Programm p=programms.get(i-2*column);
                                int index=indexOf(popular,p);
                                if (index>=0){
                                    p=popular.get(index);
                                    popular.remove(index);
                                    if (index<column)
                                        notifyItemChanged(index);
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
                                if (flag>=0 && flag<column){
                                    for (int j=0; j<column; j++){
                                        notifyItemChanged(j);
                                    }
                                }
                                else {
                                    popular.add(p);
                                }
                                Toast.makeText(context, name(p.name), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Programm p=programms.get(i-2*column);
                        int index=indexOf(popular,p);
                        if (index>=0){
                            p=popular.get(index);
                            popular.remove(index);
                            if (index<column)
                                notifyItemChanged(index);
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
                        if (flag>=0 && flag<column){
                            for (int j=0; j<column; j++){
                                notifyItemChanged(j);
                            }
                        }
                        else {
                            popular.add(p);
                        }
                        Toast.makeText(context, name(p.name), Toast.LENGTH_SHORT).show();
                    }
                };
                personViewHolder.cv.setOnClickListener(listener);
                break;
            }
            case 0:{
                Log.d("here", "0");
                if (i==0)
                    personViewHolder.programmName.setText(context.getString(R.string.popAp)+" "+name(popular.get(i).name));
                else {
                    personViewHolder.programmName.setText(name(popular.get(i).name));
                }
                personViewHolder.programmPhoto.setImageResource(popular.get(i).photoId);
                break;
            }
            case 1:{
                Log.d("here", "1");
                if (i-column==0)
                    personViewHolder.programmName.setText(context.getString(R.string.newAp)+" "+name(news.get(i-column).name));
                else{
                    personViewHolder.programmName.setText(name(news.get(i-column).name));
                }
                personViewHolder.programmPhoto.setImageResource(news.get(i-column).photoId);
                break;
            }
        }
    }
    private String name(int i){
        i++;
        int y=0;
        String str;
        StringBuilder stringBuilder=new StringBuilder();
        while(i!=0){
            y=i%16;
            i=i/16;
            switch (y){
                case 0: stringBuilder.append(context.getString(R.string.zero));
                    break;
                case 1: stringBuilder.append(context.getString(R.string.one));
                    break;
                case 2: stringBuilder.append(context.getString(R.string.two));
                    break;
                case 3: stringBuilder.append(context.getString(R.string.three));
                    break;
                case 4: stringBuilder.append(context.getString(R.string.four));
                    break;
                case 5: stringBuilder.append(context.getString(R.string.five));
                    break;
                case 6: stringBuilder.append(context.getString(R.string.six));
                    break;
                case 7: stringBuilder.append(context.getString(R.string.seven));
                    break;
                case 8: stringBuilder.append(context.getString(R.string.eight));
                    break;
                case 9: stringBuilder.append(context.getString(R.string.nine));
                    break;
                case 10: stringBuilder.append(context.getString(R.string.ten));
                    break;
                case 11: stringBuilder.append(context.getString(R.string.eleven));
                    break;
                case 12: stringBuilder.append(context.getString(R.string.twelve));
                    break;
                case 13: stringBuilder.append(context.getString(R.string.thirteen));
                    break;
                case 14: stringBuilder.append(context.getString(R.string.fourteen));
                    break;
                case 15: stringBuilder.append(context.getString(R.string.fifteen));
                    break;
                default:
                    break;
            }
        }
        return stringBuilder.reverse().toString();
    }
    private int image(int position){
        Log.d("im", "image");
        int forReturn=R.mipmap.ic_launcher;
        boolean flag=true;
        if ((position+1)%column==1){
            ss=new TreeSet<>();
        }
        int i=0;
        while(ss.contains(i) || flag) {
            Log.d("im", "imagecycle");
            Log.d("im", String.valueOf(position));
            Log.d("im", String.valueOf(ss.size()));
            i = random.nextInt(12);
            Log.d("im", String.valueOf(i));
            switch (i) {
                case 0:
                    forReturn = R.drawable.ic_local_gas_station_black;
                    break;
                case 1:
                    forReturn = R.drawable.ic_notifications_black;
                    break;
                case 2:
                    forReturn = R.drawable.ic_work_black;
                    break;
                case 3:
                    forReturn = R.drawable.ic_directions_car_black;
                    break;
                case 4:
                    forReturn = R.drawable.ic_date_range_black;
                    break;
                case 5:
                    forReturn = R.drawable.ic_cloud_black;
                    break;
                case 6:
                    forReturn = R.drawable.ic_build_black;
                    break;
                case 7:
                    forReturn = R.drawable.ic_bug_report_black;
                    break;
                case 8:
                    forReturn = R.drawable.ic_audiotrack_black;
                    break;
                case 9:
                    forReturn = R.drawable.ic_assignment_returned_black;
                    break;
                case 10:
                    forReturn = R.drawable.ic_android_black;
                    break;
                case 11:
                    forReturn = R.drawable.ic_adb_black;
                    break;
                case 12:
                    forReturn = R.drawable.ic_accessibility_black;
                    break;
            }
                flag=position>=column && previous.get(position%column)==i;
            /*if (position>=column)
            Log.d("im", (String.valueOf(previous.size())));*/
        }
        ss.add(i);
        if (previous.size()<=position%column){
            previous.add(position % column, i);
        }
        else {
            previous.set(position % column, i);
        }
        return forReturn;
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
        return programms.size();
    }
}