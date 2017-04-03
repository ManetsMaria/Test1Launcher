package com.example.marya.firsttestingproject;

/**
 * Created by marya on 28.3.17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PageFragment extends Fragment {

    private int pageNumber;

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_page, container, false);
        ImageView imV=(ImageView)result.findViewById(R.id.imageView);
        TextView pageHeader=(TextView)result.findViewById(R.id.displayText);
        if (pageNumber==0){
            imV.setImageResource(R.mipmap.ic_launcher);
            pageHeader.setText(R.string.welcome);
        }
        if(pageNumber==1){
            imV.setImageResource(R.drawable.yandex_80x80);
            pageHeader.setText(R.string.firstdescription);
        }
        if (pageNumber==2){
            imV.setImageResource(R.drawable.page3_80x80);
            pageHeader.setText(R.string.seconddescription);
        }
        return result;
    }
}