package com.itvrach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itvrach.www.itvrach.R;

/**
 * Created by engineer on 10/30/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    String[] tvshows;
    //String[] tvdsts;

    public MyAdapter(Context c, String[] tvshows) {
        this.c = c;
        this.tvshows = tvshows;
      //  this.tvdsts = tvdsts;
    }

    //INITIALIE VH
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        MyHolder holder=new MyHolder(v);
        return holder;
    }

    //BIND DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        TextView nameTxt;
       // TextView valueTxt;

        holder.nameTxt.setText(tvshows[position]);
        //holder.valueTxt.setText(tvdsts);
    }

    @Override
    public int getItemCount() {
        return tvshows.length;
    }
}